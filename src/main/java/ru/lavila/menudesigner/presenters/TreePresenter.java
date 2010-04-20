package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.*;

import javax.swing.tree.*;

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

    public void addCategory(Object parent)
    {
        ElementTreeNode parentNode = (ElementTreeNode) (parent == null ? getRoot() : parent);
        if (parentNode.element instanceof Category)
        {
            ((Category) parentNode.element).add(new CategoryImpl("New category"));
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
            
            for (Element child : children)
            {
                ElementTreeNode childNode = new ElementTreeNode(child);
                insertNodeInto(childNode, this, parent.getElements().indexOf(child));
            }
        }
    }
}
