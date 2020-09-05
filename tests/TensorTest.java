import functions.GeneralFunction;
import functions.commutative.Product;
import functions.endpoint.Constant;
import org.junit.jupiter.api.Test;
import tensors3.*;
import tensors3.Tensor;
import tensors3.elementoperations.EIT;
import tensors3.elementoperations.ElementProduct;
import tensors3.elementoperations.TContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tools.DefaultFunctions.*;

public class TensorTest {
	@Test
	void undirectedTest() {
		NestedArray<?, Integer> test = NestedArray.nest(new Object[][]{
				{1, 2},
				{3, 4}
		});
		System.out.println(test);
		assertEquals(2, test.getAtIndex(0, 1));
		assertEquals(2, test.getRank());
		test.setAtIndex(-1, 0, 0);
		assertEquals(-1, test.getAtIndex(0, 0));
		NestedArrayInterface<?, Integer> test2 = test.modifyWith(i -> -2 * i);
		assertEquals(-4, test2.getAtIndex(0, 1));
	}

	@Test
	void directedTest() {
		DirectedNestedArrayInterface<?, Integer> test = DirectedNestedArray.direct(
				new Object[][]{
						{1, 2},
						{3, 4}
				}, new boolean[]{true, false}
		);
		System.out.println(test);
		assertEquals(2, test.getAtIndex(0, 1));
		assertTrue(test.getDirection());
	}

	@Test
	void tensorTest() {
		TensorInterface test = Tensor.tensor(
				new Object[][]{
						{ONE, TWO},
						{new Constant(3), new Product(TWO, E)}
				},
				true, false);
		System.out.println(test);
		assertEquals(TWO, test.getAtIndex(0, 1));
		assertTrue(test.getDirection());
		DirectedNestedArrayInterface<?, GeneralFunction> test2d = DirectedNestedArray.direct(NestedArray.nest(
				new Object[][]{
						{ONE, ONE},
						{ONE, TWO}
				}), new boolean[]{true, false});
		TensorInterface test2 = Tensor.tensor(test2d);
//		TensorInterface sum = Tensor.sum(test, test2);
//		assertEquals(sum.getAtIndex(1, 0), new Constant(4));
//		System.out.println(sum);
	}

	@Test
	void elementTest() {
		TensorInterface id2u = Tensor.tensor(
				new Object[][]{
						{ZERO, ONE},
						{ONE, ZERO}
				},
				false, false
		);
		TensorInterface id2d = Tensor.tensor(
				new Object[][]{
						{ZERO, ONE},
						{ONE, ZERO}
				},
				true, true
		);
		System.out.println(EIT.tFE(List.of("a", "b"), new boolean[]{true, false}, 2,
				new ElementProduct(new TContainer(id2u, "a", "m"), new TContainer(id2d, "m", "b"))
				));
	}

	@Test
	void scalarTest1() {
		TensorInterface C = Tensor.tensor(
				new Object[]{
						ONE,
						TWO
				},
				true
		);
		TensorInterface R = Tensor.tensor(
				new Object[]{
						TEN, ONE
				},
				false
		);
		System.out.println(EIT.tFE(List.of(), new boolean[]{}, 2,
				new ElementProduct(new TContainer(R, "\\mu"), new TContainer(C, "\\mu"))
		));
	}

	@Test
	void scalarTest2() {
		TensorInterface C = Tensor.tensor(
				new Object[]{
						TEN, ONE
				},
				true
		);
		TensorInterface metric = Tensor.tensor(
				new Object[][]{
						{NEGATIVE_ONE, ZERO},
						{ZERO, ONE}
				},
				false, false
		);
		System.out.println(EIT.tFE(List.of("\\nu"), new boolean[]{false}, 2,
				new ElementProduct(new TContainer(C, "\\mu"), new TContainer(metric, "\\mu", "\\nu"))
		));
	}
}
