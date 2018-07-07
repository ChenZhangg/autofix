package fdse.zc.gumtree;

import java.util.ArrayList;

public abstract class Matcher {
    public static int MIN_HEIGHT = 1;

    protected final TreeNode oldRoot;
    protected final TreeNode newRoot;
    protected final MappingStore mappingStore;

    public Matcher(TreeNode oldRoot, TreeNode newRoot, MappingStore mappingStore) {
        this.oldRoot = oldRoot;
        this.newRoot = newRoot;
        this.mappingStore = mappingStore;
    }

    protected void addMapping(TreeNode oldNode, TreeNode newNode) {
        mappingStore.link(oldNode, newNode);
    }

    protected void addMappingRecursively(TreeNode oldNode, TreeNode newNode) {
        ArrayList<TreeNode> oldTreeNodeList = oldNode.getPreOrderTreeNodeList();
        ArrayList<TreeNode> newTreeNodeList = newNode.getPreOrderTreeNodeList();
        for (int i = 0; i < oldTreeNodeList.size(); i++) {
            TreeNode currentOldTreeNode = oldTreeNodeList.get(i);
            TreeNode currentNewTreeNode = newTreeNodeList.get(i);
            addMapping(currentOldTreeNode, currentNewTreeNode);
        }
    }

    public MappingStore getMappings() {
        return mappingStore;
    }

    public abstract void match();

}
