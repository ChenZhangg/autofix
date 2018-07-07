package fdse.zc.gumtree;

import java.util.HashMap;
import java.util.Map;

public class TreeContext {

    private Map<Integer, String> typeNameMap = new HashMap<>();

    private TreeNode root;

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {return root;}
    public TreeNode createTreeNode(int nodeTypeNumber, String nodeTypeName, String nodeLabel, int startPosition, int length) {
        registerTypeName(nodeTypeNumber, nodeTypeName);
        return new TreeNode(nodeTypeNumber, nodeTypeName, nodeLabel, startPosition, length);
    }

    protected void registerTypeName(int nodeTypeNumber, String nodeTypeName) {
        if (nodeTypeName == null || nodeTypeName.equals(""))
            return;
        String tempTypeName = typeNameMap.get(nodeTypeNumber);
        if (tempTypeName == null)
            typeNameMap.put(nodeTypeNumber, nodeTypeName);
    }


}
