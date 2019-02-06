/**
 * PQ interface
 * @author - Priyanka Bura - 11-13-2016
 */
public interface PQ<T> {
    public void insert(T x);
    public T deleteMin();
    public T min();

    public void add(T x);
    public T remove();
    public T peek();
}
