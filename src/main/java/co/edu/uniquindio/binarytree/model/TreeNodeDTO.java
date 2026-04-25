package co.edu.uniquindio.binarytree.model;

import lombok.Getter;

@Getter
public class TreeNodeDTO<T> {

    private final T value;
    private final TreeNodeDTO<T> left;
    private final TreeNodeDTO<T> right;

    public TreeNodeDTO(T value, TreeNodeDTO<T> left, TreeNodeDTO<T> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public static <T> TreeNodeDTO<T> from(TreeNode<T> node) {
        if (node == null) return null;
        return new TreeNodeDTO<>(
                node.getInfo(),
                from(node.getLeftChild()),
                from(node.getRightChild())
        );
    }
}