package jxc033200;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Scanner;

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;        

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;            
        }
    }

    Entry<T> root;
    int size;
    // define stack
    Deque<Entry<T>> s = new ArrayDeque<>(); //stack used to store path to any added node
                                            //used to update heights and determine balance factor
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /**  Helper method find(), looks for an entry and pushes path to stack
     * <p>
     * This helper method is used in the add(), contains(), and remove() methods
     * in order to find the node we are dealing with. After the helper method is 
     * called, we can then decide where to go from there.
     * <p>
     * The wrapper method will take as parameter the value being searched for. It 
     * clears the stack and starts the helper method at the root. 
     * <p>
     * The helper method traverses the tree to find the entry containing the specified
     * element, or the entry at which the search failed.
     * <p>
     * @param t - the root of the subtree to search
     * @param x - the element to search for
     * @return the Entry containing the element if found, or the last Entry accessed if not found
     * 
    */

    public Entry<T> find(T x) {     //wrapper method - find entry containing x, or entry at which find failed
        s.clear();        
        return find(root, x);
    }
    
    public Entry<T> find(Entry<T> t, T x) {     //helper method - find entry containing x, or entry at which find failed 
        if (t == null || x.equals(t.element)) {     //if t is null, or the element is found, return t
            return t;
        }
        while (true) {
            if (x.compareTo(t.element) < 0) {   //if x is less than t.element and the left child is null, break and return t
                if (t.left == null) {
                    break;
                }                 
                s.push(t);      //push t onto stack 
                t = t.left;     //continue to next entry for comparison                                
            } else if (x.compareTo(t.element) > 0) {    //if x is greater than t.element and the right child is null, break and return t
                if (t.right == null) {
                    break;
                }                 
                s.push(t);      //push t onto stack
                t=t.right;      //continue to next entry for comparison
            } else {
                break;
            }
        }          
        return t;   //return element found or where last entry accessed if not found
    }

    /** Is x contained in tree?
     * <p>
     * The contains() method checks whether the specified element is in the tree
     * <p>
     * It uses the helper method find() to search for the element and returns true
     * if found, false if not found
     * <p>   
     * @param x - the element to search for
     * @return true if found, false if not found     
    */
    public boolean contains(T x) {
        Entry<T> found = find(x);   //use find() helper method to search for x
        if (found == null || !found.element.equals(x))  //if not found, return false
            return false;
        return true;    //return true if found
    }


    /**
     * This is a helper method to create an Entry
     * <p>
     * When we call super.add() in the AVL tree class, it will create a binarySearchTree
     * Entry instead of a AVLtree entry, unless we use this method. With this method,
     * super.add() will call the overridden createEntry() from the AVLTree class, 
     * creating an Entry from the AVL tree class, with the added height attribute.
     * <p>   
     * @param x - the element to search for
     * @return a new entry  
    */
    public Entry<T> createEntry(T x) {
        return new Entry<>(x, null, null);  // Default to BinarySearchTree.Entry
    }
        
    /**
     * Add x to the Binary Search Tree
     * <p>
     * This method adds x to the tree. If size == 0, it will create the root. 
     * The find() helper method is used to locate the entry where x is, or the closest
     * entry to x if it failed. If the tree already has the root, we call find().
     * If find() finds x, the element already exists, in which we replace the element.
     * If find() fails, we have the entry closest to x. If the element is less than x,
     * we create a left child and push the entry to the stack. (needed to adjust height
     * in AVLTree add() method) If the element is more than x, we create the right child
     * and push the Entry to the stack.
     * <p>
     * @param x - the element to add
     * @return true or false depending on if a new entry is added or not 
    */
    public boolean add(T x) {
        if (size == 0) {
            root = createEntry(x);      //create root using helper
            System.out.println("Creating root");
            size++;     //adjust size of tree
            return true;
        } else {
            Entry<T> t = find(x);
            if (t.element.equals(x)) {      //if x is found using find(), replace 
                System.out.println("Duplicate, replacing " + x);  
                t.element = x;
                return false;
            }
            if (x.compareTo(t.element) < 0) {       //if x is less than the element, create left child
                s.push(t);      //push entry to stack 
                System.out.println("Creating left entry " + x);                
                t.left = createEntry(x);
            }
            else {      //if x is greater than the element, create right child and push entry to stack               
                s.push(t);                   
                t.right = createEntry(x); 
                System.out.println("Creating right entry " + x);
            }
            size++;
            return true;            
        }
    }    

    /** 
     * Remove x from tree. 
     * Return x if found, otherwise return null
     * <p>
     * This method removes x from the tree. If size == 0, there's nothing to remove,
     * so it returns null. The find() helper method is used to locate the entry where 
     * x is, or the closest entry to x if it failed. If find failed, (t.element does not 
     * equal x), we return null (there is nothing to remove). If the entry is found
     * and has less than 2 children, we call splice(). If it has 2 children, we find the
     * minimum element in the right subtree of t, replace t's element with that element,
     * and remove the minRight node.
     * <p>
     * @param x - the element to add
     * @return null if there's nothing to remove, or return x if removed
    */
    public T remove(T x) {
        if (size == 0)      //if no tree
            return null;
        Entry<T> t = find(x);       //t is found element
        if (!t.element.equals(x))       //if x isn't found, return null
            return null;
        if (t.left == null || t.right == null) {    //if t has 0 or 1 child
            splice(t);
        } else {        //if t has 2 children
            s.push(t);      //push t onto stack
            Entry<T> minRight = find(t.right, x);       //replace t.element with minRight.element
            t.element = minRight.element;
            splice(minRight);       //remove minRight
        }
        size--;
        return x;
    }

    /** helper method splice reassigns children according to which entry is removed
     * <p>
     * The top of the stack will be t's parent, and we determine which is the child. 
     * We determine whether t is the parent's left or right child, then reassign the 
     * parent's left or right child to t's left or right child accordingly.
     * <p>
     * @param t - the entry we are splicing
     * @return void
     */
    public void splice(Entry<T> t) {
        Entry<T> parent = s.peek();     //assign parent to top entry of stack
        Entry<T> child = (t.left == null ? t.right : t.left);       //determine which is the child
        if (parent == null) {       //if parent is null, then it's the root
            root = child;            
        } else if (parent.left == t) {     //if t is left child, make t's child the parent's left child
            parent.left = child;
        } else {        //if t is right child, make t's child the parent's right child
            parent.right = child;
        }
    }
 




    /** Iterate elements in sorted order of keys
     Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
        return null;
    }
    
    public T min() {
        return null;
    }

    public T max() {
        return null;
    }

    // Find largest key that is no bigger than x.  Return null if there is no such key.
    public T floor(T x) {
        return null;
    }

    // Find smallest key that is no smaller than x.  Return null if there is no such key.
    public T ceiling(T x) {
        return null;
    }

    // Find predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        return null;
    }

    // Find successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        return null;
    }

   // Create an array with the elements using in-order traversal of tree
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        /* write code to place elements in array here */
        return arr;
    }

    public static void main(String[] args) throws FileNotFoundException {
        BinarySearchTree<Long> bst = new BinarySearchTree<>();
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }        
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        // Initialize the timer
        Timer timer = new Timer();
        
        while (!((operation = sc.next()).equals("End"))) {            
            switch (operation) {
                case "Add": {
                    operand = sc.nextInt();
                    if (bst.add(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Remove": {
                    operand = sc.nextInt();
                    if (bst.remove(operand) != null) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Contains": {
                    operand = sc.nextInt();
                    if (bst.contains(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
            }
        }
        
        // End Time
        timer.end();
        
        System.out.println(result);
        System.out.println(timer);        
    }


    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }
}




