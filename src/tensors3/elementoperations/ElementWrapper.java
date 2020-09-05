package tensors3.elementoperations;

import functions.GeneralFunction;
import tensors3.DirectedNestedArrayInterface;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElementWrapper implements ElementAccessor {

	private final DirectedNestedArrayInterface<?, GeneralFunction> contained;
	private final String[] indices;

	public ElementWrapper(DirectedNestedArrayInterface<?, GeneralFunction> contained, String... indices) {
		this.contained = contained;
		this.indices = indices;
	}

	public GeneralFunction getValueAt(Map<String, Integer> indexValues, Map<String, GeneralFunction> toSubstitute, int dimension) {

		int[] index = Arrays.stream(indices)
				.map(indexValues::get)
				.mapToInt(i -> i)
				.toArray();
		return contained.getAtIndex(index);
	}

	public void getIndices(Set<String> set) {
		set.addAll(List.of(indices));
	}

}