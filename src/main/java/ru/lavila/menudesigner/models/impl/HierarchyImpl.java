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

    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<Category>();
        for (Element element : hierarchyElements.getAllElements())
        {
            if (element instanceof Category)
            {
                categories.add((Category) element);
            }
        }
        return categories;
    }

    public Category getElementCategory(Element element)
    {
        return hierarchyElements.getCategoryFor(element);
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
        category.addModelListener(this);
        hierarchyElements.add(parentCategory, category);
        CategorizedElements added = new CategorizedElements();
        added.add(parentCategory, category);
        fireStructureChangeEvent(new StructureChangeEventImpl(this, added, null));
        return category;
    }

    public void add(Category category, Element... elements)
    {
        add(category, -1, elements);
    }

    public void add(Category category, int index, Element... elements)
    {
        if (elements.length == 0) return;
        CategorizedElements added = new CategorizedElements();
        CategorizedElements removed = new CategorizedElements();
        processNewElements(category, Arrays.asList(elements), index, added, removed);
        HashSet<Category> updatedCategories = new HashSet<Category>();
        updatedCategories.addAll(added.getCategories());
        updatedCategories.addAll(removed.getCategories());
        if (!updatedCategories.isEmpty())
        {
            categoriesStructureChanged(updatedCategories);
            fireStructureChangeEvent(new StructureChangeEventImpl(this, added, removed));
        }
    }

    private void processNewElements(Category category, Collection<Element> elements, int index, CategorizedElements added, CategorizedElements removed)
    {
        CategorizedElements nextLevel = new CategorizedElements();
        for (Element element : elements)
        {
            if (added.getAllElements().contains(element)) continue;
            if (hierarchyElements.containsElement(element))
            {
                Category oldCategory = hierarchyElements.getCategoryFor(element);
                if (oldCategory.equals(category))
                {
                    if (hierarchyElements.getCategoryElements(category).indexOf(element) <= index) index--;
                }
                hierarchyElements.remove(oldCategory, element);
                removed.add(oldCategory, element);
                if (element instanceof Category)
                {
                    Category cat = (Category) element;
                    nextLevel.add(cat, cat.getElements());
                }
            }
            else
            {
                if (element instanceof Category)
                {
                    Category cat = (Category) element;
                    element = new CategoryImpl(element.getName());
                    nextLevel.add((Category) element, cat.getElements());
                }
                element.addModelListener(this);
            }
            if (index < 0)
            {
                hierarchyElements.add(category, element);
            }
            else
            {
                hierarchyElements.add(category, index++, element);
            }
            added.add(category, element);
        }

        for (Category subCategory : nextLevel.getCategories())
        {
            processNewElements(subCategory, nextLevel.getCategoryElements(subCategory), -1, added, removed);
        }
    }

    public void remove(Element... elements)
    {
        if (elements.length == 0) return;
        CategorizedElements removed = new CategorizedElements();
        processRemove(Arrays.asList(elements), removed);
        categoriesStructureChanged(removed.getCategories());
        if (!removed.isEmpty())
        {
            fireStructureChangeEvent(new StructureChangeEventImpl(this, null, removed));
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
                        processRemove(new ArrayList<Element>(category.getElements()), removed);
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
            return hierarchyElements.getCategoryElements(this);
        }

        public List<Item> getGroup()
        {
            return hierarchyElements.getCategoryGroup(this);
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
        private final CategorizedElements elementsAdded;
        private final CategorizedElements elementsRemoved;

        public StructureChangeEventImpl(Hierarchy source, CategorizedElements elementsAdded, CategorizedElements elementsRemoved)
        {
            this.source = source;
            this.elementsAdded = elementsAdded == null ? new CategorizedElements() : elementsAdded;
            this.elementsRemoved = elementsRemoved == null ? new CategorizedElements() : elementsRemoved;
        }

        public Hierarchy getSource()
        {
            return source;
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
