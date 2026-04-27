package co.edu.uniquindio.binarytree.controller;

import co.edu.uniquindio.binarytree.model.TreeNodeDTO;
import co.edu.uniquindio.binarytree.services.BinaryTreeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tree")
@CrossOrigin(origins = "*")
public class BinaryController {

    private final BinaryTreeService service;

    public BinaryController(BinaryTreeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<TreeNodeDTO<Integer>> getTree() {
        return ResponseEntity.ok(service.getTree());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(service.getStats());
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody Map<String, Integer> body) {
        Integer value = body.get("value");
        if (value == null)
            return ResponseEntity.badRequest().body(Map.of("error", "Missing 'value' in request body"));
        try {
            return ResponseEntity.ok(service.insert(value));
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
            return ResponseEntity.ok(service.remove(value));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clear() {
        service.clear();
        return ResponseEntity.ok(Map.of("message", "Tree cleared successfully."));
    }

    @GetMapping("/contains/{value}")
    public ResponseEntity<Map<String, Object>> contains(@PathVariable Integer value) {
        return ResponseEntity.ok(service.contains(value));
    }

    @GetMapping("/level/{value}")
    public ResponseEntity<?> getLevel(@PathVariable Integer value) {
        try {
            return ResponseEntity.ok(service.getLevel(value));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/traversal/{type}")
    public ResponseEntity<?> traversal(@PathVariable String type) {
        try {
            return ResponseEntity.ok(service.traversal(type));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}