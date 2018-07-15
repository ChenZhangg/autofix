package fdse.zc.gumtree.java;

public class Insert extends Action{
    private JavaTree node;
    private JavaTree parent;
    private int pos;
    public Insert(JavaTree node, JavaTree parent, int pos){
        this.node = node;
        this.parent = parent;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Insert " + node + " under " + parent + "at" + pos;
    }
}
