package fdse.zc.gumtree;

public class Delete extends Action {
    private ITree node;
    public Delete(ITree node){
        this.node = node;
    }

    @Override
    public String toString() {
        return "Delete " + node;
    }
}
