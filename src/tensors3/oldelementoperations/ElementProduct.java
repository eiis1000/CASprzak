package tensors3.oldelementoperations;

import functions.GeneralFunction;
import functions.commutative.Product;

import java.util.Arrays;
import java.util.List;

public class ElementProduct implements EIT {

	private final EIT[] elements;

	public ElementProduct(EIT... elements) {
		this.elements = elements;
	}

	@Override
	public GeneralFunction fN(List<String> freeIndices) {
		return new Product(
				Arrays.stream(elements)
						.map(e -> e.fN(freeIndices))
						.toArray(GeneralFunction[]::new)
		);
	}

	@Override
	public EIT aN(List<String> boundIndices, List<Integer> values) {
		return new ElementProduct(
				Arrays.stream(elements)
						.map(e -> e.aN(boundIndices, values))
						.toArray(EIT[]::new)
		);
	}

}