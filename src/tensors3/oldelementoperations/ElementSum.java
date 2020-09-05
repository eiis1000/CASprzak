package tensors3.oldelementoperations;

import functions.GeneralFunction;
import functions.commutative.Sum;

import java.util.Arrays;
import java.util.List;

public class ElementSum implements EIT {

	private final EIT[] elements;

	public ElementSum(EIT... elements) {
		this.elements = elements;
	}

	@Override
	public GeneralFunction fN(List<String> freeIndices) {
		return new Sum(
				Arrays.stream(elements)
						.map(e -> e.fN(freeIndices))
						.toArray(GeneralFunction[]::new)
		);
	}

	@Override
	public EIT aN(List<String> boundIndices, List<Integer> values) {
		return new ElementSum(
				Arrays.stream(elements)
						.map(e -> e.aN(boundIndices, values))
						.toArray(EIT[]::new)
		);
	}

}
