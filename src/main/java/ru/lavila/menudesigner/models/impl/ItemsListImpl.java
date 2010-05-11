package ru.lavila.menudesigner.models.impl;

import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.events.*;

import java.util.*;

public class ItemsListImpl implements ItemsList, ElementListener, HierarchyListener
{
    private final List<Item> items;
    private final List<Hierarchy> hierarchies;
    private final List<ItemsListListener> listeners;

    public ItemsListImpl()
    {
        items = new ArrayList<Item>();
        hierarchies = new ArrayList<Hierarchy>();
        listeners = new ArrayList<ItemsListListener>();
    }

    public Item[] toArray()
    {
        return items.toArray(new Item[items.size()]);
    }

    public Item get(int index)
    {
        return items.get(index);
    }

    public int indexOf(Item item)
    {
        return items.indexOf(item);
    }

    public int size()
    {
        return items.size();
    }

    public Item newItem(String name, double popularity)
    {
        Item item = new ItemImpl(name, popularity);
        add(null, item);
        return item;
    }

    private void add(Hierarchy sourceHierarchy, Item... newItems)
    {
        List<Item> added = new ArrayList<Item>();
        List<Integer> indexes = new ArrayList<Integer>();

        for (Item item : newItems)
        {
            if (!items.contains(item))
            {
                items.add(item);
                item.addModelListener(this);
                added.add(item);
                indexes.add(items.indexOf(item));
            }
        }

        if (!added.isEmpty())
        {
            fireListChanged(new ItemsListChangeEventImpl(ItemsListChangeEvent.EventType.ELEMENTS_ADDED, added, indexes));

            for (Hierarchy hierarchy : hierarchies)
            {
                if (hierarchy != sourceHierarchy && hierarchy.isTaxomony())
                {
                    hierarchy.add(hierarchy.getRoot(), added.toArray(new Item[added.size()]));
                }
            }
        }
    }

    public void remove(Item... toRemove)
    {
        remove(null, toRemove);
    }

    private void remove(Hierarchy sourceHierarchy, Item... toRemove)
    {
        List<Item> removed = new ArrayList<Item>();
        List<Integer> indexes = new ArrayList<Integer>();

        for (Item item : toRemove)
        {
            if (items.contains(item))
            {
                removed.add(item);
                indexes.add(items.indexOf(item));
                item.removeModelListener(this);
                items.remove(item);
            }
        }

        if (!removed.isEmpty())
        {
            fireListChanged(new ItemsListChangeEventImpl(ItemsListChangeEvent.EventType.ELEMENTS_REMOVED, removed, indexes));

            for (Hierarchy hierarchy : hierarchies)
            {
                if (hierarchy != sourceHierarchy)
                {
                    hierarchy.remove(removed.toArray(new Item[removed.size()]));
                }
            }
        }
    }

    public List<Hierarchy> getHierarchies()
    {
        return Collections.unmodifiableList(hierarchies);
    }

    public Hierarchy newHierarchy(String name, boolean taxomony)
    {
        Hierarchy hierarchy = new HierarchyImpl(name, taxomony);
        hierarchy.addModelListener(this);
        hierarchies.add(hierarchy);
        if (taxomony)
        {
            hierarchy.add(hierarchy.getRoot(), toArray());
        }
        return hierarchy;
    }

    public void addModelListener(ItemsListListener listener)
    {
        listeners.add(listener);
    }

    public void removeModelListener(ItemsListListener listener)
    {
        listeners.remove(listener);
    }

    private void fireListChanged(ItemsListChangeEvent event)
    {
        for (ItemsListListener listener : listeners)
        {
            listener.listChanged(event);
        }
    }

    private void fireModelChanged(ElementChangeEvent event)
    {
        for (ItemsListListener listener : listeners)
        {
            listener.itemChanged(event);
        }
    }

    public void modelChanged(ElementChangeEvent event)
    {
        fireModelChanged(event);
    }

    public void elementChanged(ElementChangeEvent event)
    {
    }

    public void structureChanged(StructureChangeEvent event)
    {
        switch (event.getType())
        {
            case ELEMENTS_ADDED:
                add(event.getSource(), extractItems(event.getCategorizedElements()));
                break;
            case ELEMENTS_REMOVED:
                if (event.getSource().isTaxomony())
                {
                    remove(event.getSource(), extractItems(event.getCategorizedElements()));
                }
                break;
        }
    }

    private Item[] extractItems(CategorizedElements categorizedElements)
    {
        Collection<Item> items = new HashSet<Item>();
        for (Element element : categorizedElements.getAllElements())
        {
            if (element instanceof Item)
            {
                items.add((Item) element);
            }
        }
        return items.toArray(new Item[items.size()]);
    }

    private static class ItemsListChangeEventImpl implements ItemsListChangeEvent
    {
        private final EventType type;
        private final List<Item> items;
        private final List<Integer> indexes;

        public ItemsListChangeEventImpl(EventType type, List<Item> items, List<Integer> indexes)
        {
            this.type = type;
            this.items = items;
            this.indexes = indexes;
        }

        public EventType getType()
        {
            return type;
        }

        public List<Item> getItems()
        {
            return items;
        }

        public List<Integer> getIndexes()
        {
            return indexes;
        }
    }
}
