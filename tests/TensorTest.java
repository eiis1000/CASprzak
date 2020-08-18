import functions.GeneralFunction;
import org.junit.jupiter.api.Test;
import tensor3.NestedArray;
import tensor3.NestedArrayInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TensorTest {
	@Test
	void test1() {
		NestedArray<?, Integer> test = NestedArray.newNestedArray(new Object[][] {
				{1, 2},
				{3, 4}
		});
		System.out.println(test);
		assertEquals(2, test.getAtIndex(0, 1));
		assertEquals(2, test.getRank());
		test.setAtIndex(-1, 0, 0);
		assertEquals(-1, test.getAtIndex(0, 0));
		NestedArrayInterface<?, Integer> test2 = test.modifyWith(i -> -2*i);
		assertEquals(-4, test2.getAtIndex(0, 1));
	}
}
