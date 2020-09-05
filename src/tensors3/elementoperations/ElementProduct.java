package tensors3.elementoperations;

import functions.GeneralFunction;
import functions.commutative.Product;
import functions.commutative.Sum;
import functions.endpoint.Constant;

import java.util.*;

public class ElementProduct implements EIT {

	private final EIT first;
	private final EIT second;

	public ElementProduct(EIT first, EIT second) {
		this.first = first;
		this.second = second;
	}

//	@Override
//	public GeneralFunction fN(List<String> freeIndices) {
//		return new Product(
//				Arrays.stream(elements)
//						.map(e -> e.fN(freeIndices))
//						.toArray(GeneralFunction[]::new)
//		);
//	}
//
//	@Override
//	public EIT aN(List<String> boundIndices, List<Integer> values) {
//		return new ElementProduct(
//				Arrays.stream(elements)
//						.map(e -> e.aN(boundIndices, values))
//						.toArray(EIT[]::new)
//		);
//	}

//	@Override
//	public GeneralFunction s(Map<String, Integer> indexValues, Map<String, GeneralFunction> toSubstitute) {
//
//		return new Product(
//				Arrays.stream(elements)
//						.map(e -> e.s(indexValues, toSubstitute))
//						.toArray(GeneralFunction[]::new)
//		);
//	}

	public GeneralFunction s(Map<String, Integer> indexValues, Map<String, GeneralFunction> toSubstitute, int dimension) {
		Set<String> entries = indexValues.keySet();
		Set<String> firstSet = new HashSet<>();
		first.gAI(firstSet);
		Set<String> secondSet = new HashSet<>();
		second.gAI(secondSet);

		for (String index : firstSet) {
			if (!entries.contains(index) && secondSet.contains(index)) {
				Map<String, Integer> aaaa = new HashMap<>(indexValues);
				Map<String, GeneralFunction> bbbb = new HashMap<>(toSubstitute);
				GeneralFunction[] toAdd = new Product[dimension];

				for (int i = 0; i < dimension; i++) {
					aaaa.put(index, i);
					bbbb.put(index, new Constant(i));
					toAdd[i] = new Product(first.s(aaaa, bbbb, dimension), second.s(aaaa, bbbb, dimension));
				}

				return new Sum(toAdd);
			}
		}

		return new Sum(first.s(indexValues, toSubstitute, dimension), second.s(indexValues, toSubstitute, dimension));
	}

	public void gAI(Set<String> set) {
		first.gAI(set);
		second.gAI(set);
	}

}