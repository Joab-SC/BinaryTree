package co.edu.uniquindio.binarytree.controller;

import co.edu.uniquindio.binarytree.model.BinaryTree;
import co.edu.uniquindio.binarytree.model.Step;
import co.edu.uniquindio.binarytree.model.TreeNode;
import co.edu.uniquindio.binarytree.model.TreeNodeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tree")
@CrossOrigin(origins = "*")
public class BinaryController {

    private final BinaryTree<Integer> tree = new BinaryTree<>();

    @GetMapping
    public ResponseEntity<TreeNodeDTO<Integer>> getTree() {
        return ResponseEntity.ok(TreeNodeDTO.from(tree.getRoot()));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        if (tree.isEmpty())
            return ResponseEntity.ok(Map.of("empty", true));

        return ResponseEntity.ok(Map.of(
                "size",   tree.getSize(),
                "height", tree.getHeight(),
                "leaves", tree.countLeaves(),
                "min",    tree.getMin(),
                "max",    tree.getMax()
        ));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody Map<String, Integer> body) {
        Integer value = body.get("value");
        if (value == null)
            return ResponseEntity.badRequest().body(Map.of("error", "Missing 'value' in request body"));

        try {
            List<Step<Integer>> steps = buildInsertSteps(value);
            tree.add(value);
            steps.add(new Step<>("inserted", value, "Node " + value + " successfully inserted.", TreeNodeDTO.from(tree.getRoot())));
            return ResponseEntity.ok(Map.of("steps", steps, "tree", TreeNodeDTO.from(tree.getRoot())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@RequestBody Map<String, Integer> body) {
        Integer value = body.get("value");
        if (value == null)
            return ResponseEntity.badRequest().body(Map.of("error", "Missing 'value' in request body"));

        try {
            List<Step<Integer>> steps = buildRemoveSteps(value);
            tree.remove(value);
            steps.add(new Step<>("removed", null, "Node " + value + " successfully removed. Tree reorganized.", TreeNodeDTO.from(tree.getRoot())));
            return ResponseEntity.ok(Map.of("steps", steps, "tree", TreeNodeDTO.from(tree.getRoot())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clear() {
        tree.clear();
        return ResponseEntity.ok(Map.of("message", "Tree cleared successfully."));
    }

    @GetMapping("/contains/{value}")
    public ResponseEntity<Map<String, Object>> contains(@PathVariable Integer value) {
        boolean found = tree.contains(value);
        return ResponseEntity.ok(Map.of(
                "value", value,
                "found", found,
                "level", found ? tree.getLevel(value) : -1
        ));
    }

    @GetMapping("/level/{value}")
    public ResponseEntity<Map<String, Object>> getLevel(@PathVariable Integer value) {
        if (!tree.contains(value))
            return ResponseEntity.badRequest().body(Map.of("error", "Element not found: " + value));
        return ResponseEntity.ok(Map.of("value", value, "level", tree.getLevel(value)));
    }

    @GetMapping("/traversal/inorder")
    public ResponseEntity<Map<String, Object>> inOrder() {
        if (tree.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error", "Tree is empty"));
        List<Integer> result = new ArrayList<>();
        collectInOrder(tree.getRoot(), result);
        return ResponseEntity.ok(Map.of("order", "inorder", "nodes", result));
    }

    @GetMapping("/traversal/preorder")
    public ResponseEntity<Map<String, Object>> preOrder() {
        if (tree.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error", "Tree is empty"));
        List<Integer> result = new ArrayList<>();
        collectPreOrder(tree.getRoot(), result);
        return ResponseEntity.ok(Map.of("order", "preorder", "nodes", result));
    }

    @GetMapping("/traversal/postorder")
    public ResponseEntity<Map<String, Object>> postOrder() {
        if (tree.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error", "Tree is empty"));
        List<Integer> result = new ArrayList<>();
        collectPostOrder(tree.getRoot(), result);
        return ResponseEntity.ok(Map.of("order", "postorder", "nodes", result));
    }

    @GetMapping("/traversal/levelorder")
    public ResponseEntity<Map<String, Object>> levelOrder() {
        if (tree.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error", "Tree is empty"));
        List<Integer> result = new ArrayList<>();
        LinkedList<TreeNode<Integer>> queue = new LinkedList<>();
        queue.add(tree.getRoot());
        while (!queue.isEmpty()) {
            TreeNode<Integer> node = queue.removeFirst();
            result.add(node.getInfo());
            if (node.getLeftChild()  != null) queue.add(node.getLeftChild());
            if (node.getRightChild() != null) queue.add(node.getRightChild());
        }
        return ResponseEntity.ok(Map.of("order", "levelorder", "nodes", result));
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