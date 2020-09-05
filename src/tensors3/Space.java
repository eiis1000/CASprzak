package tensors3;

import functions.GeneralFunction;
import functions.commutative.Sum;
import functions.endpoint.Constant;
import functions.unitary.transforms.PartialDerivative;
import tensors3.elementoperations.ElementAccessor;

import java.util.*;

public class Space {

	protected final String[] variableStrings;
	public final int dimension;
	public final Tensor metric;
	public final Tensor inverseMetric;
	public DirectedNestedArray<?, GeneralFunction> christoffelConnection; // TODO make in constructor & final

	public Space(int dimension, Tensor metric, Tensor inverseMetric, String... variableStrings) {
		this.variableStrings = variableStrings;
		this.dimension = dimension;
		this.metric = metric;
		this.inverseMetric = inverseMetric;
	}

	public Space(int dimension, String... variableStrings) {
		this(dimension, null, null, variableStrings); // TODO this is null, fix that
	}

	public Partial partial(String index, ElementAccessor operand) {
		return new Partial(index, operand);
	}

	public class Partial implements ElementAccessor {

		public final ElementAccessor operand;
		public final String index;

		public Partial(String index, ElementAccessor operand) {
			this.operand = operand;
			this.index = index;
		}

		@Override
		public GeneralFunction getValueAt(Map<String, Integer> indexValues, Map<String, GeneralFunction> toSubstitute, int dimension) {
			Set<String> entries = indexValues.keySet();
			Set<String> operandSet = new HashSet<>();
			operand.getIndices(operandSet);

			if (!entries.contains(index) && operandSet.contains(index)) {
				Map<String, Integer> newIndices = new HashMap<>(indexValues);
				Map<String, GeneralFunction> newSubstitutions = new HashMap<>(toSubstitute);
				GeneralFunction[] toAdd = new PartialDerivative[dimension];

				for (int i = 0; i < dimension; i++) {
					newIndices.put(index, i);
					newSubstitutions.put(index, new Constant(i));
					toAdd[i] = new PartialDerivative(operand.getValueAt(newIndices, newSubstitutions, dimension), variableStrings[i]);
				}

				return new Sum(toAdd);
			}

			return new PartialDerivative(operand.getValueAt(indexValues, toSubstitute, dimension), variableStrings[indexValues.get(index)]);
		}

		@Override
		public void getIndices(Set<String> set) {
			set.add(index);
			operand.getIndices(set);
		}

	}

}
