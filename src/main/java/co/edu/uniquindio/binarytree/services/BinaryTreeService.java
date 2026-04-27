package co.edu.uniquindio.binarytree.services;

import co.edu.uniquindio.binarytree.model.BinaryTree;
import co.edu.uniquindio.binarytree.model.Step;
import co.edu.uniquindio.binarytree.model.TreeNode;
import co.edu.uniquindio.binarytree.model.TreeNodeDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class BinaryTreeService {

    private final BinaryTree<Integer> tree = new BinaryTree<>();

    public TreeNodeDTO<Integer> getTree() {
        return TreeNodeDTO.from(tree.getRoot());
    }

    public Map<String, Object> getStats() {
        if (tree.isEmpty())
            return Map.of("empty", true);

        return Map.of(
                "size",   tree.getSize(),
                "height", tree.getHeight(),
                "leaves", tree.countLeaves(),
                "min",    tree.getMin(),
                "max",    tree.getMax()
        );
    }

    public Map<String, Object> insert(Integer value) {
        List<Step<Integer>> steps = buildInsertSteps(value);
        tree.add(value);
        steps.add(new Step<>("inserted", value, "Node " + value + " successfully inserted.", TreeNodeDTO.from(tree.getRoot())));
        return Map.of("steps", steps, "tree", TreeNodeDTO.from(tree.getRoot()));
    }

    public Map<String, Object> remove(Integer value) {
        List<Step<Integer>> steps = buildRemoveSteps(value);
        tree.remove(value);
        steps.add(new Step<>("removed", null, "Node " + value + " successfully removed. Tree reorganized.", TreeNodeDTO.from(tree.getRoot())));
        return Map.of("steps", steps, "tree", TreeNodeDTO.from(tree.getRoot()));
    }

    public void clear() {
        tree.clear();
    }

    public Map<String, Object> contains(Integer value) {
        boolean found = tree.contains(value);
        return Map.of(
                "value", value,
                "found", found,
                "level", found ? tree.getLevel(value) : -1
        );
    }

    public Map<String, Object> getLevel(Integer value) {
        if (!tree.contains(value))
            throw new RuntimeException("Element not found: " + value);
        return Map.of("value", value, "level", tree.getLevel(value));
    }

    public Map<String, Object> traversal(String type) {
        if (tree.isEmpty())
            throw new RuntimeException("Tree is empty");

        List<Integer> result = new ArrayList<>();

        switch (type) {
            case "inorder"    -> collectInOrder(tree.getRoot(), result);
            case "preorder"   -> collectPreOrder(tree.getRoot(), result);
            case "postorder"  -> collectPostOrder(tree.getRoot(), result);
            case "levelorder" -> collectLevelOrder(result);
            default           -> throw new RuntimeException("Unknown traversal type: " + type);
        }

        return Map.of("order", type, "nodes", result);
    }

    private void collectInOrder(TreeNode<Integer> node, List<Integer> result) {
        if (node == null) return;
        collectInOrder(node.getLeftChild(), result);
        result.add(node.getInfo());
        collectInOrder(node.getRightChild(), result);
    }

    private void collectPreOrder(TreeNode<Integer> node, List<Integer> result) {
        if (node == null) return;
        result.add(node.getInfo());
        collectPreOrder(node.getLeftChild(), result);
        collectPreOrder(node.getRightChild(), result);
    }

    private void collectPostOrder(TreeNode<Integer> node, List<Integer> result) {
        if (node == null) return;
        collectPostOrder(node.getLeftChild(), result);
        collectPostOrder(node.getRightChild(), result);
        result.add(node.getInfo());
    }

    private void collectLevelOrder(List<Integer> result) {
        LinkedList<TreeNode<Integer>> queue = new LinkedList<>();
        queue.add(tree.getRoot());
        while (!queue.isEmpty()) {
            TreeNode<Integer> node = queue.removeFirst();
            result.add(node.getInfo());
            if (node.getLeftChild()  != null) queue.add(node.getLeftChild());
            if (node.getRightChild() != null) queue.add(node.getRightChild());
        }
    }

    private List<Step<Integer>> buildInsertSteps(Integer value) {
        List<Step<Integer>> steps = new ArrayList<>();

        if (tree.isEmpty()) {
            steps.add(new Step<>("traverse", null, "Tree is empty. " + value + " becomes the root.", TreeNodeDTO.from(tree.getRoot())));
            return steps;
        }

        var current = tree.getRoot();
        while (current != null) {
            int cmp = current.getInfo().compareTo(value);
            if (cmp > 0) {
                steps.add(new Step<>("traverse", current.getInfo(), value + " < " + current.getInfo() + " → go left", TreeNodeDTO.from(tree.getRoot())));
                if (current.getLeftChild() == null) break;
                current = current.getLeftChild();
            } else {
                steps.add(new Step<>("traverse", current.getInfo(), value + " > " + current.getInfo() + " → go right", TreeNodeDTO.from(tree.getRoot())));
                if (current.getRightChild() == null) break;
                current = current.getRightChild();
            }
        }

        steps.add(new Step<>("place", value, "Empty spot found. Inserting " + value + " here.", TreeNodeDTO.from(tree.getRoot())));
        return steps;
    }

    private List<Step<Integer>> buildRemoveSteps(Integer value) {
        List<Step<Integer>> steps = new ArrayList<>();

        var current = tree.getRoot();
        while (current != null) {
            int cmp = current.getInfo().compareTo(value);
            if (cmp == 0) {
                boolean hasLeft  = current.getLeftChild()  != null;
                boolean hasRight = current.getRightChild() != null;

                if (!hasLeft && !hasRight) {
                    steps.add(new Step<>("found", value, value + " is a leaf node → remove directly.", TreeNodeDTO.from(tree.getRoot())));
                } else if (!hasLeft || !hasRight) {
                    steps.add(new Step<>("found", value, value + " has one child → replace node with its child.", TreeNodeDTO.from(tree.getRoot())));
                } else {
                    var successor = current.getRightChild();
                    while (successor.getLeftChild() != null) successor = successor.getLeftChild();
                    steps.add(new Step<>("found", value, value + " has two children → in-order successor is " + successor.getInfo() + ". Replace and reorganize.", TreeNodeDTO.from(tree.getRoot())));
                }
                break;
            } else if (cmp > 0) {
                steps.add(new Step<>("traverse", current.getInfo(), "Searching: " + value + " < " + current.getInfo() + " → go left", TreeNodeDTO.from(tree.getRoot())));
                current = current.getLeftChild();
            } else {
                steps.add(new Step<>("traverse", current.getInfo(), "Searching: " + value + " > " + current.getInfo() + " → go right", TreeNodeDTO.from(tree.getRoot())));
                current = current.getRightChild();
            }
        }

        return steps;
    }
}