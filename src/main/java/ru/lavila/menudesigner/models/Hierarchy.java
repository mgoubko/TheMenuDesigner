package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.*;

import java.util.*;

public class Hierarchy implements ElementListener
{
    public final Category root;
    private final List<HierarchyListener> listeners;
    private final Map<Item, Item> sourceItems;
    private final Map<Item, Category> items;

    public Hierarchy()
    {
        root = new RootCategoryImpl();
        root.addModelListener(this);
        listeners = new ArrayList<HierarchyListener>();
        sourceItems = new HashMap<Item, Item>();
        items = new HashMap<Item, Category>();
    }

    public List<Item> getItems()
    {
        return getItems(root);
    }

    private List<Item> getItems(Category category)
    {
        List<Item> items = new ArrayList<Item>();
        for (Element element : category.getElements())
        {
            if (element instanceof Item)
            {
                items.add((Item) element);
            }
            else if (element instanceof Category)
            {
                items.addAll(getItems((Category) element));
            }
        }
        return items;
    }

    public void addModelListener(HierarchyListener listener)
    {
        listeners.add(listener);
    }

    private void processNewElements(Category parent, Collection<Element> elements, Collection<Element> toRemove)
    {
        for (Element element : elements)
        {
            element.addModelListener(this);
            if (element instanceof Category)
            {
                Category category = (Category) element;
                if (category.elementsCount() > 0)
                {
                    processNewElements(category, category.getElements(), toRemove);
                }
            }
            else if (element instanceof Item)
            {
                Item item = (Item) element;
                Item sourceItem = getSource(item);
                if (sourceItems.containsKey(sourceItem))
                {
                    toRemove.add(sourceItems.get(sourceItem));
                }
                items.put(item, parent);
                sourceItems.put(sourceItem, item);
            }
        }
    }

    private void prepareToRemove(Category parent, List<Element> elements, CategorizedElements collector)
    {
        collector.add(parent, elements);
        for (Element element : elements)
        {
            element.removeModelListener(this);
            if (element instanceof Category)
            {
                Category category = (Category) element;
                if (category.elementsCount() > 0)
                {
                    prepareToRemove(category, category.getElements(), collector);
                }
            }
            else if (element instanceof Item)
            {
                Item item = (Item) element;
                Item sourceItem = getSource(item);
                items.remove(item);
                if (sourceItems.get(sourceItem) == item) sourceItems.remove(getSource(item));
            }
        }
    }

    private Item getSource(Item item)
    {
        while (item instanceof ItemAliasImpl)
        {
            item = ((ItemAliasImpl) item).sourceItem;
        }
        return item;
    }

    public void modelChanged(ElementChangeEvent event)
    {
        fireModelChangeEvent(event);
    }

    private void fireModelChangeEvent(ElementChangeEvent event)
    {
        for (HierarchyListener listener : listeners)
        {
            listener.modelChanged(event);
        }
    }

    public Category newCategory(Category parentCategory, String name)
    {
        Category category = new CategoryImpl(name);
        add(parentCategory, category);
        return category;
    }

    public Item newItem(Category parentCategory, String name, double popularity)
    {
        Item item = new ItemImpl(name, popularity);
        add(parentCategory, item);
        return item;
    }

    public Item newItem(Category parentCategory, Item sourceItem)
    {
        ItemAliasImpl item = new ItemAliasImpl(sourceItem);
        add(parentCategory, item);
        return item;
    }

    public void add(Category category, Element... elements)
    {
        HashSet<Element> toRemove = new HashSet<Element>();
        processNewElements(category, Arrays.asList(elements), toRemove);
        for (Element element : toRemove)
        {
            remove(items.get(element), element);
        }
        ((CategoryImpl) category).add(elements);
        fireStructureChangeEvent(new StructureChangeEventImpl(StructureChangeEvent.EventType.ELEMENTS_ADDED, category, elements));
    }

    //todo: support deletion of elements from several categories at once
    public void remove(Category category, Element... elements)
    {
        CategorizedElements collector = new CategorizedElements();
        prepareToRemove(category, Arrays.asList(elements), collector);
        ((CategoryImpl) category).remove(elements);
        fireStructureChangeEvent(new StructureChangeEventImpl(StructureChangeEvent.EventType.ELEMENTS_REMOVED, collector));
    }

    private void fireStructureChangeEvent(StructureChangeEvent event)
    {
        for (HierarchyListener listener : listeners)
        {
            listener.structureChanged(event);
        }
    }

    private static class RootCategoryImpl extends CategoryImpl
    {
        public RootCategoryImpl()
        {
            super("Menu");
        }
    }
}
