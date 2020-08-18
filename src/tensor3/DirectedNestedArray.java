package tensor3;

import java.util.List;

public class DirectedNestedArray<I extends DirectedNestedArrayInterface<I, T>, T> extends NestedArray<I, T> implements DirectedNestedArrayInterface<I, T> {

	private final boolean isUpper;
	private final boolean[] directions;

	public DirectedNestedArray(boolean isUpper, List<I> elements) {
		super(elements);
		this.isUpper = isUpper;
		directions = calculateDirections();
	}

	@Override
	public boolean matches(DirectedNestedArrayInterface<I, T> other) {
		return isUpper == other.getDirection() && this.matches(other);
	}

	public boolean getDirection() {
		return isUpper;
	}

	private boolean[] calculateDirections() {
		boolean[] lowerDimensions = elements.get(0).getDirections();
		boolean[] dimensions = new boolean[lowerDimensions.length + 1];
		dimensions[0] = isUpper;
		System.arraycopy(lowerDimensions, 0, dimensions, 1, lowerDimensions.length);
		return dimensions;
	}

	@Override
	public boolean[] getDirections() {
		return directions;
	}

	public class Endpoint extends NestedArray<I, T>.Endpoint implements DirectedNestedArrayInterface<I, T> {

		public Endpoint(T contained) {
			super(contained);
		}

		@Override
		public boolean matches(DirectedNestedArrayInterface<I, T> other) {
			return other instanceof Endpoint;
		}

		@Override
		public boolean getDirection() {
			throw new IllegalStateException("Endpoints have no direction."); // TODO deal with this exception
		}

		@Override
		public boolean[] getDirections() {
			return new boolean[0];
		}
	}

}
