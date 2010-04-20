package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Choice;
import ru.lavila.menudesigner.models.Hierarchy;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

public class ItemsTreePresenter extends DefaultTreeModel
{
    public ItemsTreePresenter(Hierarchy hierarchy)
    {
        super(getTreeNode(hierarchy.root));
    }

    private static MutableTreeNode getTreeNode(Choice choice)
    {
        if (choice instanceof Category)
        {
            DefaultMutableTreeNode node = new ChoiceTreeNode(choice, true);
            for (Choice child : ((Category) choice).getChoices())
            {
                node.add(getTreeNode(child));
            }
            return node;
        }
        else
        {
            return new ChoiceTreeNode(choice, false);
        }
    }

    private static class ChoiceTreeNode extends DefaultMutableTreeNode
    {
        private final Choice choice;

        private ChoiceTreeNode(Choice choice, boolean allowsChildren)
        {
            super(choice.getName(), allowsChildren);
            this.choice = choice;
        }

        @Override
        public void setUserObject(Object userObject)
        {
            super.setUserObject(userObject);
            choice.setName(userObject.toString());
        }

        @Override
        public String toString()
        {
            return choice.getName();
        }
    }
}
