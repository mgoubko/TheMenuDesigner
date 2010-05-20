package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.HierarchyListener;
import ru.lavila.menudesigner.models.events.StructureChangeEvent;

import javax.swing.tree.*;
import java.util.*;

public class TreePresenter extends DefaultTreeModel implements HierarchyListener
{
    private final Hierarchy hierarchy;
    private final HierarchyCalculator calculator;
    private final Map<Element, ElementTreeNode> nodes;
    private final List<CalculationsListener> calculationsListeners;
    private boolean frozen = false;
    private final List<StructureChangeEvent> eventQueue;

    public TreePresenter(Hierarchy hierarchy, HierarchyCalculator calculator)
    {
        super(null);
        calculationsListeners = new ArrayList<CalculationsListener>();
        eventQueue = new ArrayList<StructureChangeEvent>();
        this.hierarchy = hierarchy;
        this.calculator = calculator;
        nodes = new HashMap<Element, ElementTreeNode>();
        setRoot(getTreeNode(hierarchy.getRoot()));
        hierarchy.addModelListener(this);
    }

    private ElementTreeNode buildNode(Element element)
    {
        ElementTreeNode node = new ElementTreeNode(element);
        nodes.put(element, node);
        return node;
    }

    private MutableTreeNode getTreeNode(Element element)
    {
        if (element instanceof Category)
        {
            DefaultMutableTreeNode node = buildNode(element);
            for (Element child : ((Category) element).getElements())
            {
                node.add(getTreeNode(child));
            }
            return node;
        }
        else
        {
            return buildNode(element);
        }
    }

    public List<Element> getSelectedElements(TreePath[] selectionPaths)
    {
        List<Element> selectedElements = new ArrayList<Element>();
        for (TreePath path : selectionPaths)
        {
            TreePresenter.ElementTreeNode childNode = (TreePresenter.ElementTreeNode) path.getLastPathComponent();
            selectedElements.add(childNode.element);
        }
        return selectedElements;
    }

    public Category getCategoryFromPath(TreePath path)
    {
        TreePresenter.ElementTreeNode categoryNode = null;
        if (path != null)
        {
            categoryNode = (TreePresenter.ElementTreeNode) (path.getLastPathComponent());
            if (categoryNode != null && !(categoryNode.element instanceof Category))
            {
                categoryNode = (TreePresenter.ElementTreeNode) categoryNode.getParent();
            }
        }
        return categoryNode != null ? (Category) categoryNode.element : hierarchy.getRoot();
    }

    public void elementChanged(ElementChangeEvent event)
    {
        nodeChanged(nodes.get(event.getElement()));
        if (event.getType() == ElementChangeEvent.EventType.POPULARITY_CHANGED)
        {
            fireCalculationsChanged();
        }
    }

    public void freeze()
    {
        frozen = true;
    }

    public void unfreeze()
    {
        frozen = false;
        for (StructureChangeEvent event : eventQueue)
        {
            structureChanged(event);
        }
        eventQueue.clear();
    }

    public void structureChanged(StructureChangeEvent event)
    {
        if (frozen)
        {
            eventQueue.add(event);
        }
        else
        {
            elementsRemoved(event.getElementsRemoved());
            elementsAdded(event.getElementsAdded());
            fireCalculationsChanged();
        }
    }

    private void elementsAdded(CategorizedElements categorizedElements)
    {
        for (Category category : categorizedElements.getCategories())
        {
            Collection<Element> elements = categorizedElements.getCategoryElements(category);
            ElementTreeNode node = nodes.get(category);
            int[] indexes = new int[elements.size()];
            int index = 0;
            for (Element element : elements)
            {
                indexes[index] = category.getElements().indexOf(element);
                node.insert(buildNode(element), indexes[index]);
                index++;
            }
            Arrays.sort(indexes);
            nodesWereInserted(node, indexes);
        }
    }

    public void elementsRemoved(CategorizedElements categorizedElements)
    {
        for (Category category : categorizedElements.getCategories())
        {
            Collection<Element> elementsList = categorizedElements.getCategoryElements(category);
            ElementTreeNode parentNode = nodes.get(category);
            if (parentNode != null)
            {
                ElementTreeNode[] removedNodes = new ElementTreeNode[elementsList.size()];
                int[] removedIndexes = new int[elementsList.size()];
                int removeIndex = 0;
                for (Element element : elementsList)
                {
                    ElementTreeNode node = nodes.get(element);
                    removedNodes[removeIndex] = node;
                    removedIndexes[removeIndex] = parentNode.getIndex(node);
                    removeIndex++;
                }
                for (ElementTreeNode node : removedNodes)
                {
                    if (parentNode.isNodeChild(node)) parentNode.remove(node);
                }
                Arrays.sort(removedIndexes);
                nodesWereRemoved(parentNode, removedIndexes, removedNodes);
            }
        }
    }

    public void addCalculationListener(CalculationsListener listener)
    {
        if (!calculationsListeners.contains(listener))
        {
            calculationsListeners.add(listener);
        }
    }

    public void removeCalculationListener(CalculationsListener listener)
    {
        calculationsListeners.remove(listener);
    }

    private void fireCalculationsChanged()
    {
        for (CalculationsListener listener : calculationsListeners)
        {
            listener.valuesChanged();
        }
    }

    public String getUserSessionTime()
    {
        return String.format("%.2f", calculator.getUserSessionTime());
    }

    public String getOptimalUserSessionTime()
    {
        return String.format("%.2f", calculator.getOptimalUserSessionTime());
    }

    public static class ElementTreeNode extends DefaultMutableTreeNode
    {
        public final Element element;

        private ElementTreeNode(Element element)
        {
            super(element.getName(), element instanceof Category);
            this.element = element;
        }

        @Override
        public boolean isLeaf()
        {
            return !(element instanceof Category);
        }

        @Override
        public void setUserObject(Object userObject)
        {
            super.setUserObject(userObject);
            element.setName(userObject.toString());
        }

        @Override
        public String toString()
        {
            return element.getName() + " (" + PopularityPresenter.toString(element.getPopularity()) + ")";
        }
    }
}
