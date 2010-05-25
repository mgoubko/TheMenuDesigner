package ru.lavila.menudesigner.models;

import java.util.*;

public class CategorizedElements
{
    private final Map<Category, List<Element>> data;
    private final List<Category> categories;

    public CategorizedElements()
    {
        categories = new ArrayList<Category>();
        data = new HashMap<Category, List<Element>>();
    }

    public void add(Category category, List<Element> elements)
    {
        List<Element> categoryElements = getElementsFor(category);
        for (Element element : elements)
        {
            if (!categoryElements.contains(element)) categoryElements.add(element);
        }
    }

    public void add(Category category, int index, List<Element> elements)
    {
        List<Element> categoryElements = getElementsFor(category);
        for (Element element : elements)
        {
            if (!categoryElements.contains(element)) categoryElements.add(index++, element);
        }
    }

    public void remove(Category category, Element element)
    {
        getElementsFor(category).remove(element);
    }

    public void add(Category category, Element... elements)
    {
        add(category, Arrays.asList(elements));
    }

    public void add(Category category, int index, Element... elements)
    {
        add(category, index, Arrays.asList(elements));
    }

    public List<Category> getCategories()
    {
        return categories;
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

    public List<Element> getCategoryElements(Category category)
    {
        return Collections.unmodifiableList(getElementsFor(category));
    }

    public List<Element> getFirstCategoryElements()
    {
        return getCategories().isEmpty() ? Collections.<Element>emptyList() : getCategoryElements(getCategories().get(0));
    }

    public List<Item> getCategoryGroup(Category category)
    {
        List<Item> result = new ArrayList<Item>();
        for (Element element : getCategoryElements(category))
        {
            if (element instanceof Item)
            {
                result.add((Item) element);
            }
            else
            {
                result.addAll(getCategoryGroup((Category) element));
            }
        }
        return result;
    }

    private List<Element> getElementsFor(Category category)
    {
        List<Element> elements = data.get(category);
        if (elements == null)
        {
            elements = new ArrayList<Element>();
            data.put(category, elements);
            categories.add(category);
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
