package tensors3;

import functions.GeneralFunction;
import tensors3.elementoperations.ElementWrapper;

public interface TensorInterface extends DirectedNestedArrayInterface<TensorInterface, GeneralFunction> {

	default ElementWrapper index(String... indices) {
		return TensorTools.indexTensor(this, indices);
	}

}
