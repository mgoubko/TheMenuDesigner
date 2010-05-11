package ru.lavila.menudesigner.models;

import java.util.*;

public class CategorizedElements
{
    private final Map<Category, List<Element>> data;

    public CategorizedElements()
    {
        data = new HashMap<Category, List<Element>>();
    }

    public void add(Category category, List<Element> elements)
    {
        getElementsFor(category).addAll(elements);
    }

    public void remove(Category category, Element element)
    {
        getElementsFor(category).remove(element);
    }

    public void add(Category category, Element... elements)
    {
        add(category, Arrays.asList(elements));
    }

    public Collection<Category> getCategories()
    {
        return data.keySet();
    }

    public List<Element> getAllElements()
    {
        List<Element> elements = new ArrayList<Element>();
        for (Collection<Element> categoryElements : data.values())
        {
            elements.addAll(categoryElements);
        }
        return elements;
    }

    public List<Element> getElementsFor(Category category)
    {
        List<Element> elements = data.get(category);
        if (elements == null)
        {
            elements = new ArrayList<Element>();
            data.put(category, elements);
        }
        return elements;
    }
    
    public boolean containsElement(Element element)
    {
        for (Collection<Element> categoryElements : data.values())
        {
            if (categoryElements.contains(element)) return true;
        }
        return false;
    }

    public Category getCategoryFor(Element element)
    {
        for (Category category : data.keySet())
        {
            if (getElementsFor(category).contains(element)) return category;
        }
        return null;
    }

    public boolean isEmpty()
    {
        return getAllElements().isEmpty();
    }
}
