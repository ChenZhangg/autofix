package fdse.zc.gumtree.java;

import java.util.HashMap;
import java.util.Map;

public class TreeContext {

    private Map<Integer, String> typeNameMap = new HashMap<>();

    private JavaTree root;

    public void setRoot(JavaTree root) {
        this.root = root;
    }

    public JavaTree getRoot() {return root;}
    public JavaTree createTreeNode(int nodeTypeNumber, String nodeTypeName, String nodeLabel, int startPosition, int length) {
        registerTypeName(nodeTypeNumber, nodeTypeName);
        return new JavaTree(nodeTypeNumber, nodeTypeName, nodeLabel, startPosition, length);
    }

    protected void registerTypeName(int nodeTypeNumber, String nodeTypeName) {
        if (nodeTypeName == null || nodeTypeName.equals(""))
            return;
        String tempTypeName = typeNameMap.get(nodeTypeNumber);
        if (tempTypeName == null)
            typeNameMap.put(nodeTypeNumber, nodeTypeName);
    }


}
