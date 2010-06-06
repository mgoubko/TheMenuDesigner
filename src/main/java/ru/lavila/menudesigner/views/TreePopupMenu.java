package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TargetTreeController;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.presenters.TreePopupMenuPresenter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TreePopupMenu extends JPopupMenu
{
    private final TreePopupMenuPresenter presenter;
    private final TargetTreeController controller;
    private final TreeView treeView;

    public TreePopupMenu(TreePopupMenuPresenter presenter, TargetTreeController controller, TreeView treeView)
    {
        super();
        this.presenter = presenter;
        this.controller = controller;
        this.treeView = treeView;
        for (final Hierarchy taxonomy : presenter.getTaxonomies())
        {
            JMenuItem menuItem = new JMenuItem(taxonomy.getName());
            menuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    List<Element> elements = TreePopupMenu.this.treeView.getSelectedElements();
                    if (elements.size() == 1 && elements.get(0) instanceof Category)
                    {
                        TreePopupMenu.this.controller.classifyByTaxonomy(taxonomy, (Category) elements.get(0));
                    }
                }
            });
            add(menuItem);
        }
    }
}
