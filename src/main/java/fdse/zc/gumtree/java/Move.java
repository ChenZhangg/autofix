package fdse.zc.gumtree.java;

public class Move extends Action {
    private JavaTree node;
    private JavaTree parent;
    private int pos;
    public Move(JavaTree node, JavaTree parent, int pos){
        this.node = node;
        this.parent = parent;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Move " + node + " under " + parent + "at" + pos;
    }
}
