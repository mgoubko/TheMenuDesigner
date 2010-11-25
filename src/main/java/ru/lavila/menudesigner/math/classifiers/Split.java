package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Element;

import java.util.List;

class Split {
    public final List<Element> elements;
    public final double evaluation;

    Split(List<Element> elements) {
        this(elements, -1);
    }

    Split(List<Element> elements, double evaluation) {
        this.elements = elements;
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        String result = elements.size() + " / " + evaluation;
//        for (Element element : elements) {
//            result += " # " + element.getName();
//        }
        return result;
    }
}
