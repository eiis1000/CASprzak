package tensors2;

import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

public class OldDirectedNestedArray<T> extends OldNestedArray<T> {

	public final boolean isUpper;
	protected final List<? extends OldDirectedNestedArray<T>> directedElements;
	private boolean[] cachedDirections = null;

	protected OldDirectedNestedArray(boolean isUpper, int rank, List<? extends OldDirectedNestedArray<T>> elements, String indexName) { // TODO assert validity
		super(rank, elements, indexName);
		this.isUpper = isUpper;
		this.directedElements = elements;
	}

	protected OldDirectedNestedArray(boolean isUpper, int rank, List<? extends OldDirectedNestedArray<T>> elements) {
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

	public boolean matches(OldDirectedNestedArray<T> other, boolean checkIndices) {
		return super.matches(other, checkIndices) && isUpper == other.isUpper;
	}


	public OldDirectedNestedArray<T> modifyWith(
			UnaryOperator<Boolean> upperModifier,
			IntUnaryOperator rankModifier,
			UnaryOperator<String> indexModifier,
			Function<T, OldDirectedNestedArray<T>> endpointModifier) {
		return new OldDirectedNestedArray<>(
				upperModifier.apply(isUpper),
				rankModifier.applyAsInt(rank),
				directedElements.stream()
						.map(t -> t.modifyWith(upperModifier, rankModifier, indexModifier, endpointModifier))
						.collect(Collectors.toList()),
				indexModifier.apply(indexName)
		);
	}

	public OldDirectedNestedArray<T> modifyWith(
			UnaryOperator<Boolean> upperModifier,
			IntUnaryOperator rankModifier,
			UnaryOperator<String> indexModifier,
			UnaryOperator<T> endpointModifier) {
		return modifyWith(
				upperModifier,
				rankModifier,
				indexModifier,
				(Function<T, OldDirectedNestedArray<T>>) (e -> new Endpoint<T>(endpointModifier.apply(e)))
		);
	}

	public OldDirectedNestedArray<T> modifyWith(UnaryOperator<T> endpointModifier) {
		return modifyWith(
				i -> i,
				i -> i,
				i -> i,
				endpointModifier
		);
	}

	public static <T> OldDirectedNestedArray<T> combine(OldDirectedNestedArray<T> first, OldDirectedNestedArray<T> second, BinaryOperator<T> combiner) {
		if (!first.deepMatches(second, true))
			if (first.deepMatches(second, false))
				throw new IllegalArgumentException("Indices do not match in OldDirectedNestedArray combination.");
			else
				throw new IllegalArgumentException("Properties do not match in OldDirectedNestedArray combination.");
		return null; // TODO finish, maybe make this instance so you can override at Endpoint?
	}

	
	
}
