package tensors3.oldelementoperations;

import functions.GeneralFunction;
import tensors3.TensorInterface;

import java.util.List;

public class TContainer implements EIT {

	private final TensorInterface contained;

	public TContainer(TensorInterface contained) {
		this.contained = contained;
	}

	@Override
	public GeneralFunction fN(List<String> freeIndices) {
		return null;
	}

	@Override
	public EIT aN(List<String> boundIndices, List<Integer> values) {
		return null;
	}
}
