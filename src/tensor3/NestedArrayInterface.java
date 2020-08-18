package tensor3;

import java.util.Collections;
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

	default NestedArrayInterface<I, T> modifyWith(UnaryOperator<T> endpointModifier) {
		return modifyWith(i -> i, endpointModifier);
	}

	String toString();


	class Endpoint<I extends NestedArrayInterface<I, T>, T> implements NestedArrayInterface<I, T> {

		protected T contained;

		public Endpoint(T contained) {
			this.contained = contained;
		}

		@Override
		public int getRank() {
			return 0;
		}

		@Override
		public boolean matches(NestedArrayInterface<I, T> other) {
			return other instanceof NestedArray.Endpoint;
		}

		@Override
		public List<I> getElements() {
			return Collections.emptyList();
		}

		@Override
		public NestedArrayInterface<I, T> modifyWith(UnaryOperator<I> elementModifier, UnaryOperator<T> endpointModifier) {
			return new Endpoint<>(endpointModifier.apply(contained));
		}

		@Override
		public T getAtIndex(int... index) {
			return contained;
		}

		@Override
		public void setAtIndex(T toSet, int... index) {
			contained = toSet;
		}

		@Override
		public int[] getDimensions() {
			return new int[0];
		}

		@Override
		public String toString() {
			return contained.toString();
		}

	}


}
