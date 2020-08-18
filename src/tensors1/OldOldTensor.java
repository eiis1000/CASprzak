package tensors1;

import functions.GeneralFunction;
import functions.commutative.Product;
import functions.commutative.Sum;
import functions.endpoint.Variable;
import org.jetbrains.annotations.NotNull;
import output.OutputFunction;
import parsing.FunctionParser;
import tools.DefaultFunctions;
import tools.exceptions.NotYetImplementedException;
import tools.helperclasses.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static tools.MiscTools.minimalSimplify;

public class OldOldTensor extends GeneralFunction {

	public static boolean assertValidity = true;
	public static boolean zeroIndexed = true;
	public static boolean reindexVariableSubstitution = true;


	public static void main(String[] args) {
		OldOldTensor zeroes = new OldOldTensor("a", true, 1, 7, DefaultFunctions.ZERO);
		OldOldTensor vec1 = newVector("b", DefaultFunctions.ONE, DefaultFunctions.ONE);
		OldOldTensor bigBoy = newTensor(new int[]{2, 2}, new String[]{"a", "b"}, new boolean[]{true, false},
				new Object[][]{
						{tt("cos(x)"), tt("sin(x)")},
						{tt("-sin(x)"), tt("cos(x)")}
				}
		);
		System.out.println(zeroes);
		System.out.println(vec1);
		System.out.println(bigBoy);
		System.out.println(bigBoy.getElement(1));
		System.out.println(bigBoy.getElement(1, 0));
		zeroIndexed = false;
		System.out.println(bigBoy.getElement(2, 1));
		zeroIndexed = true;
		System.out.println(Arrays.deepToString(bigBoy.getElementTree()));
		System.out.println(tensorProduct(bigBoy, vec1));
		System.out.println();
//		OldOldTensor traceTest = bigBoy.changeIndex("b", "a"); // TODO this no work because it's not a tensor anymore
//		System.out.println(traceTest.executeInternalSums());
		OldOldTensor bigger = tensorProduct(vec1, bigBoy);
		System.out.println(bigger);
		System.out.println(bigger.executeInternalSums());
		System.out.println(tensorProduct(bigBoy, vec1).executeInternalSums());
	}

	private static GeneralFunction tt(String s) {
		return FunctionParser.parseSimplified(s);
	}


	protected String index;
	protected final boolean isContra;
	protected final GeneralFunction[] elements;
	public final int rank;

	protected OldOldTensor(String index, boolean isContra, int rank, GeneralFunction... elements) {
		this.index = index;
		this.isContra = isContra;
		this.rank = rank;
		this.elements = elements;
		if (assertValidity)
			assertValid();
	}

	protected OldOldTensor(String index, boolean isContra, int rank, int length, GeneralFunction fill) {
		this.index = index;
		this.isContra = isContra;
		this.rank = rank;
		elements = new GeneralFunction[length];
		Arrays.fill(elements, fill);
		if (assertValidity)
			assertValid();
	}

	protected void assertValid() {
		if (elements.length == 0) {
			throw new IllegalArgumentException("Tensors cannot have zero length.");
		} else if (elements[0] instanceof OldOldTensor) {
			for (GeneralFunction function : elements)
				if (function instanceof OldOldTensor tensor) {
					tensor.assertValid();
					if (tensor.rank != rank - 1)
						throw new IllegalArgumentException("Mismatched ranks in tensor construction.");
				} else
					throw new IllegalArgumentException("Mismatched tensors and non-tensors in tensor construction.");
		} else {
			if (rank != 1)
				throw new IllegalArgumentException("Mismatched ranks in tensor construction.");
			for (GeneralFunction function : elements)
				if (function instanceof OldOldTensor)
					throw new IllegalArgumentException("Mismatched tensors and non-tensors in tensor construction.");
		}
	}

	public static OldOldTensor newVector(String index, GeneralFunction... elements) {
		return new OldOldTensor(index, true, 1, elements);
	}

	public static OldOldTensor newVector(GeneralFunction... elements) {
		return newVector(null, elements);
	}

