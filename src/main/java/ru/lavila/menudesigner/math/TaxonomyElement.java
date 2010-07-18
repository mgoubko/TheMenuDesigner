package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Item;

import java.util.ArrayList;
import java.util.List;

class TaxonomyElement implements Comparable
{
    public final Element element;
    public final List<TaxonomyElement> parents;
    public final List<TaxonomyElement> children;
    public double popularity;
    public boolean terminal;

    TaxonomyElement(Element element)
    {
        this.element = element;
        this.parents = new ArrayList<TaxonomyElement>();
        this.children = new ArrayList<TaxonomyElement>();
        popularity = element instanceof Item ? element.getPopularity() : 0;
        terminal = true;
    }

    public void addParent(TaxonomyElement parent)
    {
        parents.add(parent);
    }

    public void addChild(TaxonomyElement element)
    {
        children.add(element);
        if (element.element instanceof Item)
        {
            popularity += element.popularity;
        }
    }

    public int compareTo(Object o)
    {
        if (!(o instanceof TaxonomyElement))
            throw new IllegalArgumentException("Invalid comparison of taxonomy element");
        return Double.compare(((TaxonomyElement) o).popularity, popularity);
    }

    @Override
    public String toString()
    {
        return String.format("[%.3f] %s. ", popularity, element.getName()) +
                elementNames("PARENTS", parents) + elementNames("CHILDREN", children);
    }

    private String elementNames(String title, List<TaxonomyElement> elements)
    {
        if (elements.isEmpty()) return "";
        String result = title + ": ";
        for (TaxonomyElement taxonomyElement : elements)
        {
            result += taxonomyElement.element.getName() + ", ";
        }
        return result;
    }
}
