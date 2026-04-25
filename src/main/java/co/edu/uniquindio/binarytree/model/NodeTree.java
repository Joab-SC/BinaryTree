package co.edu.uniquindio.binarytree.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeTree<T> {

    private NodeTree<T> leftChild;
    private NodeTree<T> rightChild;
    private NodeTree<T> parent;
    private T info;

    public NodeTree(T info) {
        this.leftChild = null;
        this.rightChild = null;
        this.info = info;
    }
}
