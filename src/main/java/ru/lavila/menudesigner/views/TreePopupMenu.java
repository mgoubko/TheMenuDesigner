package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TargetTreeController;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.presenters.TreePopupMenuPresenter;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TreePopupMenu extends JPopupMenu implements TreeSelectionListener {
    private final TreePopupMenuPresenter presenter;
    private final TargetTreeController controller;
    private final TreeView treeView;
    private JMenuItem sort;
    private JMenuItem optimizeAll;
    private JMenu classify;
    private JMenu optimize;

    public TreePopupMenu(TreePopupMenuPresenter presenter, TargetTreeController controller, TreeView treeView) {
        super();
        this.presenter = presenter;
        this.controller = controller;
        this.treeView = treeView;
        fillMenu();
        treeView.addTreeSelectionListener(this);
    }

    private void fillMenu() {
        sort = new JMenuItem("Sort by popularity");
        sort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.sortByPriority(getSelectedCategory());
            }
        });
        add(sort);

        optimizeAll = new JMenuItem("Optimize sub-tree");
        optimizeAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                controller.optimizeSubTree(getSelectedCategory());
            }
        });
        add(optimizeAll);

        classify = new JMenu("Classify by...");
        add(classify);

        optimize = new JMenu("Optimize by...");
        add(optimize);

        for (final Hierarchy taxonomy : presenter.getTaxonomies()) {
            JMenuItem classifyItem = new JMenuItem(taxonomy.getName());
            classifyItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    controller.classifyByTaxonomy(taxonomy, getSelectedCategory());
                }
            });
            classify.add(classifyItem);

            JMenuItem optimizeItem = new JMenuItem(taxonomy.getName());
            optimizeItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    controller.optimizeByTaxonomy(getSelectedCategory(), taxonomy);
                }
            });
            optimize.add(optimizeItem);
        }
    }

    private Category getSelectedCategory() {
        List<Element> elements = TreePopupMenu.this.treeView.getSelectedElements();
        if (elements.size() == 1 && elements.get(0) instanceof Category)
            return (Category) elements.get(0);
        return null;
    }

    public void valueChanged(TreeSelectionEvent e) {
        boolean enabled = getSelectedCategory() != null;
        sort.setEnabled(enabled);
        optimizeAll.setEnabled(enabled);
        classify.setEnabled(enabled);
        optimize.setEnabled(enabled);
    }
}
