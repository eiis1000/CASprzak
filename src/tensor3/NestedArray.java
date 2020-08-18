package tensor3;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NestedArray<I extends NestedArrayInterface<I, T>, T> implements NestedArrayInterface<I, T> {

	public static boolean zeroIndexed = true;
	public static boolean assertValidity = true;

	private final int rank;
	protected final List<I> elements;
	private final int[] dimensions;

	public NestedArray(List<I> elements) {
		this.elements = elements;
		if (assertValidity)
			assertValidity();
		this.rank = calculateRank();
		this.dimensions = calculateDimensions();
	}

	private void assertValidity() {
		try {
			Iterator<I> iter = elements.listIterator();
			I check = iter.next();
			while (iter.hasNext())
				if (!check.deepMatches(iter.next()))
					throw new IllegalArgumentException("Elements of NestedArray do not satisfy deepMatches.");
		} catch (NullPointerException e) {
			throw new IllegalStateException("Exception in assertValidity", e);
		}
	}

	private int calculateRank() {
		return elements.get(0).getRank() + 1;
	}

	public int getRank() {
		return rank;
	}

	public int getSize() {
		return elements.size();
	}


	@Override
	public T getAtIndex(int... index) {
//		if (index.length != rank) // TODO should this be commented out?
//			throw new IllegalArgumentException("Length of index array " + index.length + " does not match rank  " + rank + " of tensor during indexing.");
		return elements.get(index[index.length - rank]).getAtIndex(index);
	}


	@Override
	public void setAtIndex(T toSet, int... index) {
//		if (index.length != rank) // TODO should this be commented out?
//			throw new IllegalArgumentException("Length of index array " + index.length + " does not match rank  " + rank + " of tensor during indexing.");
		elements.get(index[index.length - rank]).setAtIndex(toSet, index);
	}


	private int[] calculateDimensions() {
		int[] lowerDimensions = elements.get(0).getDimensions();
		int[] dimensions = new int[lowerDimensions.length + 1];
		dimensions[0] = elements.size();
		System.arraycopy(lowerDimensions, 0, dimensions, 1, lowerDimensions.length);
		return dimensions;
	}

	@Override
	public int[] getDimensions() {
		return dimensions;
	}


	public boolean matches(NestedArrayInterface<I, T> other) {
		return elements.size() == other.getElements().size();
	}


	public List<I> getElements() {
		return elements;
	}

	public class Endpoint implements NestedArrayInterface<I, T> {

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
			return other instanceof Endpoint;
		}

		@Override
		public List<I> getElements() {
			return Collections.emptyList();
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
	}

}
