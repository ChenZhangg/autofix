package fdse.zc.gumtree.java;

import java.util.*;

public class MultiMappingStore implements Iterable<Mapping> {
    private Map<TreeNode, Set<TreeNode>> oldMap;

    private Map<TreeNode, Set<TreeNode>> newMap;

    public MultiMappingStore(Set<Mapping> mappingSet) {
        this();
        for (Mapping m: mappingSet){
            link(m.getFirst(), m.getSecond());
        }
    }

    public MultiMappingStore() {
        oldMap = new HashMap<>();
        newMap = new HashMap<>();
    }

    public Set<Mapping> getMappings() {
        Set<Mapping> mappingSet = new HashSet<>();
        for (TreeNode oldNode : oldMap.keySet()) {
            for (TreeNode newNode : oldMap.get(oldNode)) {
                mappingSet.add(new Mapping(oldNode, newNode));
            }
        }
        return mappingSet;
    }

    public void link(TreeNode oldNode, TreeNode newNode) {
        if (!oldMap.containsKey(oldNode)){
            oldMap.put(oldNode, new HashSet<TreeNode>());
        }
        oldMap.get(oldNode).add(newNode);

        if (!newMap.containsKey(newNode)){
            newMap.put(newNode, new HashSet<TreeNode>());
        }
        newMap.get(newNode).add(oldNode);
    }

    public void unlink(TreeNode oldNode, TreeNode newNode) {
        oldMap.get(oldNode).remove(newNode);
        newMap.get(newNode).remove(oldNode);
    }

    public Set<TreeNode> getNewNodeSet(TreeNode oldNode) {
        return oldMap.get(oldNode);
    }

    public Set<TreeNode> getOldMapKeySet() {
        return oldMap.keySet();
    }

    public Set<TreeNode> getNewMapKeySet() {
        return newMap.keySet();
    }

    public Set<TreeNode> getOldNodeSet(TreeNode newNode) {
        return newMap.get(newNode);
    }

    public boolean hasSrc(TreeNode src) {
        return oldMap.containsKey(src);
    }

    public boolean hasDst(TreeNode dst) {
        return newMap.containsKey(dst);
    }

    public boolean has(TreeNode src, TreeNode dst) {
        return oldMap.get(src).contains(dst);
    }

    public boolean isMappingUnique(TreeNode oldNode) {
        return oldMap.get(oldNode).size() == 1 && newMap.get(oldMap.get(oldNode).iterator().next()).size() == 1;
    }

    @Override
    public Iterator<Mapping> iterator() {
        return getMappings().iterator();
    }

}
