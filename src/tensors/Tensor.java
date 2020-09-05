package tensors;

import functions.GeneralFunction;
import tensors.elementoperations.ElementWrapper;

public interface Tensor extends DirectedNested<Tensor, GeneralFunction> {

	default ElementWrapper index(String... indices) {
		return TensorTools.indexTensor(this, indices);
	}

}
