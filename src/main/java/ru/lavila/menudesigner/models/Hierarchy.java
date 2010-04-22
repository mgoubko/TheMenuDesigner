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

    private void prepareToRemove(Category parent, Collection<Element> elements, Map<Category, Collection<Element>> collector)
    {
        collector.put(parent, elements);
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
        if (event instanceof StructureChangeEvent)
        {
            StructureChangeEvent structureEvent = (StructureChangeEvent) event;
            Category category = structureEvent.getElement();
            Collection<Element> elements = structureEvent.getDiff();

            switch (event.getType())
            {
                case ELEMENTS_ADDED:
                    HashSet<Element> toRemove = new HashSet<Element>();
                    processNewElements(category, elements, toRemove);
                    fireElementsAdded(category, elements.toArray(new Element[elements.size()]));
                    for (Element element : toRemove)
                    {
                        items.get(element).remove(element);
                    }
                    break;
                case ELEMENTS_REMOVED:
                    Map<Category, Collection<Element>> collector = new HashMap<Category, Collection<Element>>();
                    prepareToRemove(category, elements, collector);
                    fireElementsRemoved(collector);
                    break;
            }
        }
        else
        {
            switch (event.getType())
            {
                case NAME_CHANGED:
                    //todo
                    break;
                case POPULARITY_CHANGED:
                    //todo
                    break;
            }
        }
    }

    private void fireElementsAdded(Category category, Element... elements)
    {
        for (HierarchyListener listener : listeners)
        {
            listener.elementsAdded(category, elements);
        }
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
