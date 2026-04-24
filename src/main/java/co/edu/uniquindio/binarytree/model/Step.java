package co.edu.uniquindio.binarytree.model;

public class Step {
    private String action;
    private Integer highlightedNode;
    private String message;
    private TreeNode treeSnapshot;

    public Step(String action, Integer highlightedNode, String message, TreeNode treeSnapshot) {
        this.action = action;
        this.highlightedNode = highlightedNode;
        this.message = message;
        this.treeSnapshot = treeSnapshot;
    }


    public String getAction() { return action; }
    public Integer getHighlightedNode() { return highlightedNode; }
    public String getMessage() { return message; }
    public TreeNode getTreeSnapshot() { return treeSnapshot; }
}
