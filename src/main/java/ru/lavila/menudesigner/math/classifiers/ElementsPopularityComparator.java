package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Element;

import java.util.Comparator;

public class ElementsPopularityComparator implements Comparator<Element> {
    public int compare(Element element1, Element element2) {
        return Double.compare(element2.getPopularity(), element1.getPopularity());
    }
}
