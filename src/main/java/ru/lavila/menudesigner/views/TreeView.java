package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.presenters.CalculationsListener;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;

public class TreeView extends JPanel implements ItemsView, CalculationsListener
{
    private final TreeController controller;
    private final TreePresenter presenter;
    private final JTree tree;
    private final JLabel userSessionTime;

    public TreeView(TreePresenter presenter, TreeController controller)
    {
        super(new BorderLayout());
        this.presenter = presenter;
        this.controller = controller;

        tree = new JTree(presenter);
        tree.setEditable(true);
        add(new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        JPanel calculations = new JPanel(new GridLayout(1, 2));
        calculations.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10));
        calculations.add(new JLabel("User Session Time"));
        userSessionTime = new JLabel();
        calculations.add(userSessionTime);
        add(calculations, BorderLayout.SOUTH);

        valuesChanged();
        presenter.addCalculationListener(this);
    }

    public void addCategory()
    {
        TreePath path = tree.getSelectionPath();
        tree.expandPath(path);
        controller.addCategory(presenter.getSelectedCategory(path));
    }

    public void removeSelection()
    {
        controller.removeNodes(tree.getSelectionPaths());
    }

    public List<Element> getSelectedElements()
    {
        return presenter.getSelectedElements(tree.getSelectionPaths());
    }

    public Category getSelectedCategory()
    {
        return presenter.getSelectedCategory(tree.getSelectionPath());
    }

    public void valuesChanged()
    {
        userSessionTime.setText(presenter.getUserSessionTime());
    }
}
