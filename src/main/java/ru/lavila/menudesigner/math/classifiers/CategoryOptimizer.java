package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Hierarchy;

public interface CategoryOptimizer {
    public Split optimize(Hierarchy taxonomy);
}
