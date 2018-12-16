package fdse.zc.gumtree;

public class Move extends Action {
    private ITree node;
    private ITree parent;
    private int pos;
    public Move(ITree node, ITree parent, int pos){
        this.node = node;
        this.parent = parent;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Move " + node + " under " + parent + "at" + pos;
    }

    @Override
    public ITree getNode() {
      return node;
    }
}
