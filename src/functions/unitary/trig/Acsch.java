package functions.unitary.trig;

import functions.Function;
import functions.binary.Pow;
import functions.commutative.Sum;
import functions.commutative.Product;
import functions.special.Constant;
import functions.unitary.UnitaryFunction;

import java.util.Map;


public class Acsch extends UnitaryFunction {
	/**
	 * Constructs a new Acsch
	 * @param function The function which arccsch is operating on
	 */
	public Acsch(Function function) {
		super(function);
	}

	@Override
	public Function getDerivative(char varID) {
		return new Product(Constant.NEGATIVE_ONE, operand.getSimplifiedDerivative(varID), new Pow(Constant.NEGATIVE_ONE, new Product(operand, new Pow(Constant.HALF, new Sum(Constant.ONE, new Product(Constant.NEGATIVE_ONE, new Pow(Constant.TWO, operand)))))));
	}

	/**
	 * Returns the inverse hyperbolic cosecant of the stored {@link #operand} evaluated
	 * @param variableValues The values of the variables of the {@link Function} at the point
	 * @return the arccsch of {@link #operand} evaluated
	 */
	@Override
	public double evaluate(Map<Character, Double> variableValues) {
		double functionEvaluated = operand.evaluate(variableValues);
		return Math.log((1 + Math.sqrt(functionEvaluated * functionEvaluated + 1)) / functionEvaluated);
	}

	@Override
	public UnitaryFunction me(Function operand) {
		return new Acsch(operand);
	}
}
