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
        CategorizedElements added = new CategorizedElements();
        CategorizedElements removed = new CategorizedElements();
        processNewElements(category, Arrays.asList(elements), added, removed);
        HashSet<Category> updatedCategories = new HashSet<Category>();
        updatedCategories.addAll(added.getCategories());
        updatedCategories.addAll(removed.getCategories());
        if (!updatedCategories.isEmpty())
        {
            categoriesStructureChanged(updatedCategories);
            if (!removed.isEmpty())
            {
                fireStructureChangeEvent(new StructureChangeEventImpl(this, StructureChangeEvent.EventType.ELEMENTS_REMOVED, removed));
            }
            if (!added.isEmpty())
            {
                fireStructureChangeEvent(new StructureChangeEventImpl(this, StructureChangeEvent.EventType.ELEMENTS_ADDED, added));
            }
        }
    }

    private void processNewElements(Category category, Collection<Element> elements, CategorizedElements added, CategorizedElements removed)
    {
        for (Element element : elements)
        {
            if (hierarchyElements.containsElement(element))
            {
                Category oldCategory = hierarchyElements.getCategoryFor(element);
                if (oldCategory != category)
                {
                    hierarchyElements.remove(oldCategory, element);
                    hierarchyElements.add(category, element);
                    removed.add(oldCategory, element);
                    added.add(category, element);
                }
            }
            else
            {
                element.addModelListener(this);
                hierarchyElements.add(category, element);
                added.add(category, element);

                if (element instanceof Category)
                {
                    Category subCategory = (Category) element;
                    if (!subCategory.isEmpty())
                    {
                        processNewElements(subCategory, subCategory.getElements(), added, removed);
                    }
                }
            }
        }
    }

    public void remove(Element... elements)
    {
        CategorizedElements removed = new CategorizedElements();
        processRemove(Arrays.asList(elements), removed);
        categoriesStructureChanged(removed.getCategories());
        if (!removed.isEmpty())
        {
            fireStructureChangeEvent(new StructureChangeEventImpl(this, StructureChangeEvent.EventType.ELEMENTS_REMOVED, removed));
        }
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
        private final CategorizedElements elements;

        public StructureChangeEventImpl(Hierarchy source, EventType type, CategorizedElements elements)
        {
            this.source = source;
            this.type = type;
            this.elements = elements;
        }

        public StructureChangeEventImpl(Hierarchy source, EventType type, Category category, Element... children)
        {
            this.source = source;
            this.type = type;
            elements = new CategorizedElements();
            elements.add(category, children);
        }

        public Hierarchy getSource()
        {
            return source;
        }

        public EventType getType()
        {
            return type;
        }

        public CategorizedElements getCategorizedElements()
        {
            return elements;
        }
    }
}
