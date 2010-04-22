package ru.lavila.menudesigner.models;

import java.util.*;

public class Hierarchy implements Category.CategoryListener
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
            if (element instanceof Category)
            {
                Category category = (Category) element;
                category.addModelListener(this);
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

    private void prepareToRemove(Category parent, Collection<Element> elements, Map<Category, Collection<Element>> collector)
    {
        collector.put(parent, elements);
        for (Element element : elements)
        {
            if (element instanceof Category)
            {
                Category category = (Category) element;
                category.removeModelListener(this);
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

    public void elementsAdded(Category category, Element... elements)
    {
        HashSet<Element> toRemove = new HashSet<Element>();
        processNewElements(category, Arrays.asList(elements), toRemove);
        fireElementsAdded(category, elements);
        for (Element element : toRemove)
        {
            items.get(element).remove(element);
        }
    }

    private void fireElementsAdded(Category category, Element... elements)
    {
        for (HierarchyListener listener : listeners)
        {
            listener.elementsAdded(category, elements);
        }
    }

    public void elementsRemoved(Category category, Element... elements)
    {
        Map<Category, Collection<Element>> collector = new HashMap<Category, Collection<Element>>();
        prepareToRemove(category, Arrays.asList(elements), collector);
        fireElementsRemoved(collector);
    }

    private void fireElementsRemoved(Map<Category, Collection<Element>> elementsMap)
    {
        for (HierarchyListener listener : listeners)
        {
            listener.elementsRemoved(elementsMap);
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
