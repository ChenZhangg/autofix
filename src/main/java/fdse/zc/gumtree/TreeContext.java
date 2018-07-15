package fdse.zc.gumtree;

import java.util.HashMap;
import java.util.Map;

import fdse.zc.gumtree.java.JavaTree;

public class TreeContext {

    private Map<Integer, String> typeNameMap = new HashMap<>();

    private ITree root;

    public void setRoot(ITree root) {
        this.root = root;
    }

    public ITree getRoot() {return root;}
    public ITree createTree(int nodeTypeNumber, String nodeTypeName, String nodeLabel, int startPosition, int length, int startLineNumber, int endLineNumber) {
        registerTypeName(nodeTypeNumber, nodeTypeName);
        return new JavaTree(nodeTypeNumber, nodeTypeName, nodeLabel, startPosition, length, startLineNumber, endLineNumber);
    }

    protected void registerTypeName(int nodeTypeNumber, String nodeTypeName) {
        if (nodeTypeName == null || nodeTypeName.equals(""))
            return;
        String tempTypeName = typeNameMap.get(nodeTypeNumber);
        if (tempTypeName == null)
            typeNameMap.put(nodeTypeNumber, nodeTypeName);
    }


}
