package functions.unitary;

import functions.Function;

public class Csch extends UnitaryFunction{
    public Csch(Function function) {
        super(function);
    }

    @Override
    public UnitaryFunction me(Function function) {
        return null;
    }

    @Override
    public Function getDerivative(int varID) {
        return null;
    }

    @Override
    public double evaluate(double... variableValues) {
        return 0;
    }
}
