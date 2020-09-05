package tensors3.elementoperations;

import functions.GeneralFunction;
import tensors3.DirectedNestedArrayInterface;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TContainer implements EIT {

	private final DirectedNestedArrayInterface<?, GeneralFunction> contained;
	private final String[] indices;

	public TContainer(DirectedNestedArrayInterface<?, GeneralFunction> contained, String... indices) {
		this.contained = contained;
		this.indices = indices;
	}

//	@Override
//	public GeneralFunction fN(List<String> freeIndices) {
//		return null;
//	}
//
//	@Override
//	public EIT aN(List<String> boundIndices, List<Integer> values) {
//		return null;
//	}

	public GeneralFunction s(Map<String, Integer> indexValues, Map<String, GeneralFunction> toSubstitute, int dimension) {

		int[] index = Arrays.stream(indices)
				.map(indexValues::get)
				.mapToInt(i -> i)
				.toArray();
		return contained.getAtIndex(index);
	}

	public void gAI(Set<String> set) {
		set.addAll(List.of(indices));
	}

}
