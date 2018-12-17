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
        return "Update " + node + "of type " + node.toShortString() +"'s label to " + label;
    }

    @Override
    public ITree getNode() {
      return node;
    }

    public String getLabel() {
      return label;
    }
}
