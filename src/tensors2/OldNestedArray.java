package tensors2;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class OldNestedArray<T> implements OldIndexable<T> {

	protected final int rank;
	protected final List<? extends OldNestedArray<T>> elements;
	protected String indexName;
	public static boolean zeroIndexed = true;
	private int[] cachedDimensions = null;

	protected OldNestedArray(int rank, List<? extends OldNestedArray<T>> elements, String indexName) { // TODO assert validity
		this.rank = rank;
		this.elements = elements;
		this.indexName = indexName;
	}

	protected OldNestedArray(int rank, List<? extends OldNestedArray<T>> elements) { // TODO assert validity
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

	public boolean matches(OldNestedArray<T> other, boolean checkIndices) {
		return rank == other.rank && elements.size() == other.elements.size() && (!checkIndices || indexName.equals(other.indexName));
	}

	public boolean deepMatches(OldNestedArray<T> other, boolean checkIndices) {
		return matches(other, checkIndices) && new OldZip<>( // TODO test this with opposite upper/lower
				elements.iterator(),
				other.elements.iterator(),
				(a, b) -> a.deepMatches(b, checkIndices)
		).fullReduce(true, (t, r) -> t && r);
	}


	public OldNestedArray<T> modifyWith(
			IntUnaryOperator rankModifier,
			UnaryOperator<String> indexModifier,
			Function<T, OldNestedArray<T>> endpointModifier) {
		return new OldNestedArray<>(
				rankModifier.applyAsInt(rank),
				elements.stream()
						.map(t -> t.modifyWith(rankModifier, indexModifier, endpointModifier))
						.collect(Collectors.toList()),
				indexModifier.apply(indexName)
		);
	}

	public OldNestedArray<T> modifyWith(
			IntUnaryOperator rankModifier,
			UnaryOperator<String> indexModifier,
			UnaryOperator<T> endpointModifier) {
		return modifyWith(
				rankModifier,
				indexModifier,
				(Function<T, OldNestedArray<T>>) (e -> new Endpoint<T>(endpointModifier.apply(e)))
		);
	}

	public OldNestedArray<T> modifyWith(UnaryOperator<T> endpointModifier) {
		return modifyWith(
				i -> i,
				i -> i,
				endpointModifier
		);
	}


	protected static class Endpoint<T> extends OldDirectedNestedArray<T> {

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
//		public boolean matches(OldNestedArray<T> other) {
//			return other instanceof Endpoint<T>;
//		}

		@Override
		public OldNestedArray<T> modifyWith(
				IntUnaryOperator rankModifier,
				UnaryOperator<String> indexModifier,
				Function<T, OldNestedArray<T>> endpointModifier) {
			return endpointModifier.apply(contained);
		}

		@Override
		public OldDirectedNestedArray<T> modifyWith(
				UnaryOperator<Boolean> upperModifier,
				IntUnaryOperator rankModifier,
				UnaryOperator<String> indexModifier,
				Function<T, OldDirectedNestedArray<T>> endpointModifier) {
			return endpointModifier.apply(contained);
		}

	}

}
