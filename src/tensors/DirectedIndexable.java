package tensors;

public class DirectedIndexable<T> implements Indexable<T> {

	protected final int rank;
	protected final DirectedIndexable<T>[] elements;
	public static boolean zeroIndexed = true;

	protected DirectedIndexable(int rank, DirectedIndexable<T>[] elements) {
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
		int[] dimensions = new int[rank];
		getDimensionsHelper(dimensions);
		return dimensions;
	}

	protected void getDimensionsHelper(int[] dimensions) {
		dimensions[dimensions.length - rank] = elements.length;
		elements[0].getDimensionsHelper(dimensions);
	}


	private static class Endpoint<T> extends DirectedIndexable<T> {

		private T contained;

		Endpoint(T contained) {
			super(0, null);
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

	}

}
