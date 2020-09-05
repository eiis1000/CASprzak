package tensors3.oldelementoperations;

import functions.GeneralFunction;

import java.util.List;

public interface EIT {

//	Tensor createAAAA(List<String> freeIndices, List<String> boundIndices, EIT unt);

	GeneralFunction fN(List<String> freeIndices);

	EIT aN(List<String> boundIndices, List<Integer> values);

}
