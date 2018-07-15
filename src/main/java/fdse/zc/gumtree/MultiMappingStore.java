package fdse.zc.gumtree;

import java.util.*;

public class MultiMappingStore implements Iterable<Mapping> {
    private Map<ITree, Set<ITree>> oldMap;

    private Map<ITree, Set<ITree>> newMap;

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
        for (ITree oldNode : oldMap.keySet()) {
            for (ITree newNode : oldMap.get(oldNode)) {
                mappingSet.add(new Mapping(oldNode, newNode));
            }
        }
        return mappingSet;
    }

    public void link(ITree oldNode, ITree newNode) {
        if (!oldMap.containsKey(oldNode)){
            oldMap.put(oldNode, new HashSet<ITree>());
        }
        oldMap.get(oldNode).add(newNode);

        if (!newMap.containsKey(newNode)){
            newMap.put(newNode, new HashSet<ITree>());
        }
        newMap.get(newNode).add(oldNode);
    }

    public void unlink(ITree oldNode, ITree newNode) {
        oldMap.get(oldNode).remove(newNode);
        newMap.get(newNode).remove(oldNode);
    }

    public Set<ITree> getNewNodeSet(ITree oldNode) {
        return oldMap.get(oldNode);
    }

    public Set<ITree> getOldMapKeySet() {
        return oldMap.keySet();
    }

    public Set<ITree> getNewMapKeySet() {
        return newMap.keySet();
    }

    public Set<ITree> getOldNodeSet(ITree newNode) {
        return newMap.get(newNode);
    }

    public boolean hasSrc(ITree src) {
        return oldMap.containsKey(src);
    }

    public boolean hasDst(ITree dst) {
        return newMap.containsKey(dst);
    }

    public boolean has(ITree src, ITree dst) {
        return oldMap.get(src).contains(dst);
    }

    public boolean isMappingUnique(ITree oldNode) {
        return oldMap.get(oldNode).size() == 1 && newMap.get(oldMap.get(oldNode).iterator().next()).size() == 1;
    }

    @Override
    public Iterator<Mapping> iterator() {
        return getMappings().iterator();
    }

}
