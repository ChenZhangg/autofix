package fdse.zc.gumtree.java;

public class Update extends Action{
    private TreeNode node;
    private String label;
    public Update(TreeNode node, String label){
        this.node = node;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Update " + node + "'s label to " + label;
    }
}
