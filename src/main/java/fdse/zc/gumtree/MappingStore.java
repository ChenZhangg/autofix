package fdse.zc.gumtree.java;

import java.util.*;

public class MappingStore {
    private Map<JavaTree, JavaTree> oldMap;
    private Map<JavaTree, JavaTree> newMap;

    public MappingStore() {
        oldMap = new HashMap<>();
        newMap = new HashMap<>();
    }

    public MappingStore(Set<Mapping> mappings) {
        this();
        for (Mapping m: mappings) link(m.getFirst(), m.getSecond());
    }

    public void link(JavaTree oldNode, JavaTree newNode) {
        oldMap.put(oldNode, newNode);
        newMap.put(newNode, oldNode);
    }

    public JavaTree getNewTreeNode(JavaTree oldNode){
        return oldMap.get(oldNode);
    }
    public JavaTree getOldTreeNode(JavaTree newNode){
        return newMap.get(newNode);
    }

    public boolean has(JavaTree oldNode, JavaTree newNode){
        return oldMap.get(oldNode) == newNode;
    }

    public boolean hasNewTreeNode(JavaTree node){
        return newMap.containsKey(node);
    }

    public boolean hasOldTreeNode(JavaTree node){
        return oldMap.containsKey(node);
    }

    public MappingStore copy() {
        return new MappingStore(asSet());
    }

    public Set<Mapping> asSet() {
        return new AbstractSet<Mapping>() {

            @Override
            public Iterator<Mapping> iterator() {
                Iterator<JavaTree> it = oldMap.keySet().iterator();
                return new Iterator<Mapping>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Mapping next() {
                        JavaTree old = it.next();
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
