package fdse.zc.gumtree;

public class Update extends Action{
    private ITree node;
    private String label;
    public Update(ITree node, String label){
        this.node = node;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Update " + node + "of type " + node.getNodeTypeNumber() +"'s label to " + label;
    }
}
