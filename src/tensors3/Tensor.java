package tensors3;

import functions.GeneralFunction;
import tensors3.elementoperations.ElementWrapper;

import java.util.List;
import java.util.stream.Collectors;

public class Tensor extends DirectedNestedArray<TensorInterface, GeneralFunction> implements TensorInterface {

	@SuppressWarnings("unchecked")
	public static TensorInterface tensor(DirectedNestedArrayInterface<?, GeneralFunction> directedNestedArray) {
		if (directedNestedArray instanceof DirectedEndpoint)
			return new TensorEndpoint(((DirectedEndpoint<?, GeneralFunction>) directedNestedArray).contained);
		else if (directedNestedArray.getElements().get(0) instanceof DirectedEndpoint)
			return new Tensor(
					directedNestedArray.getDirection(),
					directedNestedArray.getElements().stream()
							.map(e -> (DirectedEndpoint<?, GeneralFunction>) e)
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

	public static TensorInterface tensor(NestedArrayInterface<?, GeneralFunction> nestedArray, boolean... directions) {
		return tensor(direct(nestedArray, directions));
	}

	public static TensorInterface tensor(Object[] elements, boolean... directions) {
		return tensor(direct(nest(elements), directions));
	}

	protected Tensor(boolean isUpper, List<TensorInterface> elements) {
		super(isUpper, elements);
	}


	@Override
	public void setAtIndex(GeneralFunction toSet, int... index) {
		throw new UnsupportedOperationException("Tensors are final, and their elements should never be changed.");
	}

	public ElementWrapper index(String... indices) {
		return TensorTools.indexTensor(this, indices);
	}


	public static class TensorEndpoint extends DirectedEndpoint<TensorInterface, GeneralFunction> implements TensorInterface {
		public TensorEndpoint(GeneralFunction contained) {
			super(contained);
		}
	}
}
