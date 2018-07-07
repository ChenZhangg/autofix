public class Delete extends Action {
    private TreeNode node;
    public Delete(TreeNode node){
        this.node = node;
    }

    @Override
    public String toString() {
        return "Delete " + node;
    }
}
