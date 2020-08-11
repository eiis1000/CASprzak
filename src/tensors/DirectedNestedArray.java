package tensors;

public class DirectedNestedArray<T> extends NestedArray<T> {

	protected final boolean isUpper;
	protected final DirectedNestedArray<T>[] directedElements;
	private boolean[] cachedDirections = null;

	protected DirectedNestedArray(int rank, DirectedNestedArray<T>[] elements, boolean isUpper) { // TODO assert validity
		super(rank, elements);
		this.isUpper = isUpper;
		this.directedElements = elements;
	}

	public boolean[] getDirections() {
		if (cachedDirections != null)
			return cachedDirections;
		boolean[] directions = new boolean[rank];
		getDirectionsHelper(directions);
		cachedDirections = directions;
		return directions;
	}

	protected void getDirectionsHelper(boolean[] directions) {
		directions[directions.length - rank] = isUpper;
		directedElements[0].getDirectionsHelper(directions);
	}

}
