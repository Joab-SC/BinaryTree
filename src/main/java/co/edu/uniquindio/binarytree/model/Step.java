package co.edu.uniquindio.binarytree.model;

import lombok.Getter;

@Getter
public class Step<T extends Comparable<T>> {

    private String action;
    private Integer highlightedNode;
    private String message;
    private TreeNodeDTO<T> treeSnapshot;

    public Step(String action, Integer highlightedNode, String message, TreeNodeDTO<T> treeSnapshot) {
        this.action = action;
        this.highlightedNode = highlightedNode;
        this.message = message;
        this.treeSnapshot = treeSnapshot;
    }

}