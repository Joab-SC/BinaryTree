package co.edu.uniquindio.binarytree.controller;


import co.edu.uniquindio.binarytree.model.Step;
import co.edu.uniquindio.binarytree.model.TreeNode;
import co.edu.uniquindio.binarytree.service.BSTService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bst")
@CrossOrigin(origins = "*")  // En producción pondrías solo tu dominio
public class BSTController {

    private final BSTService bstService;


    public BSTController(BSTService bstService) {
        this.bstService = bstService;
    }


    @GetMapping("/tree")
    public ResponseEntity<TreeNode> getTree() {
        return ResponseEntity.ok(bstService.getTree());
    }


    @PostMapping("/insert/{value}")
    public ResponseEntity<List<Step>> insert(@PathVariable int value) {
        List<Step> steps = bstService.insert(value);
        return ResponseEntity.ok(steps);
    }


    @DeleteMapping("/delete/{value}")
    public ResponseEntity<List<Step>> delete(@PathVariable int value) {
        List<Step> steps = bstService.delete(value);
        return ResponseEntity.ok(steps);
    }


    @DeleteMapping("/reset")
    public ResponseEntity<String> reset() {
        bstService.reset();
        return ResponseEntity.ok("Árbol reiniciado.");
    }
}
