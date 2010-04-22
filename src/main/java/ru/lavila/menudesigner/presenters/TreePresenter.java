package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.events.HierarchyListener;

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

    public void elementsAdded(Category parent, Element... children)
    {
        ElementTreeNode node = nodes.get(parent);
        int[] indexes = new int[children.length];
        for (int i = 0; i < children.length; i++)
        {
            indexes[i] = parent.getElements().indexOf(children[i]);
            node.insert(buildNode(children[i]), indexes[i]);
        }
        nodesWereInserted(node, indexes);
    }

    public void elementsRemoved(Map<Category, Collection<Element>> elementsMap)
    {
        for (Category category : elementsMap.keySet())
        {
            Collection<Element> elementsList = elementsMap.get(category);
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
            return element.getName();
        }
    }
}
