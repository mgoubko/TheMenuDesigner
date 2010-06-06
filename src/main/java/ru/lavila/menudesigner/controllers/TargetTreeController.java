package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.math.TaxonomyClassifier;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;

public class TargetTreeController
{
    private final TaxonomyClassifier classifier;

    public TargetTreeController(Hierarchy hierarchy, ItemsListCalculator calculator)
    {
        //todo: listen for menu model change
        this.classifier = new TaxonomyClassifier(calculator.getMenuModel(), hierarchy);
    }

    public void classifyByTaxonomy(Hierarchy taxonomy, Category category)
    {
        classifier.classifyByTaxonomy(taxonomy, category);
    }
}
