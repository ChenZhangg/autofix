package fdse.zc.gumtree.java;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class TreeNodeMap {
    private TIntObjectMap<JavaTree> map;

    public TreeNodeMap(JavaTree treeNode) {
        this();
        putTreeNodes(treeNode);
    }

    public TreeNodeMap() {
        map = new TIntObjectHashMap<>();
    }

    public JavaTree getTreeNode(int id) {
        return map.get(id);
    }

    public boolean contains(JavaTree treeNode) {
        return contains(treeNode.getId());
    }

    public boolean contains(int id) {
        return map.containsKey(id);
    }

    public void putTreeNodes(JavaTree treeNode) {
        for (JavaTree t: treeNode.getPreOrderTreeNodeList())
            map.put(t.getId(), t);
    }

    public void putTreeNode(JavaTree treeNode) {
        map.put(treeNode.getId(), treeNode);
    }
}
