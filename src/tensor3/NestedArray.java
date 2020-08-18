package tensor3;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class NestedArray<I extends NestedArrayInterface<I, T>, T> implements NestedArrayInterface<I, T> {

	public static boolean zeroIndexed = true;
	public static boolean assertValidity = true;

	private final int rank;
	protected final List<I> elements;
	private final int[] dimensions;


	@SuppressWarnings({"unchecked"})
	public static <I extends NestedArrayInterface<I, T>, T> NestedArray<I, T> newNestedArray(Object[] elements) {
		if (elements[0] instanceof Object[])
			return new NestedArray<>(Arrays.stream(elements)
					.map(e -> (I) newNestedArray((Object[]) e))
					.collect(Collectors.toList()));
		else
			return new NestedArray<>(Arrays.stream(elements)
					.map(e -> (I) new NestedArray<I, T>().new Endpoint((T) e))
					.collect(Collectors.toList())
			);
	}

	private NestedArray() {
		elements = null;
		rank = -1;
		dimensions = null;
	}

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


	public NestedArrayInterface<I, T> modifyWith(UnaryOperator<I> elementModifier,
												 UnaryOperator<T> endpointModifier) {
		return new NestedArray<>(elements.stream()
				.map(elementModifier)
				.collect(Collectors.toList()));
	}


	public String toString() {
		return elements.toString();
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
		public NestedArrayInterface<I, T> modifyWith(UnaryOperator<I> elementModifier, UnaryOperator<T> endpointModifier) {
			return new Endpoint(endpointModifier.apply(contained));
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
