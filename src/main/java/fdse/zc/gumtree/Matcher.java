package fdse.zc.gumtree;

import java.util.ArrayList;

public abstract class Matcher {
    public static int MIN_HEIGHT = 1;

    protected final ITree oldRoot;
    protected final ITree newRoot;
    protected final MappingStore mappingStore;

    public Matcher(ITree oldRoot, ITree newRoot, MappingStore mappingStore) {
        this.oldRoot = oldRoot;
        this.newRoot = newRoot;
        this.mappingStore = mappingStore;
    }

    protected void addMapping(ITree oldNode, ITree newNode) {
        mappingStore.link(oldNode, newNode);
    }

    protected void addMappingRecursively(ITree oldNode, ITree newNode) {
        ArrayList<ITree> oldTreeNodeList = oldNode.getPreOrderTreeNodeList();
        ArrayList<ITree> newTreeNodeList = newNode.getPreOrderTreeNodeList();
        for (int i = 0; i < oldTreeNodeList.size(); i++) {
            ITree currentOldTreeNode = oldTreeNodeList.get(i);
            ITree currentNewTreeNode = newTreeNodeList.get(i);
            addMapping(currentOldTreeNode, currentNewTreeNode);
        }
    }

    public MappingStore getMappings() {
        return mappingStore;
    }

    public abstract void match();

}
