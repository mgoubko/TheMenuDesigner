package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.*;

import javax.swing.tree.*;
import java.util.*;

public class TreePresenter extends DefaultTreeModel
{
    public TreePresenter(Hierarchy hierarchy)
    {
        super(null);
        setRoot(getTreeNode(hierarchy.root));
    }

    private MutableTreeNode getTreeNode(Element element)
    {
        if (element instanceof Category)
        {
            DefaultMutableTreeNode node = new ElementTreeNode(element);
            for (Element child : ((Category) element).getElements())
            {
                node.add(getTreeNode(child));
            }
            return node;
        }
        else
        {
            return new ElementTreeNode(element);
        }
    }

    public void addCategory(TreePath parentPath)
    {
        ElementTreeNode parentNode = (ElementTreeNode) (parentPath == null ? getRoot() : parentPath.getLastPathComponent());
        if (parentNode.element instanceof Category)
        {
            ((Category) parentNode.element).add(new CategoryImpl("New category"));
        }
    }

    public void removeNodes(TreePath[] paths)
    {
        if (paths == null) return;
        Map<Category, Collection<Element>> toRemove = new HashMap<Category, Collection<Element>>();
        for (TreePath path : paths)
        {
            ElementTreeNode childNode = (ElementTreeNode) path.getLastPathComponent();
            Category parent = (Category) ((ElementTreeNode) childNode.getParent()).element;
            Collection<Element> parentElements = toRemove.get(parent);
            if (parentElements == null)
            {
                parentElements = new HashSet<Element>();
                toRemove.put(parent, parentElements);
            }
            parentElements.add(childNode.element);
        }
        for (Category category : toRemove.keySet())
        {
            Collection<Element> elements = toRemove.get(category);
            category.remove(elements.toArray(new Element[elements.size()]));
        }
    }

    private class ElementTreeNode extends DefaultMutableTreeNode implements ElementListener
    {
        public final Element element;

        private ElementTreeNode(Element element)
        {
            super(element.getName(), element instanceof Category);
            this.element = element;
            element.addModelListener(this);
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

        public void elementsAdded(Category parent, Element... children)
        {
            if (parent != element) return;

            int[] indexes = new int[children.length];
            for (int i = 0; i < children.length; i++)
            {
                indexes[i] = parent.getElements().indexOf(children[i]);
                this.insert(new ElementTreeNode(children[i]), indexes[i]);
            }
            nodesWereInserted(this, indexes);
        }

        public void elementsRemoved(Category parent, Element... elements)
        {
            if (parent != element) return;

            Collection<Element> elementsList = Arrays.asList(elements);
            ElementTreeNode[] removedNodes = new ElementTreeNode[elements.length];
            int[] removedIndexes = new int[elements.length];
            int removeIndex = 0;
            for (int i = 0; i < getChildCount(); i++)
            {
                ElementTreeNode childNode = (ElementTreeNode) getChildAt(i);
                if (elementsList.contains(childNode.element))
                {
                    removedNodes[removeIndex] = childNode;
                    removedIndexes[removeIndex] = i;
                    removeIndex++;
                }
            }
            for (ElementTreeNode node : removedNodes)
            {
                remove(node);
            }
            nodesWereRemoved(this, removedIndexes, removedNodes);
        }
    }
}
