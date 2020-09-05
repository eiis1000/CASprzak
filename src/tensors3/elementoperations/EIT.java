package tensors3.elementoperations;

import functions.GeneralFunction;
import functions.endpoint.Constant;
import tensors3.DirectedNestedArray;
import tensors3.NestedArray;
import tensors3.NestedArrayInterface;
import tensors3.Tensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EIT {


//	GeneralFunction fN(List<String> freeIndices);
//
//	EIT aN(List<String> boundIndices, List<Integer> values);

	GeneralFunction s(Map<String, Integer> indexValues, Map<String, GeneralFunction> toSubstitute, int dimension);

	void gAI(Set<String> set);

	static DirectedNestedArray<?, GeneralFunction> tFE(List<String> freeIndices, boolean[] directions, int dimension, EIT unt) {
		NestedArrayInterface<?, GeneralFunction> array = NestedArray.aaa(freeIndices.size(), dimension, null);
		int[] free = new int[freeIndices.size()];
		Map<String, Integer> indexValues = new HashMap<>();
		Map<String, GeneralFunction> toSubstitute = new HashMap<>();

		do {
			for (int i = 0; i < free.length; i++) {
				indexValues.put(freeIndices.get(i), free[i]);
				toSubstitute.put(freeIndices.get(i), new Constant(free[i])); // TODO make this more efficient by replacing the loop with stuff in incrementArray
			}
			array.setAtIndex(unt.s(indexValues, toSubstitute, dimension).simplify(), free);
		} while (incrementArray(free, dimension, 0));

		return Tensor.tensor(array, directions);
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


}
