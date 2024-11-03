
/** Starter code for AVL Tree
 */
 
// replace package name with your netid
package jxc033200;

public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    static class Entry<T> extends BinarySearchTree.Entry<T> {
        int height;        
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, (Entry<T>)left, (Entry<T>)right);                      
        }
    }

    AVLTree() {
	super();
    }
    //overridden createEntry to ensure that super.add() will create an AVLTree entry
    @Override
    public Entry<T> createEntry(T x) {
        Entry<T> t = new Entry<>(x, null, null); 
        return t;
    }

	/**
     * Adds an element to the tree, rebalancing it if necessary to maintain AVL properties
     * <p>
     * Calls super.add() to inherit the add method from the BinarySearchTree class, then 
     * goes through the s stack to update heights and rebalance the tree. After rebalancing,
     * updates the parent's child reference if the current entry changed.
     * <p>
     * @param x - the element to add
     * @return true if the element was added, false if the element is a duplicate
     */
    @Override
    public boolean add(T x) {    
        System.out.println("Adding " + x);
        if (!super.add(x)) return false;             
        while (!s.isEmpty()) {
            Entry<T> node = (Entry<T>)s.pop();      // Get the Entry from the stack                              
            updateHeight(node);     // Update the height of the current Entry
            node = rebalance(node);     // Rebalance if necessary              
            if (!s.isEmpty()) {     // After rebalancing, update the parent's child reference if needed
                Entry<T> parent = (Entry<T>) s.peek();
                if (parent.left == node || x.compareTo(parent.element) < 0) {
                    parent.left = node;     // Update the left child
                } else {
                    parent.right = node;    // Update the right child
                }
            } else {
                root = node;    // Update the root if we've reached the top of the tree
            }
        }
        return true;
    }

    /**
     * Updates the height of an Entry based on the heights of its children
     * <p>
     * Gets the height of the left and right child, then calculates the height of the 
     * entry as the greater height + 1.
     * <p>
     * @param t
     */
    private void updateHeight(Entry<T> t) {
        int leftChildHeight = height((Entry<T>) t.left);    //get heights of left and right child
        int rightChildHeight = height((Entry<T>) t.right);
        t.height = 1 + Math.max(leftChildHeight, rightChildHeight);     //height is max of left/right child+1
    }
    
    /**
     * Gets the balance factor of a Entry
     * <p>
     * The balance factor determines whether a Entry needs to be rebalanced.
     * It is the height difference between the left and right subtree.
     * <p>
     * @param t - the Entry we need the balance factor for
     * @return the balance factor of the Entry
     */
    private int getBalanceFactor(Entry<T> t) {
        return height((Entry<T>) t.right) - height((Entry<T>) t.left);
    }

    /**
     * Calculates the height of a Entry
     * <p>
     * null Entries will have a height of -1
     * <p?
     * @param t - the entry for which to get the height
     * @return  - returns the height of the Entry, or -1 for no Entry
     */
    private int height(Entry<T> t) {
        return (t == null) ? -1 : t.height;
    }
    
    /**
     * Performs a left rotation
     * <p>
     * Save the right child of the Entry as newRoot. We then replace the right child
     * of the Entry with t.right.left, and then set the Entry as newRoot's left child.
     * <p>
     * @param t - the entry needing rotation
     * @return newRoot - the new root of subtree after rotation
     */
    private Entry<T> rotateLeft(Entry<T> t) {
        Entry<T> newRoot = (Entry<T>) t.right;
        t.right = newRoot.left;
        newRoot.left = t;    
        updateHeight(t);
        updateHeight(newRoot);   
        System.out.println("Performed left rotation on node " + t.element); 
        return newRoot;
    }
    
    /**
     * Performs a right rotation
     * <p>
     * Save the left child of the Entry as newRoot. We then replace the left child
     * of the Entry with t.left.right, and then set the Entry as newRoot's right child.
     * <p>
     * @param t - the entry needing rotation
     * @return newRoot - the new root of subtree after rotation
     */
    private Entry<T> rotateRight(Entry<T> t) {
        Entry<T> newRoot = (Entry<T>) t.left;
        t.left = newRoot.right;
        newRoot.right = t;    
        updateHeight(t);
        updateHeight(newRoot);   
        System.out.println("Performed right rotation on node " + t.element); 
        return newRoot;
    }

    /**
     * Rebalance a node if its balance factor is outside the allowed range (-1 to 1)
     * <p>
     * This handles the different rebalancing cases: right rotation, LeftRight rotation,
     * right rotation, RightLeft rotation. We call the getBalanceFactor method to get the
     * balance factor of the Entry. If < -1, it is left heavy, in which we do a right rotation.
     * If it is left heavy, and the left child also has a negative balance factor, we do
     * a left-right rotation.
     * If the balance factor is > 1, it is right heavy, and we do a left rotation.
     * If right heavy and the right child has a positive balance factor, we do a right-left 
     * rotation. 
     * If the balance factor is between -1 and 1, we simply return the Entry without rotations.
     * <p>
     * @param t - the entry needing rebalancing
     * @return t - the original entry if not rotated, or the root entry after rotation.
     */
    private Entry<T> rebalance(Entry<T> t) {        
        int balanceFactor = getBalanceFactor(t);
        if (balanceFactor < -1) {
            System.out.println("balancefactor < -1, left heavy");
            if (getBalanceFactor((Entry<T>) t.left) <= 0) {
                t = rotateRight(t);     //Rotate right
            } else {
                t.left = rotateLeft((Entry<T>) t.left);     //Rotate left-right
                t = rotateRight(t);
            }
        }
        if (balanceFactor > 1) {
            System.out.println("balancefactor > 1, right heavy");
            if (getBalanceFactor((Entry<T>) t.right) >= 0) {
                t = rotateLeft(t);      //Rotate left
            } else {
                t.right = rotateRight((Entry<T>) t.right);      //rotate right-left
                t = rotateLeft(t);
            }
        }
        return t;
    }
	
	//Optional. Complete for extra credit
	@Override
    public T remove(T x) {
	return super.remove(x);
    }
	
	/** TO DO
	 *	verify if the tree is a valid AVL tree, that satisfies 
	 *	all conditions of BST, and the balancing conditions of AVL trees. 
	 *	In addition, do not trust the height value stored at the nodes, and
	 *	heights of nodes have to be verified to be correct.  Make your code
	 *  as efficient as possible. HINT: Look at the bottom-up solution to verify BST
	*/
	public boolean verify() {
        if (root == null) return true; // An empty tree is a valid AVL tree
        return verify((Entry<T>) root).flag; // Call the helper method starting from the root
    }
    
    // Helper method that recursively verifies the AVL tree and returns multiple values
    private Result verify(Entry<T> node) {
        if (node == null) {
            // For a null node, return a valid result with base values
            return new Result(true, null, null, -1);
        }
        
        T cur = node.element; // Current node's element
        
        // Initialize current node's minimum and maximum values to its own value
        T lmin = cur, rmax = cur;
        int lh = -1, rh = -1; // Initialize left and right heights
        
        // Verify the left subtree
        if (node.left != null) {
            Result leftResult = verify((Entry<T>) node.left);
            if (!leftResult.flag || leftResult.max.compareTo(cur) >= 0) {
                return new Result(false, leftResult.min, leftResult.max, 1 + leftResult.height);
            }
            lmin = leftResult.min; // Update the minimum value
            lh = leftResult.height; // Update the left height
        }
        
        // Verify the right subtree
        if (node.right != null) {
            Result rightResult = verify((Entry<T>) node.right);
            if (!rightResult.flag || rightResult.min.compareTo(cur) <= 0) {
                return new Result(false, rightResult.min, rightResult.max, 1 + rightResult.height);
            }
            rmax = rightResult.max; // Update the maximum value
            rh = rightResult.height; // Update the right height
        }
        
        // Check AVL balance condition: |lh - rh| <= 1
        if (Math.abs(lh - rh) > 1) {
            return new Result(false, lmin, rmax, node.height);
        }
        
        // Calculate the current node's height
        int height = 1 + Math.max(lh, rh);
        
        // Return the final result for the current subtree
        return new Result(true, lmin, rmax, height);
    }
    
    // Helper class to store multiple return values
    private class Result {
        boolean flag; 
        T min; 
        T max; 
        int height; 
        
        Result(boolean flag, T min, T max, int height) {
            this.flag = flag;
            this.min = min;
            this.max = max;
            this.height = height;
        }
    }
}

