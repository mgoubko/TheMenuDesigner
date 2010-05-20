package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.Element;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ElementsTransferable implements Transferable
{
    public static DataFlavor dataFlavor = DataFlavor.build();

    private final Element[] elements;

    public ElementsTransferable(Element[] elements)
    {
        this.elements = elements;
    }

    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors()
    {
        return new java.awt.datatransfer.DataFlavor[]{dataFlavor};
    }

    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor)
    {
        return flavor.equals(dataFlavor);
    }

    public Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
        return elements;
    }

    private static class DataFlavor extends java.awt.datatransfer.DataFlavor
    {
        private DataFlavor() throws ClassNotFoundException
        {
            super(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + Element[].class.getName() + "\"");
        }

        private static DataFlavor build()
        {
            try
            {
                return new DataFlavor();
            }
            catch (ClassNotFoundException e)
            {
                return null;
            }
        }
    }
}
