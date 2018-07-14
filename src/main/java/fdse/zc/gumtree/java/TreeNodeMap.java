package fdse.zc.gumtree.java;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class TreeNodeMap {
    private TIntObjectMap<TreeNode> map;

    public TreeNodeMap(TreeNode treeNode) {
        this();
        putTreeNodes(treeNode);
    }

    public TreeNodeMap() {
        map = new TIntObjectHashMap<>();
    }

    public TreeNode getTreeNode(int id) {
        return map.get(id);
    }

    public boolean contains(TreeNode treeNode) {
        return contains(treeNode.getId());
    }

    public boolean contains(int id) {
        return map.containsKey(id);
    }

    public void putTreeNodes(TreeNode treeNode) {
        for (TreeNode t: treeNode.getPreOrderTreeNodeList())
            map.put(t.getId(), t);
    }

    public void putTreeNode(TreeNode treeNode) {
        map.put(treeNode.getId(), treeNode);
    }
}
