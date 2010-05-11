package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.presenters.TablePresenter;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HierarchyView extends JPanel
{
    private final ToolbarBuilder toolbarBuilder;
    private final ItemsList itemsList;
    private final Hierarchy hierarchy;
    private boolean asTree;
    private JScrollPane scrollPane;
    private TreeView treeView;
    private TableView tableView;

    public HierarchyView(ItemsList itemsList, Hierarchy hierarchy, boolean asTree, ToolbarConfigurator configurator)
    {
        super(new BorderLayout());

        this.itemsList = itemsList;
        this.hierarchy = hierarchy;
        this.asTree = asTree;

        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(400, 500));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.toolbarBuilder = new ToolbarBuilder(configurator, toolbar);
        this.scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(toolbar, BorderLayout.NORTH);
        add(scrollPane);

        treeView = new TreeView(new TreePresenter(hierarchy), new TreeController(hierarchy));
        tableView = new TableView(new TablePresenter(itemsList));

        rebuildView();
    }

    private void rebuildView()
    {
        toolbarBuilder.rebuild();
        scrollPane.setViewportView(asTree ? treeView : tableView);
    }

    public java.util.List<Element> getSelectedElements()
    {
        return asTree ? treeView.getSelectedElements() : tableView.getSelectedElements();
    }

    public Category getSelectedCategory()
    {
        return asTree ? treeView.getSelectedCategory() : hierarchy.getRoot();
    }

    private class ToolbarBuilder implements ToolbarConfig
    {
        protected final ToolbarConfigurator configurator;
        protected final JPanel toolbar;

        public ToolbarBuilder(ToolbarConfigurator configurator, JPanel toolbar)
        {
            this.configurator = configurator;
            this.toolbar = toolbar;
        }

        public JPanel rebuild()
        {
            toolbar.removeAll();
            configurator.fillToolbar(this);
            if (asTree) addTreeButtons();
            toolbar.revalidate();
            toolbar.repaint();
            return toolbar;
        }

        protected void addTreeButtons()
        {
            JButton newCategory = new JButton("New cat.");
            newCategory.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    treeView.addCategory();
                }
            });
            toolbar.add(newCategory);

            JButton remove = new JButton("Remove");
            remove.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    treeView.removeSelection();
                }
            });
            toolbar.add(remove);
        }

        public void addButton(JButton button)
        {
            toolbar.add(button);
        }

        public void addSwitchViewButton()
        {
            JButton switchView = new JButton("Switch");
            switchView.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    asTree = !asTree;
                    rebuildView();
                }
            });
            toolbar.add(switchView);
        }
    }
}
