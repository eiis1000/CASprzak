package tensor3;

public interface DirectedNestedArrayInterface<I extends DirectedNestedArrayInterface<I, T>, T> extends NestedArrayInterface<I, T> {

	boolean matches(DirectedNestedArrayInterface<I, T> other); // TODO test if deepMatches accounts for direction

	boolean getDirection();

	boolean[] getDirections();


}
