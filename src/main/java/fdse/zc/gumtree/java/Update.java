package fdse.zc.gumtree.java;

public class Update extends Action{
    private JavaTree node;
    private String label;
    public Update(JavaTree node, String label){
        this.node = node;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Update " + node + "'s label to " + label;
    }
}
