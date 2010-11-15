package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;

import java.util.*;

public abstract class AbstractClassifier {
    protected final Hierarchy targetHierarchy;
    protected final Category category;

    public AbstractClassifier(Hierarchy targetHierarchy, Category category) {
        this.targetHierarchy = targetHierarchy;
        this.category = category;
    }

    protected void cleanupCategory() {
        List<Element> children = category.getElements();
        targetHierarchy.remove(children.toArray(new Element[children.size()]));
    }

    protected void applySplitToCategory(List<Element> split) {
        targetHierarchy.add(category, split.toArray(new Element[split.size()]));
    }
}
