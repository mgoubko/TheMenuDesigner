package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.models.Element;

import javax.swing.*;
import java.util.List;

public interface ItemsView
{
    public List<Element> getSelectedElements();
    public void addToolBar(JToolBar toolBar);
}
