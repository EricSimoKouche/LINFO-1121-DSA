package fundamentals;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Proposed at the exam of August 2025
 * You are asked to implement a specialized linked chain data structure
 * optimized for high-performance insertions.
 *
 * This chain consists of n elements numbered from 0 to n-1.
 * Elements 0 and n-1 are always present in the chain and cannot be removed or relocated.
 * No elements may be inserted before 0 or after n-1.
 * All other elements start unlinked and can be inserted exactly once.
 *
 * To ensure constant-time operations, the chain is represented internally using two arrays:
 * 	successor[i] and predecessor[i] represent the immediate next and
 * 	previous elements of element i in the chain.
 * 	If the element i is not yet part of the chain, we have successor[i] = predecessor[i] = i.
 *  By convention, predecessor[0] == 0 and successor[n - 1] == n - 1.
 *
 * When the data structure is initialized with n elements,
 * the chain contains exactly two linked elements: 0 <-> n-1.
 *
 * You must implement the following methods:
 * 	-	insertAfter(a, b): Insert element b immediately after element a in the chain.
 * 	-	insertBefore(a, b): Insert element b immediately before element a in the chain.
 * 	-   iterator() that enables to iterate over the elements of the chain in their current order,
 * 	    from 0 to n-1.
 */
public class LinkedChain implements Iterable<Integer> {

    protected int[] successor;
    protected int[] predecessor;
    private int n;

    /**
     * Constructs a linked chain with n elements.
     * The chain is initialized with two linked elements: 0 <-> n-1.
     * The other elements (1 to n-2) are not yet in the chain.
     * @param n > 1 the capacity of the chain
     */
    public LinkedChain(int n) {
        // TODO
        this.successor = new int[n];
        this.predecessor = new int[n];
        this.n = n;

        // Initializing the links in the chain
        this.predecessor[0] = 0;
        this.successor[0] = n-1;
        this.predecessor[n-1] = 0;
        this.successor[n-1] = n-1;
        for (int i = 1;  i < n-1; i++) {
            this.predecessor[i] = i;
            this.successor[i] = i;
        }
    }


    /**
     * Inserts an element just after another one in the chain
     * @param a the element after which b will be inserted,
     *        "a" must be in the chain and must be different from n-1 (last element)
     * @param b the element to insert, it must not be in the chain
     * @throws IllegalArgumentException if "a" is not in the chain,
     *         or if "b" is already in the chain or if "a" is the last
     *         or if "a" or "b" are not in the range [0, n-1]
     */
    public void insertAfter(int a, int b) {
        // TODO
        if ( a < 0 || a >= n || b < 0 || b >= n)
            throw new IllegalArgumentException("a: " + a + " or b:" + b + " are not in the range [0, "+ (n-1) +"]");
        if (predecessor[a] == a && successor[a] == a)
            throw new IllegalArgumentException("a: " + a + " is not in the chain");
        if (predecessor[b] != b || successor[b] != b)
            throw new IllegalArgumentException("b: " + b + " is already in the chain");
        if (a == n- 1)
            throw new IllegalArgumentException("a: " + a + " is the last element");

        predecessor[successor[a]] = b;
        successor[b] = successor[a];
        predecessor[b] = a;
        successor[a] = b;
    }


    /**
     * Inserts element "b" just before element "a".
     * @param a the element before which "b" should be inserted,
     *        "a" must be in the chain and must be different from 0 (first element)
     * @param b the element to insert, it must not be in the chain
     * @throws IllegalArgumentException if "a" is not in the chain,
     *        or if "b" is already in the chain or if "a" is the first
     *        or if "a" or "b" are not in the range [0, n-1]
     */
    public void insertBefore(int a, int b) {
        // TODO
        if ( a < 0 || a >= n || b < 0 || b >= n)
            throw new IllegalArgumentException("a: " + a + " or b:" + b + " are not in the range [0, "+ (n-1) +"]");
        if (predecessor[a] == a && successor[a] == a)
            throw new IllegalArgumentException("a: " + a + " is not in the chain");
        if (predecessor[b] != b || successor[b] != b)
            throw new IllegalArgumentException("b: " + b + " is already in the chain");
        if (a == 0)
            throw new IllegalArgumentException("a: " + a + " is the last element");

        successor[predecessor[a]] = b;
        predecessor[b] = predecessor[a];
        successor[b] = a;
        predecessor[a] = b;
    }



    /**
     * Iterator over the chain from 0 to n-1 in the order they are connected.
     * @return an iterator over the chain, in the order they are connected,
     *         starting from the first i.e. 0 and ending with the last i.e. n-1
     */
    @Override
    public Iterator<Integer> iterator() {
        // TODO
         return new Iterator<Integer>() {

             private int index = 0;
             private boolean endReached = false;

             @Override
             public boolean hasNext() {
                 return !endReached;
             }

             @Override
             public Integer next() {
                 if (!hasNext())
                     throw new NoSuchElementException();
                 Integer result = index;
                 if (index == n-1)
                     endReached = true;
                 index = successor[index];
                 return result;
             }
         };
    }

}
