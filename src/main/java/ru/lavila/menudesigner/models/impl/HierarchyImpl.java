package ru.lavila.menudesigner.models.impl;

import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.events.*;

import java.util.*;

class HierarchyImpl implements Hierarchy, ElementListener
{
    private final Category root;
    private final List<HierarchyListener> listeners;
    private final CategorizedElements hierarchyElements;
    private final boolean taxomony;

    public HierarchyImpl(String name, boolean taxomony)
    {
        this.taxomony = taxomony;
        hierarchyElements = new CategorizedElements();
        root = new CategoryImpl(name);
        root.addModelListener(this);
        hierarchyElements.add(null, root);
        listeners = new ArrayList<HierarchyListener>();
    }

    public Category getRoot()
    {
        return root;
    }

    public boolean isTaxomony()
    {
        return taxomony;
    }

    public Item newItem(Category parentCategory, String name, double popularity)
    {
        Item item = new ItemImpl(name, popularity);
        add(parentCategory, item);
        return item;
    }

    public Category newCategory(Category parentCategory, String name)
    {
        Category category = new CategoryImpl(name);
        add(parentCategory, category);
        return category;
    }

    public void add(Category category, Element... elements)
    {
        if (elements.length == 0) return;
        CategorizedElements added = new CategorizedElements();
        CategorizedElements movedFrom = new CategorizedElements();
        CategorizedElements movedTo = new CategorizedElements();
        processNewElements(category, Arrays.asList(elements), true, added, movedFrom, movedTo);
        HashSet<Category> updatedCategories = new HashSet<Category>();
        updatedCategories.addAll(added.getCategories());
        updatedCategories.addAll(movedFrom.getCategories());
        updatedCategories.addAll(movedTo.getCategories());
        if (!updatedCategories.isEmpty())
        {
            categoriesStructureChanged(updatedCategories);
            if (!movedFrom.isEmpty())
            {
                fireStructureChangeEvent(new StructureChangeEventImpl(this, StructureChangeEvent.EventType.ELEMENTS_MOVED, movedTo, movedFrom));
            }
            if (!added.isEmpty())
            {
                fireStructureChangeEvent(new StructureChangeEventImpl(this, StructureChangeEvent.EventType.ELEMENTS_ADDED, added, null));
            }
        }
    }

    void addSilent(Category category, Element... elements)
    {
        processNewElements(category, Arrays.asList(elements), false, null, null, null);
    }

    private void processNewElements(Category category, Collection<Element> elements, boolean moveExisting, CategorizedElements added, CategorizedElements movedFrom, CategorizedElements movedTo)
    {
        for (Element element : elements)
        {
            if (hierarchyElements.containsElement(element))
            {
                if (moveExisting)
                {
                    Category oldCategory = hierarchyElements.getCategoryFor(element);
                    if (oldCategory != category)
                    {
                        hierarchyElements.remove(oldCategory, element);
                        hierarchyElements.add(category, element);
                        if (movedFrom != null) movedFrom.add(oldCategory, element);
                        if (movedTo != null) movedTo.add(category, element);
                    }
                }
            }
            else
            {
                element.addModelListener(this);
                hierarchyElements.add(category, element);
                if (added != null) added.add(category, element);

                if (element instanceof Category)
                {
                    Category subCategory = (Category) element;
                    if (!subCategory.isEmpty())
                    {
                        processNewElements(subCategory, subCategory.getElements(), moveExisting, added, movedFrom, movedTo);
                    }
                }
            }
        }
    }

    public void remove(Element... elements)
    {
        remove(false, elements);
    }

    void removeSilent(Element... elements)
    {
        remove(true, elements);
    }

    private void remove(boolean silent, Element... elements)
    {
        if (elements.length == 0) return;
        CategorizedElements removed = new CategorizedElements();
        processRemove(Arrays.asList(elements), removed);
        categoriesStructureChanged(removed.getCategories());
        if (silent || removed.isEmpty()) return;
        fireStructureChangeEvent(new StructureChangeEventImpl(this, StructureChangeEvent.EventType.ELEMENTS_REMOVED, null, removed));
    }

    private void processRemove(Collection<Element> elements, CategorizedElements removed)
    {
        for (Element element : elements)
        {
            Category parent = hierarchyElements.getCategoryFor(element);
            if (parent != null)
            {
                hierarchyElements.remove(parent, element);
                removed.add(parent, element);
                element.removeModelListener(this);
                if (element instanceof Category)
                {
                    Category category = (Category) element;
                    if (!category.isEmpty())
                    {
                        processRemove(category.getElements(), removed);
                    }
                }
            }
        }
    }

    public void addModelListener(HierarchyListener listener)
    {
        listeners.add(listener);
    }

    public void removeModelListener(HierarchyListener listener)
    {
        listeners.remove(listener);
    }

    public void modelChanged(ElementChangeEvent event)
    {
        fireModelChangeEvent(event);

        // once element popularity changes its category popularity should change too
        if (event.getType() == ElementChangeEvent.EventType.POPULARITY_CHANGED)
        {
            Category category = hierarchyElements.getCategoryFor(event.getElement());
            if (category != null)
            {
                ((CategoryImpl) category).firePopularityChanged();
            }
        }

    }

    private void fireModelChangeEvent(ElementChangeEvent event)
    {
        for (HierarchyListener listener : listeners)
        {
            listener.elementChanged(event);
        }
    }

    private void fireStructureChangeEvent(StructureChangeEvent event)
    {
        for (HierarchyListener listener : listeners)
        {
            listener.structureChanged(event);
        }
    }

    private void categoriesStructureChanged(Collection<Category> categories)
    {
        for (Category category : categories)
        {
            ((CategoryImpl) category).firePopularityChanged();
        }
    }

    private class CategoryImpl extends ElementImpl implements Category
    {
        CategoryImpl(String name)
        {
            super(name);
        }

        public List<Element> getElements()
        {
            return hierarchyElements.getElementsFor(this);
        }

        public boolean isEmpty()
        {
            return getElements().isEmpty();
        }

        public double getPopularity()
        {
            double popularity = 0;
            for (Element element : getElements())
            {
                popularity += element.getPopularity();
            }
            return popularity;
        }
    }

    private static class StructureChangeEventImpl implements StructureChangeEvent
    {
        private final Hierarchy source;
        private final EventType type;
        private final CategorizedElements elementsAdded;
        private final CategorizedElements elementsRemoved;

        public StructureChangeEventImpl(Hierarchy source, EventType type, CategorizedElements elementsAdded, CategorizedElements elementsRemoved)
        {
            this.source = source;
            this.type = type;
            this.elementsAdded = elementsAdded;
            this.elementsRemoved = elementsRemoved;
        }

        public Hierarchy getSource()
        {
            return source;
        }

        public EventType getType()
        {
            return type;
        }

        public CategorizedElements getElementsAdded()
        {
            return elementsAdded;
        }

        public CategorizedElements getElementsRemoved()
        {
            return elementsRemoved;
        }
    }
}
