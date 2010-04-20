package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.presenters.TablePresenter;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HierarchyView extends JPanel
{
    private final Hierarchy hierarchy;
    private boolean asTree;
    private JScrollPane scrollPane;
    private JButton switchView;
    private TreeView treeView;
    private TableView tableView;

    public HierarchyView(Hierarchy hierarchy, boolean asTree)
    {
        super(new BorderLayout());
        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(400, 500));

        this.hierarchy = hierarchy;
        this.asTree = asTree;

        buildGUI();
    }

    private void buildGUI()
    {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(toolbar, BorderLayout.NORTH);

        switchView = new JButton();
        switchView.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                asTree = !asTree;
                rebuildView();
            }
        });
        toolbar.add(switchView);

        JButton newCategory = new JButton("New category");
        newCategory.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                treeView.addCategory();
            }
        });
        toolbar.add(newCategory);

        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        treeView = new TreeView(new TreePresenter(hierarchy));
        tableView = new TableView(new TablePresenter(hierarchy));

        rebuildView();
    }

    private void rebuildView()
    {
        scrollPane.setViewportView(asTree ? treeView : tableView);
        switchView.setText(asTree ? "Table view" : "Tree view");
    }
}
