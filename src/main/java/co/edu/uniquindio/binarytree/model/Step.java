package co.edu.uniquindio.binarytree.model;

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

    public String getAction() { return action; }
    public Integer getHighlightedNode() { return highlightedNode; }
    public String getMessage() { return message; }
    public TreeNodeDTO<T> getTreeSnapshot() { return treeSnapshot; }
}