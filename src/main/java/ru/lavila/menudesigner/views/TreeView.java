package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.presenters.CalculationsListener;
import ru.lavila.menudesigner.presenters.TreePresenter;
import ru.lavila.menudesigner.views.toolbars.TreeToolBar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
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
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(toolBars, BorderLayout.CENTER);
        topPanel.add(new TreeToolBar(this), BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel calculations = new JPanel(new GridLayout(1, 2));
        calculations.setBorder(new EmptyBorder(10, 10, 10, 10));
        calculations.add(new JLabel("User Session Time"));
        userSessionTime = new JLabel();
        calculations.add(userSessionTime);
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

    public Category getSelectedCategory()
    {
        return presenter.getCategoryFromPath(tree.getSelectionPath());
    }

    public void valuesChanged()
    {
        userSessionTime.setText(presenter.getUserSessionTime());
    }

    private class TreeTransferHandler extends TransferHandler
    {
        private Category category;
        private Element[] elements;
        private int index;

        @Override
        public boolean canImport(TransferSupport support)
        {
            return support.isDataFlavorSupported(ElementsDataFlavor.elementsDataFlavor);
        }

        @Override
        protected Transferable createTransferable(JComponent c)
        {
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
            // Do not import data, just store the request. Actual import will be performed on exportDone
            // as it changes source structure which may break D'n'D
            JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
            this.category = presenter.getCategoryFromPath(dropLocation.getPath());
            this.index = dropLocation.getChildIndex();
            try
            {
                elements = (Element[]) support.getTransferable().getTransferData(ElementsDataFlavor.elementsDataFlavor);
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
            controller.addElements(category, index, elements);
        }
    }

    private static class ElementsTransferable implements Transferable
    {
        private final Element[] elements;

        public ElementsTransferable(Element[] elements)
        {
            this.elements = elements;
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            return new DataFlavor[]{ElementsDataFlavor.elementsDataFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return flavor.equals(ElementsDataFlavor.elementsDataFlavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
            if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
            return elements;
        }
    }

    private static class ElementsDataFlavor extends DataFlavor
    {
        public static DataFlavor elementsDataFlavor = buildElementsDataFlavor();

        private ElementsDataFlavor() throws ClassNotFoundException
        {
            super(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + Element[].class.getName() + "\"");
        }

        private static DataFlavor buildElementsDataFlavor()
        {
            try
            {
                return new ElementsDataFlavor();
            }
            catch (ClassNotFoundException e)
            {
                return null;
            }
        }
    }
}
