package tensors;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class NestedArray<T> implements Indexable<T> {

	protected final int rank;
	protected final List<? extends NestedArray<T>> elements;
	protected String indexName;
	public static boolean zeroIndexed = true;
	private int[] cachedDimensions = null;

	protected NestedArray(int rank, List<? extends NestedArray<T>> elements, String indexName) { // TODO assert validity
		this.rank = rank;
		this.elements = elements;
		this.indexName = indexName;
	}

	protected NestedArray(int rank, List<? extends NestedArray<T>> elements) { // TODO assert validity
		this(rank, elements, null);
	}

	@Override
	public T getAtIndex(int... index) {
		if (index.length != rank)
			throw new IllegalArgumentException("Length of index array " + index.length + " does not match rank  " + rank + " of tensor during indexing.");
		return getAtIndexHelper(index);
	}

	protected T getAtIndexHelper(int[] index) {
		return elements.get(index[index.length - rank]).getAtIndexHelper(index);
	}

	@Override
	public void setAtIndex(T toSet, int... index) {
		if (index.length != rank)
			throw new IllegalArgumentException("Length of index array " + index.length + " does not match rank  " + rank + " of tensor during indexing.");
		setAtIndexHelper(toSet, index);
	}

	protected void setAtIndexHelper(T toSet, int[] index) {
		elements.get(index[index.length - rank]).setAtIndexHelper(toSet, index);
	}

	public int[] getDimensions() {
		if (cachedDimensions != null)
			return cachedDimensions;
		int[] dimensions = new int[rank];
		getDimensionsHelper(dimensions);
		cachedDimensions = dimensions;
		return dimensions;
	}

	protected void getDimensionsHelper(int[] dimensions) {
		dimensions[dimensions.length - rank] = elements.size();
		elements.get(0).getDimensionsHelper(dimensions);
	}

	public boolean matches(NestedArray<T> other) {
		return rank == other.rank && elements.size() == other.elements.size();
	}

	public boolean deepMatches(NestedArray<T> other) {
		return matches(other) && new Zip<>( // TODO test this with opposite upper/lower
				elements.iterator(),
				other.elements.iterator(),
				NestedArray::deepMatches
		).fullReduce(true, (t, r) -> t && r);
	}


	public NestedArray<T> modifyWith(
			IntUnaryOperator rankModifier,
			UnaryOperator<String> indexModifier,
			Function<T, NestedArray<T>> endpointModifier) {
		return new NestedArray<>(
				rankModifier.applyAsInt(rank),
				elements.stream()
						.map(t -> t.modifyWith(rankModifier, indexModifier, endpointModifier))
						.collect(Collectors.toList()),
				indexModifier.apply(indexName)
		);
	}

	public NestedArray<T> modifyWith(
			IntUnaryOperator rankModifier,
			UnaryOperator<String> indexModifier,
			UnaryOperator<T> endpointModifier) {
		return modifyWith(
				rankModifier,
				indexModifier,
				(Function<T, NestedArray<T>>) (e -> new Endpoint<T>(endpointModifier.apply(e)))
		);
	}

	public NestedArray<T> modifyWith(UnaryOperator<T> endpointModifier) {
		return modifyWith(
				i -> i,
				i -> i,
				endpointModifier
		);
	}


	protected static class Endpoint<T> extends DirectedNestedArray<T> {

		private T contained;

		Endpoint(T contained) {
			super(true, 0, Collections.emptyList());
			this.contained = contained;
		}

		@Override
		protected T getAtIndexHelper(int[] index) {
			return contained;
		}

		@Override
		protected void setAtIndexHelper(T toSet, int[] index) {
			contained = toSet;
		}

		@Override
		protected void getDirectionsHelper(boolean[] directions) {
			// Do nothing
		}

//		@Override
//		public boolean matches(NestedArray<T> other) {
//			return other instanceof Endpoint<T>;
//		}

		@Override
		public NestedArray<T> modifyWith(
				IntUnaryOperator rankModifier,
				UnaryOperator<String> indexModifier,
				Function<T, NestedArray<T>> endpointModifier) {
			return endpointModifier.apply(contained);
		}

		@Override
		public DirectedNestedArray<T> modifyWith(
				UnaryOperator<Boolean> upperModifier,
				IntUnaryOperator rankModifier,
				UnaryOperator<String> indexModifier,
				Function<T, DirectedNestedArray<T>> endpointModifier) {
			return endpointModifier.apply(contained);
		}

	}

}
