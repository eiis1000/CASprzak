package tensors;

public class DirectedNestedArray<T> extends NestedArray<T> {

	protected final boolean isUpper;
	protected final DirectedNestedArray<T>[] directedElements;

	protected DirectedNestedArray(int rank, DirectedNestedArray<T>[] elements, boolean isUpper) { // TODO assert validity
		super(rank, elements);
		this.isUpper = isUpper;
		this.directedElements = elements;
	}

	public boolean[] getDirections() {
		boolean[] directions = new boolean[rank];
		getDirectionsHelper(directions);
		return directions;
	}

	protected void getDirectionsHelper(boolean[] directions) {
		directions[directions.length - rank] = isUpper;
		directedElements[0].getDirectionsHelper(directions);
	}

}
