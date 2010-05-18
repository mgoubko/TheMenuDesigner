package ru.lavila.menudesigner.math;

public class ReadUntilMenuModel implements MenuModel
{
    private final double tResp;
    private final double tLoad;
    private final double tRead;
    private final double tClick;

    public ReadUntilMenuModel(double tResp, double tLoad, double tRead, double tClick)
    {
        this.tResp = tResp;
        this.tLoad = tLoad;
        this.tRead = tRead;
        this.tClick = tClick;
    }

    public double getTimeToSelect(int target, int total)
    {
        return tResp + tLoad * total + tRead * target + tClick;
    }
}
