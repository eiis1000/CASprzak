package tensor3;

import functions.GeneralFunction;
import functions.commutative.Sum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tensor extends DirectedNestedArray<TensorInterface, GeneralFunction> implements TensorInterface {

	public static Tensor tensor(DirectedNestedArrayInterface<?, GeneralFunction> directedNestedArray) {
		if (directedNestedArray.getElements().get(0) instanceof NestedEndpoint)
			return new Tensor(
					directedNestedArray.getDirection(),
					directedNestedArray.getElements().stream()
							.map(e -> (TensorEndpoint) e)
							.map(e -> new TensorEndpoint(e.contained))
							.collect(Collectors.toList())
			);
		else
			return new Tensor(
					directedNestedArray.getDirection(),
					directedNestedArray.getElements().stream()
							.map(Tensor::tensor)
							.collect(Collectors.toList())
			);
	}

	public static Tensor tensor(NestedArrayInterface<?, GeneralFunction> nestedArray, boolean[] directions) {
		return tensor(direct(nestedArray, directions));
	}

	public static Tensor tensor(Object[] elements, boolean[] directions) {
		return tensor(direct(nest(elements), directions));
	}

	protected Tensor(boolean isUpper, List<TensorInterface> elements) {
		super(isUpper, elements);
	}

	@Override
	public void setAtIndex(GeneralFunction toSet, int... index) {
		throw new UnsupportedOperationException("Tensors are final, and their elements should never be changed.");
	}


}
