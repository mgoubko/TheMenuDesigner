package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.MenuDesigner;
import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.MenuModelListener;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.presenters.CalculationsListener;
import ru.lavila.menudesigner.presenters.ElementsTransferable;
import ru.lavila.menudesigner.presenters.TreePresenter;
import ru.lavila.menudesigner.views.toolbars.TreeToolBar;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class TreeView extends JPanel implements ItemsView, TreePresenter.ForceSelectionListener, CalculationsListener, MenuModelListener
{
    private final TreeController controller;
    private final TreePresenter presenter;
    private final JTree tree;
    private final JPanel toolBars;
    private final HierarchyCalculator calculator;
    private final CalculationsPanel calculations;
    private TreePresenter.ElementTreeNode activeNode = null;
    private List<Category> sortedCategories = null;

    public TreeView(TreePresenter presenter, TreeController controller, HierarchyCalculator calculator)
    {
        super(new BorderLayout());
        this.presenter = presenter;
        this.controller = controller;
        this.calculator = calculator;
        calculator.addModelListener(this);
        presenter.addCalculationListener(this);
        presenter.addForceSelectionListener(this);
        updateCalculations();

        tree = new JTree(presenter);
        tree.setEditable(true);
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.INSERT);
        tree.setTransferHandler(new TreeTransferHandler());
        tree.setCellRenderer(new HierarchyTreeCellRenderer());
        add(new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        addTreeSelectionListener(new HierarchyTreeSelectionListener());

        toolBars = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addToolBar(new TreeToolBar(this));
        add(toolBars, BorderLayout.NORTH);

        calculations = new CalculationsPanel(calculator);
        presenter.addCalculationListener(calculations);
        add(calculations, BorderLayout.SOUTH);
    }

    public void addTreeSelectionListener(TreeSelectionListener listener)
    {
        tree.addTreeSelectionListener(listener);
    }

    public void setPopupMenu(final JPopupMenu popupMenu)
    {
        //todo: refactor, support several calls
        tree.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void updateCalculations()
    {
        this.sortedCategories = calculator.getCategoriesSortedByQuality();
    }

    public void menuModelChanged()
    {
        updateCalculations();
    }

    public void valuesChanged()
    {
        updateCalculations();
    }

    public void addToolBar(JToolBar toolBar)
    {
        toolBars.add(toolBar, 0);
    }

    public void addCategory()
    {
        TreePath path = tree.getSelectionPath();
        tree.expandPath(path);
        controller.addCategory(presenter.getCategoryFromPath(path));
    }

    public void removeSelection()
    {
        controller.removeNodes(tree.getSelectionPaths());
    }

    public List<Element> getSelectedElements()
    {
        return presenter.getSelectedElements(tree.getSelectionPaths());
    }

    public void setSelection(TreePath[] paths)
    {
        tree.setSelectionPaths(paths);
        if (paths.length > 0) tree.scrollPathToVisible(paths[0]);
    }

    private class TreeTransferHandler extends TransferHandler
    {
        @Override
        public boolean canImport(TransferSupport support)
        {
            return support.isDataFlavorSupported(ElementsTransferable.dataFlavor);
        }

        @Override
        protected Transferable createTransferable(JComponent c)
        {
            presenter.freeze();
            List<Element> elements = getSelectedElements();
            return new ElementsTransferable(elements.toArray(new Element[elements.size()]));
        }

        @Override
        public int getSourceActions(JComponent c)
        {
            return TransferHandler.MOVE;
        }

        @Override
        public boolean importData(TransferSupport support)
        {
            try
            {
                JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
                Category category = presenter.getCategoryFromPath(dropLocation.getPath());
                int index = dropLocation.getChildIndex();
                controller.addElements(category, index, (Element[]) support.getTransferable().getTransferData(ElementsTransferable.dataFlavor));
                return true;
            }
            catch (UnsupportedFlavorException e)
            {
                return false;
            }
            catch (IOException e)
            {
                return false;
            }
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action)
        {
            presenter.unfreeze();
        }
    }

    private class HierarchyTreeSelectionListener implements TreeSelectionListener
    {
        public void valueChanged(TreeSelectionEvent e)
        {
            activeNode = presenter.getActiveNode(tree.getSelectionPaths());
            tree.repaint();
            calculations.showFor(activeNode == null ? null : (Category) activeNode.element);
        }
    }

    private class HierarchyTreeCellRenderer extends DefaultTreeCellRenderer
    {
        private final Border inactiveBorder = BorderFactory.createEmptyBorder();
        private final Border activeBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black);
        private final Icon folderGreen = MenuDesigner.getIcon("folder_green");
        private final Icon folderOrange = MenuDesigner.getIcon("folder_orange");
        private final Icon folderRed = MenuDesigner.getIcon("folder_red");
        private final Icon item = MenuDesigner.getIcon("item");

        private Element element;

        @Override
        public Icon getLeafIcon()
        {
            return item;
        }

        @Override
        public Icon getClosedIcon()
        {
            return element instanceof Category ? getColoredIcon() : super.getClosedIcon();
        }

        @Override
        public Icon getOpenIcon()
        {
            return element instanceof Category ? getColoredIcon() : super.getClosedIcon();
        }

        private Icon getColoredIcon()
        {
            int index = sortedCategories.indexOf(element);
            int zone = sortedCategories.size() / 3;
            if (index < zone)
            {
                return folderGreen;
            }
            else if (index < 2 * zone)
            {
                return folderOrange;
            }
            else
            {
                return folderRed;
            }
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
        {
            element = ((TreePresenter.ElementTreeNode) value).element;
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            setBorder(value.equals(activeNode) ? activeBorder : inactiveBorder);
            return this;
        }
    }
}
