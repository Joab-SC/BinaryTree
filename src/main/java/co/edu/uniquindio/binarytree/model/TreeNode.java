package co.edu.uniquindio.binarytree.model;


public class TreeNode {
    private int value;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }


    public static TreeNode fromNode(Node node) {
        if (node == null) return null;

        TreeNode treeNode = new TreeNode(node.value);
        treeNode.left = fromNode(node.left);   // recursivo hacia la izquierda
        treeNode.right = fromNode(node.right); // recursivo hacia la derecha
        return treeNode;
    }


    public int getValue() { return value; }
    public TreeNode getLeft() { return left; }
    public TreeNode getRight() { return right; }
}
