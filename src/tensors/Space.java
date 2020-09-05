package tensors;

import functions.GeneralFunction;
import functions.commutative.Sum;
import functions.endpoint.Constant;
import functions.unitary.transforms.PartialDerivative;
import tensors.elementoperations.ElementAccessor;

import java.util.*;

import static tensors.TensorTools.*;
import static tools.DefaultFunctions.HALF;

public class Space {

	protected final String[] variableStrings;
	public final int dimension;
	public final Tensor metric;
	public final Tensor inverseMetric;
	public final DirectedNested<?, GeneralFunction> christoffel;

	public Space(String[] variableStrings, Tensor metric, Tensor inverseMetric) {
		this.variableStrings = variableStrings;
		this.dimension = variableStrings.length;
		this.metric = metric;
		this.inverseMetric = inverseMetric;
		christoffel = calculateChristoffel();
	}

	public Partial partial(String index, ElementAccessor operand) {
		return new Partial(index, operand);
	}

	private DirectedNested<?, GeneralFunction> calculateChristoffel() {
		return createFrom(
				List.of("\\mu", "\\sigma", "\\nu"),
				new boolean[]{false, true, false},
				2,
				product(
						wrap(HALF),
						inverseMetric.index("\\sigma", "\\rho"),
						sum(
								partial("\\mu", metric.index("\\nu", "\\rho")),
								partial("\\nu", metric.index("\\rho", "\\mu")),
								negative(partial("\\rho", metric.index("\\mu", "\\nu")))
						)
				)
		);
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
