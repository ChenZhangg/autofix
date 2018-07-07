public class Insert extends Action{
    private TreeNode node;
    private TreeNode parent;
    private int pos;
    public Insert(TreeNode node, TreeNode parent, int pos){
        this.node = node;
        this.parent = parent;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Insert " + node + " under " + parent + "at" + pos;
    }
}
