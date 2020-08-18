package tensor3;

import java.util.List;
import java.util.function.UnaryOperator;

public interface NestedArrayInterface<I extends NestedArrayInterface<I, T>, T> extends Indexable<T> {

	int getRank();

	boolean matches(NestedArrayInterface<I, T> other);

	default boolean deepMatches(NestedArrayInterface<I, T> other) {
		return matches(other) && new Zip<>( // TODO test this with opposite upper/lower
				getElements().iterator(),
				other.getElements().iterator(),
				NestedArrayInterface::deepMatches
		).fullReduce(true, (t, r) -> t && r);
	}

	List<I> getElements();

	NestedArrayInterface<I, T> modifyWith(UnaryOperator<I> elementModifier,
										  UnaryOperator<T> endpointModifier);

	String toString();

}
