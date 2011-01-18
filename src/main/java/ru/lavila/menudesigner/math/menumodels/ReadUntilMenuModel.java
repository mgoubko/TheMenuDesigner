package ru.lavila.menudesigner.math.menumodels;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import ru.lavila.menudesigner.utils.TheLogger;

public class ReadUntilMenuModel extends AnonymousMenuModel
{
    private final int MAX_SIZE = 100;

    protected double tResp;
    protected double tLoad;
    protected double tRead;
    protected double tClick;
    protected double[] optimalProportion = null;

    public ReadUntilMenuModel(double tResp, double tLoad, double tRead, double tClick)
    {
        this.tResp = tResp;
        this.tLoad = tLoad;
        this.tRead = tRead;
        this.tClick = tClick;
    }

    public String getName()
    {
        return "Self-terminating serial search";
    }

    public double getTimeToSelect(int target, int total)
    {
        return tResp + tLoad * total + tRead * target + tClick;
    }

    protected double getModelFactor(int total)
    {
        return (tResp + tLoad * total + tClick) / tRead;
    }

    public double[] getOptimalProportion()
    {
        if (optimalProportion == null)
        {
            optimalProportion = calculateOptimalProportion();
        }
        return optimalProportion;
    }

    private double[] calculateOptimalProportion()
    {
        try
        {
            UnivariateRealSolver solver = UnivariateRealSolverFactory.newInstance().newDefaultSolver();
            ProportionFinderFunction bestFunction = null;
            double bestBase = 0;
            double minResult = 0;
            //todo: make calculations less straightforward
            for (int size = 2; size <= MAX_SIZE; size++)
            {
                ProportionFinderFunction function = new ProportionFinderFunction(size, getModelFactor(size));
                double base = solver.solve(function, 0, 1, 0.1);
                double result = -1 / Math.log(base);
                if (bestFunction == null || result < minResult)
                {
                    bestFunction = function;
                    bestBase = base;
                    minResult = result;
                }
            }
            return bestFunction.proportion(bestBase);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error solving equation", e);
        }
    }

    private static class ProportionFinderFunction implements UnivariateRealFunction
    {
        private final int size;
        private final double modelFactor;

        public ProportionFinderFunction(int size, double modelFactor)
        {
            this.size = size;
            this.modelFactor = modelFactor;
        }

        private double element(double base, int index)
        {
            return Math.pow(base, modelFactor + index + 1);
        }

        private double[] proportion(double base)
        {
            double[] proportion = new double[size];
            for (int index = 0; index < size; index++)
            {
                proportion[index] = element(base, index);
            }
            return proportion;
        }

        public double value(double base) throws FunctionEvaluationException
        {
            double value = -1;
            for (int index = 0; index < size; index++)
            {
                value += element(base, index);
            }
            return value;
        }
    }
}
