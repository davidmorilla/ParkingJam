package es.upm.pproject.parkingjam.parking_jam.utilities;
/**
 * A utility class to hold a pair of objects.
 * 
 * @param <L> the type of the left object
 * @param <R> the type of the right object
 */
public class Pair<L, R> {
    private L left;
    private R right;
    
    /**
     * Constructs a new Pair with the specified values.
     * 
     * @param left  the left object
     * @param right the right object
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }
    /**
     * Returns the left object.
     * 
     * @return the left object
     */
    public L getLeft() {
        return left;
    }
    /**
     * Sets the left object.
     * 
     * @param left the new left object
     */
    public void setLeft(L left) {
        this.left = left;
    }
    /**
     * Returns the right object.
     * 
     * @return the right object
     */
    public R getRight() {
        return right;
    }
    /**
     * Sets the right object.
     * 
     * @param right the new right object
     */
    public void setRight(R right) {
        this.right = right;
    }
}
