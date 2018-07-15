package fdse.zc.gumtree.java;

public class Delete extends Action {
    private JavaTree node;
    public Delete(JavaTree node){
        this.node = node;
    }

    @Override
    public String toString() {
        return "Delete " + node;
    }
}
