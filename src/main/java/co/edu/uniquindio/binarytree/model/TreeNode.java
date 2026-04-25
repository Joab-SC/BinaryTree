package co.edu.uniquindio.binarytree.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNode<T> {

    private TreeNode<T> leftChild;
    private TreeNode<T> rightChild;
    private TreeNode<T> parent;
    private T info;

    public TreeNode(T info) {
        this.leftChild = null;
        this.rightChild = null;
        this.info = info;
    }
}