package functions.unitary.trig;

import functions.Function;
import functions.commutative.Multiply;
import functions.unitary.UnitaryFunction;

public class Sinh extends UnitaryFunction {
	/**
	 * Constructs a new Sinh
	 * @param function The function which sinh is operating on
	 */
	public Sinh(Function function) {
		super(function);
	}

	@Override
	public double evaluate(double... variableValues) {
		return Math.sin(function.evaluate(variableValues));
	}

	@Override
	public Function getDerivative(int varID) {
		return new Multiply(new Cosh(function), function.getSimplifiedDerivative(varID));
	}

	public UnitaryFunction me(Function operand) {
		return new Sinh(operand);
	}

}
