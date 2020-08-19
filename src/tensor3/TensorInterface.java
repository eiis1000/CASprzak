package tensor3;

import functions.GeneralFunction;
import functions.commutative.Sum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public interface TensorInterface extends DirectedNestedArrayInterface<TensorInterface, GeneralFunction> {

	static TensorInterface sum(TensorInterface first, TensorInterface second) {
		if (first instanceof TensorEndpoint firstEnd && second instanceof TensorEndpoint secondEnd)
			return new TensorEndpoint(new Sum(firstEnd.contained, secondEnd.contained));
		else if (!first.deepMatches(second))
			throw new IllegalArgumentException("Cannot evaluate the sum of non-matching tensors.");
		else {
			Iterator<TensorInterface> zip = new Zip<>(first.getElements().iterator(), second.getElements().iterator(), TensorInterface::sum);
			List<TensorInterface> elementList = new ArrayList<>(first.getElements().size());
			zip.forEachRemaining(elementList::add);
			return new Tensor(first.getDirection(), elementList);
		}
	}

	class TensorEndpoint extends DirectedEndpoint<TensorInterface, GeneralFunction> implements TensorInterface {
		public TensorEndpoint(GeneralFunction contained) {
			super(contained);
		}
	}
}
