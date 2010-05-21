package ru.lavila.menudesigner.math;

public class ReadUntilWithErrorMenuModel extends ReadUntilMenuModel
{
    protected final double errorProbability;

    public ReadUntilWithErrorMenuModel(double tResp, double tLoad, double tRead, double tClick, double errorProbability)
    {
        super(tResp, tLoad, tRead, tClick);
        this.errorProbability = errorProbability;
    }

    @Override
    public double getTimeToSelect(int target, int total)
    {
        return (1 + 2 * errorProbability) * (tResp + tClick + tLoad * total) + errorProbability * tRead * total + (1 + errorProbability) * tRead * target;
    }

    @Override
    public double[] getOptimalProportion()
    {
        return new double[]{0.32958626358552495, 0.22630834746066913, 0.15539321200226222, 0.10669977757040802, 0.07326473522800214, 0.050306772424968345, 0.034542830789488506, 0.023718618814810317};
    }
}