	public static OldOldTensor newCovector(String index, GeneralFunction... elements) {
		return new OldOldTensor(index, false, 1, elements);
	}

	public static OldOldTensor newCovector(GeneralFunction... elements) {
		return newCovector(null, elements);
	}

	public static OldOldTensor newTensor(int[] dimensions, String[] indices, boolean[] contravariants, Object[] elements) {
		if (dimensions.length != indices.length || indices.length != contravariants.length)
			throw new IllegalArgumentException("OldOldTensor argument arrays do not have matching lengths.");
		return newTensor(0, dimensions, indices, contravariants, elements);
	}

	private static OldOldTensor newTensor(int loc, int[] dimensions, String[] indices, boolean[] contravariants, Object[] elements) {
		GeneralFunction[] arr = new GeneralFunction[dimensions[loc]];
		try {
			if (loc == dimensions.length - 1) {
				for (int i = 0; i < arr.length; i++)
					arr[i] = (GeneralFunction) elements[i];
				return new OldOldTensor(indices[loc], contravariants[loc], 1, arr);
			} else {
				for (int i = 0; i < arr.length; i++)
					arr[i] = newTensor(loc + 1, dimensions, indices, contravariants, (Object[]) elements[i]);
				return new OldOldTensor(indices[loc], contravariants[loc], ((OldOldTensor) arr[0]).rank + 1, arr);
			}
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Array argument length (rank) does not match nested GeneralFunction array (Object[]) depth.");
		}
	}

	public static OldOldTensor modifyWith(OldOldTensor seed,
										  Function<String, String> indexModifier,
										  Function<Boolean, Boolean> contravarianceModifier,
										  Function<Integer, Integer> rankModifier,
										  Function<OldOldTensor, GeneralFunction> tensorElementModifier,
										  Function<GeneralFunction, GeneralFunction> functionElementModifier,
										  boolean simplifyFunctions) { // TODO move the if statement up a level for efficiency
		if (seed.rank > 1)
			return new OldOldTensor(
					indexModifier.apply(seed.index),
					contravarianceModifier.apply(seed.isContra),
					rankModifier.apply(seed.rank),
					Arrays.stream(seed.elements)
							.map(function -> tensorElementModifier.apply((OldOldTensor) function))
							.toArray(GeneralFunction[]::new)
			);
		else
			return new OldOldTensor(
					indexModifier.apply(seed.index),
					contravarianceModifier.apply(seed.isContra),
					rankModifier.apply(seed.rank),
					Arrays.stream(seed.elements)
							.map(function ->
									simplifyFunctions
									? minimalSimplify(functionElementModifier.apply(function))
									: functionElementModifier.apply(function))
							.toArray(GeneralFunction[]::new)
			);
	}


	private GeneralFunction getElementHelper(LinkedList<Integer> indices) {
		int idx = indices.removeFirst();
		if (indices.isEmpty())
			return elements[idx];
		else if (elements[idx] instanceof OldOldTensor tensor)
			return tensor.getElementHelper(indices);
		else
			throw new IllegalArgumentException("Supplied too more indices than there are nested tensors to index.");
	}

	public GeneralFunction getElement(List<Integer> indices) {
		LinkedList<Integer> linkedIndices = new LinkedList<>(indices);
		if (!zeroIndexed)
			for (ListIterator<Integer> iter = linkedIndices.listIterator(); iter.hasNext();)
				iter.set(iter.next() - 1);
		return getElementHelper(linkedIndices);
	}

	public GeneralFunction getElement(Integer... indices) {
		return getElement(List.of(indices));
	}


	private void getIndicesHelper(String[] indexArray) {
		indexArray[indexArray.length - rank] = index;
		if (elements[0] instanceof OldOldTensor tensor)
			tensor.getIndicesHelper(indexArray);
	}

	public String[] getIndices() {
		String[] indices = new String[rank];
		getIndicesHelper(indices);
		return indices;
	}

	private void getVariancesHelper(boolean[] variances) {
		variances[variances.length - rank] = isContra;
		if (elements[0] instanceof OldOldTensor tensor)
			tensor.getVariancesHelper(variances);
	}

