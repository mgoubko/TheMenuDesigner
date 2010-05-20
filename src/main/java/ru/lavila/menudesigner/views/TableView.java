package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.presenters.ElementsTransferable;
import ru.lavila.menudesigner.presenters.TablePresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.util.List;

public class TableView extends JPanel implements ItemsView
{
    private final TablePresenter presenter;
    private final JTable table;
    private final JPanel toolBars;

    public TableView(TablePresenter presenter)
    {
        super(new BorderLayout());
        this.presenter = presenter;

        table = new JTable(presenter);
        table.setDragEnabled(true);
        table.setTransferHandler(new TableTransferHandler());
        add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        toolBars = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(toolBars, BorderLayout.NORTH);
    }

    public void addToolBar(JToolBar toolBar)
    {
        toolBars.add(toolBar);
    }

    public List<Element> getSelectedElements()
    {
        return presenter.getSelectedItems(table.getSelectedRows());
    }

    private class TableTransferHandler extends TransferHandler
    {
        @Override
        public boolean canImport(TransferSupport support)
        {
            return false;
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
            return false;
        }
    }

}
