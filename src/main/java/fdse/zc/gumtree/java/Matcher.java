package fdse.zc.gumtree.java;

import java.util.ArrayList;

public abstract class Matcher {
    public static int MIN_HEIGHT = 1;

    protected final JavaTree oldRoot;
    protected final JavaTree newRoot;
    protected final MappingStore mappingStore;

    public Matcher(JavaTree oldRoot, JavaTree newRoot, MappingStore mappingStore) {
        this.oldRoot = oldRoot;
        this.newRoot = newRoot;
        this.mappingStore = mappingStore;
    }

    protected void addMapping(JavaTree oldNode, JavaTree newNode) {
        mappingStore.link(oldNode, newNode);
    }

    protected void addMappingRecursively(JavaTree oldNode, JavaTree newNode) {
        ArrayList<JavaTree> oldTreeNodeList = oldNode.getPreOrderTreeNodeList();
        ArrayList<JavaTree> newTreeNodeList = newNode.getPreOrderTreeNodeList();
        for (int i = 0; i < oldTreeNodeList.size(); i++) {
            JavaTree currentOldTreeNode = oldTreeNodeList.get(i);
            JavaTree currentNewTreeNode = newTreeNodeList.get(i);
            addMapping(currentOldTreeNode, currentNewTreeNode);
        }
    }

    public MappingStore getMappings() {
        return mappingStore;
    }

    public abstract void match();

}