	public boolean[] getVariances() {
		boolean[] list = new boolean[rank];
		getVariancesHelper(list);
		return list;
	}

	private void getDimensionsHelper(int[] dimensions) {
		dimensions[dimensions.length - rank] = elements.length;
		if (elements[0] instanceof OldOldTensor tensor)
			tensor.getDimensionsHelper(dimensions);
	}

	public int[] getDimensions() {
		int[] list = new int[rank];
		getDimensionsHelper(list);
		return list;
	}


	public Object[] getElementTree() {
		if (rank > 1)
			return Arrays.stream(elements)
					.map(function -> ((OldOldTensor) function).getElementTree())
					.toArray();
		else
			return elements;
	}


	public OldOldTensor changeIndex(String from, String to) {
		return modifyWith(this,
				i -> i.equals(from) ? to : i,
				i -> i,
				i -> i,
				i -> i.changeIndex(from, to),
				reindexVariableSubstitution ? i -> i.substituteVariables(Map.of(from, new Variable(to))) : i -> i,
				true);
	}


	public OldOldTensor scale(GeneralFunction scalar) {
		return scale(scalar, this);
	}

	public static OldOldTensor scale(GeneralFunction scalar, OldOldTensor tensor) {
		return modifyWith(tensor,
				i -> i,
				i -> i,
				i -> i,
				i -> i.scale(scalar),
				i -> new Product(i, scalar),
				true);
	}

	public static OldOldTensor tensorProduct(OldOldTensor... tensors) {
		OldOldTensor tensor = tensors[0];
		for (int i = 1; i < tensors.length; i++)
			tensor = tensorProduct(tensor, tensors[i]);
		return tensor;
	}

	private static OldOldTensor tensorProduct(OldOldTensor first, OldOldTensor second) { // TODO check order of resulting indices
		return modifyWith(second,
				i -> i,
				i -> i,
				i -> i + first.rank,
				i -> OldOldTensor.tensorProduct(first, i),
				first::scale,
				false);
	}


	public static OldOldTensor sum(OldOldTensor... tensors) {
		OldOldTensor tensor = tensors[0];
		for (int i = 1; i < tensors.length; i++)
			tensor = addTwo(tensor, tensors[i]);
		return tensor;
	}

	private static OldOldTensor addTwo(OldOldTensor first, OldOldTensor second) {
		if (first.isContra != second.isContra)
			throw new IllegalArgumentException("Mismatched tensor variance in addition.");
		if (first.rank != second.rank)
			throw new IllegalArgumentException("Mismatched tensor rank in addition.");
		else if (!first.index.equals(second.index))
			throw new IllegalArgumentException("Mismatched tensor index name in addition.");
		else if (first.elements.length != second.elements.length)
			throw new IllegalArgumentException("Mismatched tensor size in addition.");

		if (first.elements[0] instanceof OldOldTensor && second.elements[0] instanceof OldOldTensor) {
			GeneralFunction[] elementSums = new GeneralFunction[first.elements.length];
			for (int i = 0; i < elementSums.length; i++)
				elementSums[i] = OldOldTensor.addTwo((OldOldTensor) first.elements[i], (OldOldTensor) second.elements[i]);
			return new OldOldTensor(first.index, first.isContra, first.rank, elementSums);
		} else if (!(first.elements[0] instanceof OldOldTensor || second.elements[0] instanceof OldOldTensor)) {
			GeneralFunction[] elementSums = new GeneralFunction[first.elements.length];
			for (int i = 0; i < elementSums.length; i++)
				elementSums[i] = minimalSimplify(new Sum(first.elements[i], second.elements[i]));
			return new OldOldTensor(first.index, first.isContra, first.rank, elementSums);
		} else
			throw new IllegalArgumentException("Mismatched tensor dimensions in addition.");
	}

