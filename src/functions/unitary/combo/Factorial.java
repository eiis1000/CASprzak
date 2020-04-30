package functions.unitary.combo;

import functions.Function;
import functions.unitary.UnitaryFunction;

public abstract class Factorial extends UnitaryFunction {
	/**
	 * Constructs a new UnitaryFunction
	 * @param function The {@link Function} which will be operated on
	 */
	public Factorial(Function function) {
		super(function);
	}

	/**
	 * Returns the specific approximation used for this factorial in the form of a {@link Function}
	 * @return a {@link Function} representing the specific approximation
	 */
	public abstract Function classForm();
}
