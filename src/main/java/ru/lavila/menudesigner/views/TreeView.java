package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.models.Choice;
import ru.lavila.menudesigner.presenters.PopularityPresenter;

import javax.swing.*;
import javax.swing.tree.TreeModel;

public class TreeView extends JTree
{
    public TreeView(TreeModel treeModel)
    {
        super(treeModel);
    }

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        return value instanceof Choice ? ((Choice) value).getName() + " (" + new PopularityPresenter(((Choice) value).getPopularity()) + ")" : "";
    }
}
