package tensors3.oldelementoperations;

import functions.GeneralFunction;

import java.util.List;

public class GFContainer implements EIT {

	public final GeneralFunction contained;

	public GFContainer(GeneralFunction contained) {
		this.contained = contained;
	}

	@Override
	public GeneralFunction fN(List<String> freeIndices) {
		return contained;
	}

	@Override
	public EIT aN(List<String> boundIndices, List<Integer> values) {
		return null;
	}
}