	public OldOldTensor executeInternalSums() {
		String[] oldIndices = getIndices();
		Pair<Integer, Integer> repeatedIndex = getRepeatedIndex(oldIndices);
		if (repeatedIndex == null)
			return this;
		int first = repeatedIndex.getFirst();
		int second = repeatedIndex.getSecond();
		boolean[] oldVariances = getVariances();
		if (oldVariances[first] == oldVariances[second])
			throw new IllegalArgumentException("Variances are not opposite in internal tensor sum.");
		int[] oldDimensions = getDimensions();
		if (oldDimensions[first] != oldDimensions[second])
			throw new IllegalArgumentException("Mismatched dimensions of indices in internal tensor sum.");
		int sumLength = oldDimensions[first];

		String[] newIndices = new String[oldIndices.length - 2];
		boolean[] newVariances = new boolean[oldVariances.length - 2];
		int[] newDimensions = new int[oldDimensions.length - 2];
		for (int i = 0, j = 0; i < rank; i++) {
			if (i != first && i != second) {
				newIndices[j] = oldIndices[i];
				newVariances[j] = oldVariances[i];
				newDimensions[j] = oldDimensions[i];
				j++;
			}
		}

		int[] newIxs = new int[newDimensions.length];
		Arrays.fill(newIxs, 0);

		OldOldNestedArray oldElements = new OldOldNestedArray(getElementTree());
		OldOldNestedArray newElements = OldOldNestedArray.createFromDimensions(newDimensions);
		boolean flag = true;
		while (flag) {
			int[] oldIxs = copyToArraySkipping(newIxs, first, second);
			GeneralFunction[] toAdd = new GeneralFunction[sumLength];
			for (int i = 0; i < sumLength; i++) {
				oldIxs[first] = i;
				oldIxs[second] = i;
				toAdd[i] = (GeneralFunction) oldElements.getObjectAtIndex(oldIxs);
			}
			newElements.setObjectAtIndex(sumArbitrary(toAdd), newIxs);
			flag = incrementArray(0, newIxs, newDimensions);
		}
		return OldOldTensor.newTensor(
				newDimensions,
				newIndices,
				newVariances,
				newElements.array
		);
	}

	private static GeneralFunction sumArbitrary(GeneralFunction... elements) {
		if (elements[0] instanceof OldOldTensor) {
			OldOldTensor tensor = (OldOldTensor) elements[0];
			for (int i = 1; i < elements.length; i++)
				tensor = addTwo(tensor, (OldOldTensor) elements[i]);
			return tensor;
		} else
			return new Sum(elements);
	}

	private static int[] copyToArraySkipping(int[] oldArray, int first, int second) {
		int[] newArray = new int[oldArray.length + 2];
		int j = 0;
		for (int i = 0; i < newArray.length; i++)
			if (i != first && i != second)
				newArray[i] = oldArray[j++];
		return newArray;
	}

	private static boolean incrementArray(int loc, int[] indices, int[] maxes) {
		if (loc == indices.length)
			return false;
		else if (indices[loc] + 1 < maxes[loc]) {
			indices[loc]++;
			return true;
		} else {
			indices[loc] = 0;
			return incrementArray(loc + 1, indices, maxes);
		}
	}

	private static Pair<Integer, Integer> getRepeatedIndex(String[] sourceIndices) {
		for (int i = 1; i < sourceIndices.length; i++)
			for (int j = 0; j < i; j++)
				if (sourceIndices[i].equals(sourceIndices[j]))
					return new Pair<>(i, j);
		return null;
	}

	@Override
	public GeneralFunction clone() {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	public GeneralFunction substituteAll(Predicate<? super GeneralFunction> test, Function<? super GeneralFunction, ? extends GeneralFunction> replacer) {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	public boolean equalsFunction(GeneralFunction that) {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	protected int compareSelf(GeneralFunction that) {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	public int hashCode() {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	public @NotNull Iterator<GeneralFunction> iterator() {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	public GeneralFunction getDerivative(String varID) {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	public double evaluate(Map<String, Double> variableValues) {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	public OutputFunction toOutputFunction() {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}

	@Override
	public GeneralFunction simplify() {
		throw new NotYetImplementedException("Not implemented in OldOldTensor.");
	}
	
	public String toString() {
		return (isContra ? "vec" : "cov") + Arrays.toString(elements) + (index == null ? "" : index);
	}
	
}
