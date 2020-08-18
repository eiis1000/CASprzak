import functions.GeneralFunction;
import org.junit.jupiter.api.Test;
import tensor3.NestedArray;

public class TensorTest {
	@Test
	void test1() {
		NestedArray<?, Integer> test = NestedArray.newNestedArray(new Object[][] {
				{1, 2},
				{3, 4}
		});
		System.out.println(test);
		System.out.println(test.getAtIndex(1, 1) + test.getRank());
	}
}
