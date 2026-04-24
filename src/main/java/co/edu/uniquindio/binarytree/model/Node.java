package co.edu.uniquindio.binarytree.model;

/**
 * Representa un nodo del árbol BST.
 * Cada nodo tiene un valor, y referencias a su hijo izquierdo y derecho.
 */
public class Node {
    public int value;
    public Node left;
    public Node right;

    public Node(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}
