package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.List;

public class HierarchyOptimizer
{
    private final Hierarchy targetHierarchy;
    private final List<Hierarchy> taxonomies;
    private final MenuModel menuModel;
    private final CategoryEvaluator evaluator;

    public HierarchyOptimizer(Hierarchy targetHierarchy, MenuModel menuModel, CategoryEvaluator evaluator, ItemsList itemsList)
    {
        this.targetHierarchy = targetHierarchy;
        this.menuModel = menuModel;
        this.evaluator = evaluator;
        this.taxonomies = itemsList.getTaxonomies();
    }

    public void optimize()
    {
        Category category = targetHierarchy.getRoot();
        optimizeCategory(category);
    }

    private void optimizeCategory(Category category)
    {
        CategoryClassifier classifier = new CategoryClassifier(targetHierarchy, category);
        classifier.flatten();
        classifier.sortByPopularity();
        double best = evaluator.evaluate(category);
        Hierarchy bestTaxonomy = null;
        for (Hierarchy taxonomy : taxonomies)
        {
            new LocalSearchCategoryOptimizer(targetHierarchy, category, evaluator).optimize(taxonomy);
            double evaluation = evaluator.evaluate(category);
            if (best == -1 || evaluation < best)
            {
                best = evaluation;
                bestTaxonomy = taxonomy;
            }
        }
        if (bestTaxonomy == null)
        {
            classifier.flatten();
            classifier.sortByPopularity();
        }
        else
        {
            new LocalSearchCategoryOptimizer(targetHierarchy, category, evaluator).optimize(bestTaxonomy);
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
