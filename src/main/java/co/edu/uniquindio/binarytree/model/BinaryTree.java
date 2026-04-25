package co.edu.uniquindio.binarytree.model;

import lombok.Getter;
import java.util.LinkedList;

public class BinaryTree<T extends Comparable<T>> {

    @Getter
    private TreeNode<T> root;
    private int size;

    public BinaryTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void add(T value) {
        if (isEmpty()) {
            root = new TreeNode<>(value);
            size++;
            return;
        }
        add(root, value);
    }

    private void add(TreeNode<T> node, T value) {
        int cmp = node.getInfo().compareTo(value);

        if (cmp == 0)
            throw new RuntimeException("Element already exists: " + value);

        if (cmp > 0) {
            if (node.getLeftChild() == null) {
                TreeNode<T> newNode = new TreeNode<>(value);
                newNode.setParent(node);
                node.setLeftChild(newNode);
                size++;
            } else {
                add(node.getLeftChild(), value);
            }
        } else {
            if (node.getRightChild() == null) {
                TreeNode<T> newNode = new TreeNode<>(value);
                newNode.setParent(node);
                node.setRightChild(newNode);
                size++;
            } else {
                add(node.getRightChild(), value);
            }
        }
    }

    public boolean contains(T value) {
        return search(root, value);
    }

    private boolean search(TreeNode<T> node, T value) {
        if (node == null) return false;
        int cmp = node.getInfo().compareTo(value);
        if (cmp == 0) return true;
        if (cmp < 0)  return search(node.getRightChild(), value);
        return              search(node.getLeftChild(), value);
    }

    public void remove(T value) {
        if (!contains(value))
            throw new RuntimeException("Element not found: " + value);
        root = remove(root, value);
        size--;
    }

    private TreeNode<T> remove(TreeNode<T> node, T value) {
        if (node == null) return null;

        int cmp = node.getInfo().compareTo(value);

        if (cmp > 0) {
            node.setLeftChild(remove(node.getLeftChild(), value));
        } else if (cmp < 0) {
            node.setRightChild(remove(node.getRightChild(), value));
        } else {
            // Leaf node
            if (node.getLeftChild() == null && node.getRightChild() == null)
                return null;

            // One child (right)
            if (node.getLeftChild() == null) {
                node.getRightChild().setParent(node.getParent());
                return node.getRightChild();
            }

            // One child (left)
            if (node.getRightChild() == null) {
                node.getLeftChild().setParent(node.getParent());
                return node.getLeftChild();
            }

            // Two children → replace with in-order successor
            TreeNode<T> successor = getMinNode(node.getRightChild());
            node.setInfo(successor.getInfo());
            node.setRightChild(remove(node.getRightChild(), successor.getInfo()));
        }

        return node;
    }

    public void inOrder(TreeNode<T> node) {
        if (node == null) return;
        inOrder(node.getLeftChild());
        System.out.print(node.getInfo() + " ");
        inOrder(node.getRightChild());
    }

    public void preOrder(TreeNode<T> node) {
        if (node == null) return;
        System.out.print(node.getInfo() + " ");
        preOrder(node.getLeftChild());
        preOrder(node.getRightChild());
    }

    public void postOrder(TreeNode<T> node) {
        if (node == null) return;
        postOrder(node.getLeftChild());
        postOrder(node.getRightChild());
        System.out.print(node.getInfo() + " ");
    }

    public void printByLevel() {
        if (isEmpty()) return;
        LinkedList<TreeNode<T>> queue = new LinkedList<>();
        queue.add(root);
        printByLevel(queue);
        System.out.println();
    }

    private void printByLevel(LinkedList<TreeNode<T>> parents) {
        if (parents.isEmpty()) return;
        LinkedList<TreeNode<T>> children = new LinkedList<>();
        while (!parents.isEmpty()) {
            TreeNode<T> node = parents.removeFirst();
            System.out.print(node.getInfo() + " ");
            if (node.getLeftChild()  != null) children.add(node.getLeftChild());
            if (node.getRightChild() != null) children.add(node.getRightChild());
        }
        printByLevel(children);
    }

    public int getSize() {
        return size;
    }

    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(TreeNode<T> node) {
        if (node == null) return -1;
        return 1 + Math.max(getHeight(node.getLeftChild()), getHeight(node.getRightChild()));
    }

    public int getLevel(T value) {
        return getLevel(root, value, 0);
    }

    private int getLevel(TreeNode<T> node, T value, int level) {
        if (node == null) return -1;
        int cmp = node.getInfo().compareTo(value);
        if (cmp == 0) return level;
        if (cmp < 0)  return getLevel(node.getRightChild(), value, level + 1);
        return              getLevel(node.getLeftChild(),  value, level + 1);
    }

    public int countLeaves() {
        return countLeaves(root);
    }

    private int countLeaves(TreeNode<T> node) {
        if (node == null) return 0;
        if (node.getLeftChild() == null && node.getRightChild() == null) return 1;
        return countLeaves(node.getLeftChild()) + countLeaves(node.getRightChild());
    }

    public T getMin() {
        if (isEmpty()) throw new RuntimeException("Tree is empty");
        return getMinNode(root).getInfo();
    }

    private TreeNode<T> getMinNode(TreeNode<T> node) {
        if (node.getLeftChild() == null) return node;
        return getMinNode(node.getLeftChild());
    }

    public T getMax() {
        if (isEmpty()) throw new RuntimeException("Tree is empty");
        return getMaxNode(root).getInfo();
    }

    private TreeNode<T> getMaxNode(TreeNode<T> node) {
        if (node.getRightChild() == null) return node;
        return getMaxNode(node.getRightChild());
    }

    public void clear() {
        root = null;
        size = 0;
    }
}