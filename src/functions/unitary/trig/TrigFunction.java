package functions.unitary.trig;

import functions.Function;
import functions.unitary.UnitaryFunction;

public abstract class TrigFunction extends UnitaryFunction {

	public TrigFunction(Function operand) {
		super(operand);
	}

	/**
	 * Returns the Class corresponding to the inverse of this trig function
	 * @return the inverse class
	 */
	public abstract Class<? extends TrigFunction> getInverse();
}