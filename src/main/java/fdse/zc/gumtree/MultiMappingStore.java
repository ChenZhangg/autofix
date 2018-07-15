package fdse.zc.gumtree.java;

import java.util.*;

public class MultiMappingStore implements Iterable<Mapping> {
    private Map<JavaTree, Set<JavaTree>> oldMap;

    private Map<JavaTree, Set<JavaTree>> newMap;

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
        for (JavaTree oldNode : oldMap.keySet()) {
            for (JavaTree newNode : oldMap.get(oldNode)) {
                mappingSet.add(new Mapping(oldNode, newNode));
            }
        }
        return mappingSet;
    }

    public void link(JavaTree oldNode, JavaTree newNode) {
        if (!oldMap.containsKey(oldNode)){
            oldMap.put(oldNode, new HashSet<JavaTree>());
        }
        oldMap.get(oldNode).add(newNode);

        if (!newMap.containsKey(newNode)){
            newMap.put(newNode, new HashSet<JavaTree>());
        }
        newMap.get(newNode).add(oldNode);
    }

    public void unlink(JavaTree oldNode, JavaTree newNode) {
        oldMap.get(oldNode).remove(newNode);
        newMap.get(newNode).remove(oldNode);
    }

    public Set<JavaTree> getNewNodeSet(JavaTree oldNode) {
        return oldMap.get(oldNode);
    }

    public Set<JavaTree> getOldMapKeySet() {
        return oldMap.keySet();
    }

    public Set<JavaTree> getNewMapKeySet() {
        return newMap.keySet();
    }

    public Set<JavaTree> getOldNodeSet(JavaTree newNode) {
        return newMap.get(newNode);
    }

    public boolean hasSrc(JavaTree src) {
        return oldMap.containsKey(src);
    }

    public boolean hasDst(JavaTree dst) {
        return newMap.containsKey(dst);
    }

    public boolean has(JavaTree src, JavaTree dst) {
        return oldMap.get(src).contains(dst);
    }

    public boolean isMappingUnique(JavaTree oldNode) {
        return oldMap.get(oldNode).size() == 1 && newMap.get(oldMap.get(oldNode).iterator().next()).size() == 1;
    }

    @Override
    public Iterator<Mapping> iterator() {
        return getMappings().iterator();
    }

}
