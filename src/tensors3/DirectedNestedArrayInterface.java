package tensors3;

import functions.GeneralFunction;
import tensors3.elementoperations.EIT;
import tensors3.elementoperations.TContainer;

public interface DirectedNestedArrayInterface<I extends DirectedNestedArrayInterface<I, T>, T> extends NestedArrayInterface<I, T> {

	boolean matches(DirectedNestedArrayInterface<I, T> other); // TODO test if deepMatches accounts for direction

	boolean getDirection();

	boolean[] getDirections();

}
