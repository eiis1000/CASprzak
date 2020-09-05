package tensors3;

public interface Indexable<T> {
	T getAtIndex(int... index);
	void setAtIndex(T toSet, int... index);
	int[] getDimensions();
}
