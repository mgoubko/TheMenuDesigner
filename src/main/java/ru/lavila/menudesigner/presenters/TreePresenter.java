package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.HierarchyListener;
import ru.lavila.menudesigner.models.events.StructureChangeEvent;

import javax.swing.tree.*;
import java.util.*;

public class TreePresenter extends DefaultTreeModel implements HierarchyListener
{
    private final Hierarchy hierarchy;
    private final Map<Element, ElementTreeNode> nodes;

    public TreePresenter(Hierarchy hierarchy)
    {
        super(null);
        this.hierarchy = hierarchy;
        nodes = new HashMap<Element, ElementTreeNode>();
        setRoot(getTreeNode(hierarchy.root));
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

    public Category getSelectedCategory(TreePath selectionPath)
    {
        TreePresenter.ElementTreeNode categoryNode = null;
        if (selectionPath != null)
        {
            categoryNode = (TreePresenter.ElementTreeNode) (selectionPath.getLastPathComponent());
            if (categoryNode != null && !(categoryNode.element instanceof Category))
            {
                categoryNode = (TreePresenter.ElementTreeNode) categoryNode.getParent();
            }
        }
        return categoryNode != null ? (Category) categoryNode.element : hierarchy.root;
    }

    public void modelChanged(ElementChangeEvent event)
    {
    }

    public void structureChanged(StructureChangeEvent event)
    {
        switch (event.getType())
        {
            case ELEMENTS_ADDED:
                elementsAdded(event.getCategorizedElements());
                break;
            case ELEMENTS_REMOVED:
                elementsRemoved(event.getCategorizedElements());
                break;
        }
    }

    private void elementsAdded(CategorizedElements categorizedElements)
    {
        for (Category category : categorizedElements.getCategories())
        {
            Collection<Element> elements = categorizedElements.getElementsFor(category);
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
            Collection<Element> elementsList = categorizedElements.getElementsFor(category);
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
                    parentNode.remove(node);
                }
                Arrays.sort(removedIndexes);
                nodesWereRemoved(parentNode, removedIndexes, removedNodes);
            }
        }
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
