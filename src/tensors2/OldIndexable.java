package tensors2;

public interface OldIndexable<T> {
	T getAtIndex(int... index);
	void setAtIndex(T toSet, int... index);
	int[] getDimensions();
}
