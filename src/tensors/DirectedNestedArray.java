package tensors;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class DirectedNestedArray<T> extends NestedArray<T> {

	public final boolean isUpper;
	protected final List<? extends DirectedNestedArray<T>> directedElements;
	private boolean[] cachedDirections = null;

	protected DirectedNestedArray(boolean isUpper, int rank, List<? extends DirectedNestedArray<T>> elements, String indexName) { // TODO assert validity
		super(rank, elements, indexName);
		this.isUpper = isUpper;
		this.directedElements = elements;
	}

	protected DirectedNestedArray(boolean isUpper, int rank, List<? extends DirectedNestedArray<T>> elements) {
		this(isUpper, rank, elements, null);
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
		directedElements.get(0).getDirectionsHelper(directions);
	}

	public boolean matches(DirectedNestedArray<T> other) {
		return super.matches(other) && isUpper == other.isUpper;
	}


	public DirectedNestedArray<T> modifyWith(
			UnaryOperator<Boolean> upperModifier,
			IntUnaryOperator rankModifier,
			UnaryOperator<String> indexModifier,
			Function<T, DirectedNestedArray<T>> endpointModifier) {
		return new DirectedNestedArray<>(
				upperModifier.apply(isUpper),
				rankModifier.applyAsInt(rank),
				directedElements.stream()
						.map(t -> t.modifyWith(upperModifier, rankModifier, indexModifier, endpointModifier))
						.collect(Collectors.toList()),
				indexModifier.apply(indexName)
		);
	}

	public DirectedNestedArray<T> modifyWith(
			UnaryOperator<Boolean> upperModifier,
			IntUnaryOperator rankModifier,
			UnaryOperator<String> indexModifier,
			UnaryOperator<T> endpointModifier) {
		return modifyWith(
				upperModifier,
				rankModifier,
				indexModifier,
				(Function<T, DirectedNestedArray<T>>) (e -> new Endpoint<T>(endpointModifier.apply(e)))
		);
	}

	public DirectedNestedArray<T> modifyWith(UnaryOperator<T> endpointModifier) {
		return modifyWith(
				i -> i,
				i -> i,
				i -> i,
				endpointModifier
		);
	}



	
	
}
