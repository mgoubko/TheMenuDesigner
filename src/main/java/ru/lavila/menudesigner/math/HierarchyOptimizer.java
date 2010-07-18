package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.*;

import java.util.List;

public class HierarchyOptimizer
{
    private final Hierarchy targetHierarchy;
    private final HierarchyCalculator calculator;
    private final List<Hierarchy> taxonomies;

    public HierarchyOptimizer(Hierarchy targetHierarchy, HierarchyCalculator calculator, ItemsList itemsList)
    {
        this.targetHierarchy = targetHierarchy;
        this.calculator = calculator;
        this.taxonomies = itemsList.getTaxonomies();
    }

    public void optimize()
    {
        Category category = targetHierarchy.getRoot();
        optimizeCategory(category);
    }

    private void optimizeCategory(Category category)
    {
        double best = calculator.getCategoryTimeLoss(category);
        Hierarchy bestTaxonomy = null;
        for (Hierarchy taxonomy : taxonomies)
        {
            new CategoryClassifier(targetHierarchy, category).optimize(taxonomy, calculator.getMenuModel());
            double timeLoss = calculator.getCategoryTimeLoss(category);
            if (best == -1 || timeLoss < best)
            {
                best = timeLoss;
                bestTaxonomy = taxonomy;
            }
        }
        if (bestTaxonomy == null)
        {
            new CategoryClassifier(targetHierarchy, category).flatten();
        }
        else
        {
            new CategoryClassifier(targetHierarchy, category).optimize(bestTaxonomy, calculator.getMenuModel());
            for (Element element : category.getElements())
            {
                if (element instanceof Category)
                {
                    optimizeCategory((Category) element);
                }
            }
        }
    }
}
