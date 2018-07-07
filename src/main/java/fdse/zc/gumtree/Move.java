package fdse.zc.gumtree;

public class Move extends Action {
    private TreeNode node;
    private TreeNode parent;
    private int pos;
    public Move(TreeNode node, TreeNode parent, int pos){
        this.node = node;
        this.parent = parent;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Move " + node + " under " + parent + "at" + pos;
    }
}
