package tensors;

public class NestedArray<T> implements Indexable<T> {

	protected final int rank;
	protected final NestedArray<T>[] elements;
	public static boolean zeroIndexed = true;
	private int[] cachedDimensions = null;

	protected NestedArray(int rank, NestedArray<T>[] elements) { // TODO assert validity
		this.rank = rank;
		this.elements = elements;
	}

	@Override
	public T getAtIndex(int... index) {
		if (index.length != rank)
			throw new IllegalArgumentException("Length of index array " + index.length + " does not match rank  " + rank + " of tensor during indexing.");
		return getAtIndexHelper(index);
	}

	protected T getAtIndexHelper(int[] index) {
		return elements[index[index.length - rank]].getAtIndexHelper(index);
	}

	@Override
	public void setAtIndex(T toSet, int... index) {
		if (index.length != rank)
			throw new IllegalArgumentException("Length of index array " + index.length + " does not match rank  " + rank + " of tensor during indexing.");
		setAtIndexHelper(toSet, index);
	}

	protected void setAtIndexHelper(T toSet, int[] index) {
		elements[index[index.length - rank]].setAtIndexHelper(toSet, index);
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
		dimensions[dimensions.length - rank] = elements.length;
		elements[0].getDimensionsHelper(dimensions);
	}


	protected static class Endpoint<T> extends DirectedNestedArray<T> {

		private T contained;

		Endpoint(T contained) {
			super(0, null, true);
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

	}

}
