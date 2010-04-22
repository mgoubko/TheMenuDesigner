package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;

import java.util.List;

public class StructureChangeEventImpl implements StructureChangeEvent
{
    private final Category element;
    private final EventType type;
    private final List<Element> diff;

    public StructureChangeEventImpl(Category category, EventType type, List<Element> diff)
    {
        this.element = category;
        this.type = type;
        this.diff = diff;
    }

    public List<Element> getDiff()
    {
        return diff;
    }

    public Category getElement()
    {
        return element;
    }

    public EventType getType()
    {
        return type;
    }

    public List<Element> getOldValue()
    {
        return null;
    }

    public List<Element> getNewValue()
    {
        return null;
    }
}
