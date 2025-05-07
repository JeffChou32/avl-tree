# avl-tree

The program is an implementation of an AVL Tree data structure in Java, which ensures self-balancing properties for efficient insertions, deletions, and lookups. It comprises a driver program (AVLTreeDriver) and an AVLTree class extending a generic binary search tree (BinarySearchTree). 

## Driver Program (AVLTreeDriver):

Reads input from a file or standard input, processing commands like "Add", "Remove", and "Contains" for the AVL Tree.
Maintains a result variable to record successful operations, applying a modulus operation to avoid overflow.
Tracks the program's execution time using a Timer class.
Provides an option to verify if the tree maintains valid AVL properties after each operation.

## AVL Tree Implementation (AVLTree Class):
Self-Balancing Mechanism: Implements height-balanced binary tree logic, where each node's balance factor is kept between -1 and 1 by performing rotations (left, right, left-right, and right-left) when necessary.
Efficient Height Tracking: The updateHeight() method ensures each node's height is accurate, crucial for maintaining AVL balance.
Rebalancing Logic: The rebalance() method rebalances nodes based on balance factors, checking conditions for left-heavy or right-heavy states and applying appropriate rotations.
Tree Verification: A verify() method is implemented to confirm AVL properties (balance factors, heights, and BST properties) without relying on stored height values. The Result helper class handles multiple return values, enabling efficient, bottom-up verification.

## Rotation Methods:
rotateLeft and rotateRight: Basic rotations adjust tree structure to maintain balance. These methods involve swapping nodes and updating heights to reestablish AVL properties.

The remove() method inherits from BinarySearchTree and may be overridden to incorporate AVL rebalancing on deletions.
## Verification and Validation:

Verifies AVL properties after each modification. It checks the BST property, height accuracy, and ensures that all balance factors are within the permissible range for AVL trees.
This program is ideal for understanding AVL Trees and balancing operations, with clear debug messages for each rotation and validation checks to reinforce correctness.

$$ Compile: 
javac jxc033200/*.java

$$ Code Execution:
To run all test cases for BinarySearchTree:
java jxc033200/BinarySearchTree bst-t04-no-remove.txt
java jxc033200/BinarySearchTree bst-t05-no-remove.txt

$$ To run all test cases for AVLTreeDriver:
java jxc033200/AVLTreeDriver bst-t04-no-remove.txt
java jxc033200/AVLTreeDriver bst-t05-no-remove.txt
