package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;

import java.util.List;

public interface StructureChangeEvent extends ElementChangeEvent<List<Element>>
{
    Category getElement();
    List<Element> getDiff();
}
