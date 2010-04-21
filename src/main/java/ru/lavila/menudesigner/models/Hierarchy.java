package ru.lavila.menudesigner.models;

import java.util.ArrayList;
import java.util.List;

public class Hierarchy implements Category.CategoryListener
{
    public final Category root;
    private final List<HierarchyListener> listeners;

    public Hierarchy()
    {
        root = new RootCategoryImpl();
        listenTo(root);
        listeners = new ArrayList<HierarchyListener>();
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

    private void listenTo(Element... elements)
    {
        for (Element element : elements)
        {
            if (element instanceof Category)
            {
                Category category = (Category) element;
                category.addModelListener(this);
                if (category.elementsCount() > 0)
                {
                    listenTo(category.getElements().toArray(new Element[category.elementsCount()]));
                }
            }
        }
    }

    private void stopListeningTo(Element... elements)
    {
        for (Element element : elements)
        {
            if (element instanceof Category)
            {
                Category category = (Category) element;
                category.removeModelListener(this);
                if (category.elementsCount() > 0)
                {
                    stopListeningTo(category.getElements().toArray(new Element[category.elementsCount()]));
                }
            }
        }
    }

    public void elementsAdded(Category category, Element... elements)
    {
        listenTo(elements);
        fireElementsAdded(category, elements);
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
        stopListeningTo(elements);
        fireElementsRemoved(category, elements);
    }

    private void fireElementsRemoved(Category category, Element... elements)
    {
        for (HierarchyListener listener : listeners)
        {
            listener.elementsRemoved(category, elements);
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
