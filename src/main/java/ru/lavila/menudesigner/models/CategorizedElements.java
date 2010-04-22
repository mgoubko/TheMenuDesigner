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
        List<Element> categoryElements = data.get(category);
        if (categoryElements == null)
        {
            categoryElements = new ArrayList<Element>();
            data.put(category, categoryElements);
        }
        categoryElements.addAll(elements);
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
        return data.get(category);
    }
}
