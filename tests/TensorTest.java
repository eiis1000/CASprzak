import functions.GeneralFunction;
import functions.commutative.Product;
import functions.endpoint.Constant;
import org.junit.jupiter.api.Test;
import tensor3.*;

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
		DirectedNestedArray<?, Integer> test2 = DirectedNestedArray.direct(NestedArray.nest(
				new Object[][]{
						{1, 2},
						{3, 4}
				}), new boolean[]{true, false});
//		assertEquals(test, test2);
	}

	@Test
	void tensorTest() {
		Tensor test = Tensor.tensor(
				new Object[][]{
						{ONE, TWO},
						{new Constant(3), new Product(TWO, E)}
				}, new boolean[]{true, false}
		);
		System.out.println(test);
		assertEquals(TWO, test.getAtIndex(0, 1));
		assertTrue(test.getDirection());
	}
}
