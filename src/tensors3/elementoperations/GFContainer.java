package tensors3.elementoperations;

import functions.GeneralFunction;

import java.util.Map;
import java.util.Set;

public class GFContainer implements EIT {

	public final GeneralFunction contained;

	public GFContainer(GeneralFunction contained) {
		this.contained = contained;
	}

//	@Override
//	public GeneralFunction fN(List<String> freeIndices) {
//		return contained;
//	}
//
//	@Override
//	public EIT aN(List<String> boundIndices, List<Integer> values) {
//		return null;
//	}

	@Override
	public GeneralFunction s(Map<String, Integer> indexValues, Map<String, GeneralFunction> toSubstitute, int dimension) {
//		Map<String, GeneralFunction> toSubstitute = new HashMap<>(indexValues.size());
//		indexValues.forEach((k, v) -> toSubstitute.put(k, new Constant(v)));
		return contained.substituteVariables(toSubstitute);
	}

	public void gAI(Set<String> set) {
		// Do nothing
	}


}
