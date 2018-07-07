package fdse.zc.gumtree;

import java.util.*;

public class MappingStore {
    private Map<TreeNode, TreeNode> oldMap;
    private Map<TreeNode, TreeNode> newMap;

    public MappingStore() {
        oldMap = new HashMap<>();
        newMap = new HashMap<>();
    }

    public MappingStore(Set<Mapping> mappings) {
        this();
        for (Mapping m: mappings) link(m.getFirst(), m.getSecond());
    }

    public void link(TreeNode oldNode, TreeNode newNode) {
        oldMap.put(oldNode, newNode);
        newMap.put(newNode, oldNode);
    }

    public TreeNode getNewTreeNode(TreeNode oldNode){
        return oldMap.get(oldNode);
    }
    public TreeNode getOldTreeNode(TreeNode newNode){
        return newMap.get(newNode);
    }

    public boolean has(TreeNode oldNode, TreeNode newNode){
        return oldMap.get(oldNode) == newNode;
    }

    public boolean hasNewTreeNode(TreeNode node){
        return newMap.containsKey(node);
    }

    public boolean hasOldTreeNode(TreeNode node){
        return oldMap.containsKey(node);
    }

    public MappingStore copy() {
        return new MappingStore(asSet());
    }

    public Set<Mapping> asSet() {
        return new AbstractSet<Mapping>() {

            @Override
            public Iterator<Mapping> iterator() {
                Iterator<TreeNode> it = oldMap.keySet().iterator();
                return new Iterator<Mapping>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Mapping next() {
                        TreeNode old = it.next();
                        if (old == null) return null;
                        return new Mapping(old, oldMap.get(old));
                    }
                };
            }

            @Override
            public int size() {
                return oldMap.keySet().size();
            }
        };
    }
}
