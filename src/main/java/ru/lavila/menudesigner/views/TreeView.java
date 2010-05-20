package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.presenters.CalculationsListener;
import ru.lavila.menudesigner.presenters.ElementsTransferable;
import ru.lavila.menudesigner.presenters.TreePresenter;
import ru.lavila.menudesigner.views.toolbars.TreeToolBar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

public class TreeView extends JPanel implements ItemsView, CalculationsListener
{
    private final TreeController controller;
    private final TreePresenter presenter;
    private final JTree tree;
    private final JPanel toolBars;
    private final JLabel userSessionTime;
    private final JLabel optimalSessionTime;

    public TreeView(TreePresenter presenter, TreeController controller)
    {
        super(new BorderLayout());
        this.presenter = presenter;
        this.controller = controller;

        tree = new JTree(presenter);
        tree.setEditable(true);
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.INSERT);
        tree.setTransferHandler(new TreeTransferHandler());
        add(new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        toolBars = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addToolBar(new TreeToolBar(this));
        add(toolBars, BorderLayout.NORTH);

        JPanel calculations = new JPanel(new GridLayout(2, 2));
        calculations.setBorder(new EmptyBorder(10, 10, 10, 10));
        calculations.add(new JLabel("User Session Time"));
        userSessionTime = new JLabel();
        calculations.add(userSessionTime);
        calculations.add(new JLabel("Optimal Time"));
        optimalSessionTime = new JLabel();
        calculations.add(optimalSessionTime);

        add(calculations, BorderLayout.SOUTH);

        valuesChanged();
        presenter.addCalculationListener(this);
    }

    public void addToolBar(JToolBar toolBar)
    {
        toolBars.add(toolBar);
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

    public void valuesChanged()
    {
        userSessionTime.setText(presenter.getUserSessionTime());
        optimalSessionTime.setText(presenter.getOptimalUserSessionTime());
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
}
