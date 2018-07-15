package fdse.zc.gumtree;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class TreeNodeMap {
    private TIntObjectMap<ITree> map;

    public TreeNodeMap(ITree treeNode) {
        this();
        putTreeNodes(treeNode);
    }

    public TreeNodeMap() {
        map = new TIntObjectHashMap<>();
    }

    public ITree getTreeNode(int id) {
        return map.get(id);
    }

    public boolean contains(ITree treeNode) {
        return contains(treeNode.getId());
    }

    public boolean contains(int id) {
        return map.containsKey(id);
    }

    public void putTreeNodes(ITree treeNode) {
        for (ITree t: treeNode.getPreOrderTreeNodeList())
            map.put(t.getId(), t);
    }

    public void putTreeNode(ITree treeNode) {
        map.put(treeNode.getId(), treeNode);
    }
}
