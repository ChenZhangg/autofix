package fdse.zc.gumtree;

import java.util.*;

public class MappingStore {
    private Map<ITree, ITree> oldMap;
    private Map<ITree, ITree> newMap;

    public MappingStore() {
        oldMap = new HashMap<>();
        newMap = new HashMap<>();
    }

    public MappingStore(Set<Mapping> mappings) {
        this();
        for (Mapping m: mappings) link(m.getFirst(), m.getSecond());
    }

    public void link(ITree oldNode, ITree newNode) {
        oldMap.put(oldNode, newNode);
        newMap.put(newNode, oldNode);
    }

    public ITree getNewTreeNode(ITree oldNode){
        return oldMap.get(oldNode);
    }
    public ITree getOldTreeNode(ITree newNode){
        return newMap.get(newNode);
    }

    public boolean has(ITree oldNode, ITree newNode){
        return oldMap.get(oldNode) == newNode;
    }

    public boolean hasNewTreeNode(ITree node){
        return newMap.containsKey(node);
    }

    public boolean hasOldTreeNode(ITree node){
        return oldMap.containsKey(node);
    }

    public MappingStore copy() {
        return new MappingStore(asSet());
    }

    public Set<Mapping> asSet() {
        return new AbstractSet<Mapping>() {

            @Override
            public Iterator<Mapping> iterator() {
                Iterator<ITree> it = oldMap.keySet().iterator();
                return new Iterator<Mapping>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Mapping next() {
                        ITree old = it.next();
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
