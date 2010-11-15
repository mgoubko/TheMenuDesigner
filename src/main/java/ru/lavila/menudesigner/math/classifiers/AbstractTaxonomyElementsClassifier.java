package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbstractTaxonomyElementsClassifier extends AbstractClassifier {
    public AbstractTaxonomyElementsClassifier(Hierarchy targetHierarchy, Category category) {
        super(targetHierarchy, category);
    }

    protected List<TaxonomyElement> collectTaxonomyElements(Category taxonomyCategory, List<Item> targetItems, double ignoreLimit) {
        List<TaxonomyElement> taxonomyElements = new ArrayList<TaxonomyElement>();
        for (Element element : taxonomyCategory.getElements()) {
            if (element instanceof Item && targetItems.contains(element)) {
                taxonomyElements.add(new TaxonomyElement(element));
            } else if (element instanceof Category) {
                Category category = (Category) element;
                List<TaxonomyElement> childTaxonomyElements = collectTaxonomyElements(category, targetItems, ignoreLimit);
                if (!childTaxonomyElements.isEmpty()) {
                    TaxonomyElement categoryTaxonomyElement = new TaxonomyElement(category);
                    taxonomyElements.add(categoryTaxonomyElement);
                    for (TaxonomyElement taxonomyElement : childTaxonomyElements) {
                        categoryTaxonomyElement.addChild(taxonomyElement);
                        if (taxonomyElement.popularity >= ignoreLimit) {
                            categoryTaxonomyElement.terminal = false;
                        }
                        taxonomyElement.addParent(categoryTaxonomyElement);
                        taxonomyElements.add(taxonomyElement);
                    }
                }
            }
        }
        Collections.sort(taxonomyElements);
        return taxonomyElements;
    }
}
