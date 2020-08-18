package tensor3;

public interface DirectedNestedArrayInterface<I extends DirectedNestedArrayInterface<I, T>, T> extends NestedArrayInterface<I, T> {

	boolean matches(DirectedNestedArrayInterface<I, T> other); // TODO test if deepMatches accounts for direction

	boolean getDirection();

	boolean[] getDirections();


	class Endpoint<I extends DirectedNestedArrayInterface<I, T>, T> extends NestedArrayInterface.Endpoint<I, T> implements DirectedNestedArrayInterface<I, T> {

		public Endpoint(T contained) {
			super(contained);
		}

		@Override
		public boolean matches(DirectedNestedArrayInterface<I, T> other) {
			return other instanceof DirectedNestedArrayInterface.Endpoint;
		}

		@Override
		public boolean getDirection() {
			throw new IllegalStateException("Endpoints have no direction."); // TODO deal with this exception
		}

		@Override
		public boolean[] getDirections() {
			return new boolean[0];
		}
	}

}
