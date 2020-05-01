package functions.unitary.trig;

import functions.Function;
import functions.commutative.Product;
import functions.special.Constant;
import functions.unitary.UnitaryFunction;

import java.util.Map;


public class Csch extends TrigFunction {
	/**
	 * Constructs a new Csch
	 * @param function The function which csch is operating on
	 */
	public Csch(Function function) {
		super(function);
	}

	@Override
	public Function getDerivative(char varID) {
		return new Product(new Constant(-1), operand.getSimplifiedDerivative(varID), new Csch(operand), new Coth(operand));
	}

	/**
	 * Returns the hyperbolic cosecant of the stored {@link #operand} evaluated
	 * @param variableValues The values of the variables of the {@link Function} at the point
	 * @return the csch of {@link #operand} evaluated
	 */
	@Override
	public double evaluate(Map<Character, Double> variableValues) {
		return 1 / Math.sinh(operand.evaluate(variableValues));
	}

	@Override
	public UnitaryFunction me(Function operand) {
		return new Csch(operand);
	}

	@Override
	public Function integrate() {
		return null;
	}
}
