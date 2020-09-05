package tensors3;

import functions.GeneralFunction;
import functions.endpoint.Constant;
import tensors3.elementoperations.*;
import tools.DefaultFunctions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TensorTools {

	public static DirectedNestedArrayInterface<?, GeneralFunction> createFrom(List<String> freeIndices, boolean[] directions, int dimension, ElementAccessor formula) {
		NestedArrayInterface<?, GeneralFunction> array = NestedArray.createSquare(freeIndices.size(), dimension, null);
		int[] freeValues = new int[freeIndices.size()];
		Map<String, Integer> indexValues = new HashMap<>();
		Map<String, GeneralFunction> toSubstitute = new HashMap<>();

		do {
			for (int i = 0; i < freeValues.length; i++) {
				indexValues.put(freeIndices.get(i), freeValues[i]);
				toSubstitute.put(freeIndices.get(i), new Constant(freeValues[i])); // TODO make this more efficient by replacing the loop with stuff in incrementArray
			}
			array.setAtIndex(formula.getValueAt(indexValues, toSubstitute, dimension).simplify(), freeValues);
		} while (directions.length != 0 && incrementArray(freeValues, dimension, 0));

		return DirectedNestedArray.direct(array, directions);
	}

	private static boolean incrementArray(int[] array, int max, int start) {
		array[start]++;
		if (array[start] < max)
			return true;
		array[start] = 0;
		if (start + 1 < array.length)
			return incrementArray(array, max, start + 1);
		return false;
	}

	public static ElementWrapper indexTensor(DirectedNestedArray<?, GeneralFunction> toAccess, String... indices) {
		return new ElementWrapper(toAccess, indices);
	}

	public static GeneralFunctionWrapper wrap(GeneralFunction toWrap) {
		return new GeneralFunctionWrapper(toWrap);
	}

	public static ElementAccessor sum(ElementAccessor... elements) {
		return new ElementSum(elements);
	}

	public static ElementAccessor product(ElementAccessor... elements) {
		if (elements.length == 0)
			return new GeneralFunctionWrapper(DefaultFunctions.ONE);
		if (elements.length == 1)
			return elements[0];

		ElementProduct current = new ElementProduct(elements[0],  elements[1]);
		for (int i = 2; i < elements.length; i++)
			current = new ElementProduct(current, elements[i]);

		return current;
	}

	public static ElementAccessor negative(ElementAccessor elementAccessor) {
		return new ElementProduct(wrap(DefaultFunctions.NEGATIVE_ONE), elementAccessor);
	}

}
