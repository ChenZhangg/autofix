package fdse.zc.gumtree.java;

import java.util.*;

public class GreedySubtreeMatcher extends Matcher{
    public GreedySubtreeMatcher(JavaTree oldRoot, JavaTree newRoot, MappingStore mappingStore) {
        super(oldRoot, newRoot, mappingStore);
    }

    private void popHigherTree(HeightIndexedPriorityList oldList, HeightIndexedPriorityList newList) {
        if (oldList.peekMax() > newList.peekMax())
            oldList.open();
        if(oldList.peekMax() < newList.peekMax())
            newList.open();
    }

    public void match(){
        MultiMappingStore multiMappingStore = new MultiMappingStore();

        HeightIndexedPriorityList oldHeightIndexedPriorityList = new HeightIndexedPriorityList(oldRoot);
        HeightIndexedPriorityList newHeightIndexedPriorityList = new HeightIndexedPriorityList(newRoot);

        while (oldHeightIndexedPriorityList.peekMax() != -1 && newHeightIndexedPriorityList.peekMax() != -1) {
            while (oldHeightIndexedPriorityList.peekMax() != newHeightIndexedPriorityList.peekMax()) {
                popHigherTree(oldHeightIndexedPriorityList, newHeightIndexedPriorityList);
            }

            ArrayList<JavaTree> oldCurrentHeightNodeList = oldHeightIndexedPriorityList.pop();
            ArrayList<JavaTree> newCurrentHeightNodeList = newHeightIndexedPriorityList.pop();

            boolean[] marksForOldNodeList = new boolean[oldCurrentHeightNodeList.size()];
            boolean[] marksForNewNodeList = new boolean[newCurrentHeightNodeList.size()];

            for (int i = 0; i < oldCurrentHeightNodeList.size(); i++) {
                for (int j = 0; j < newCurrentHeightNodeList.size(); j++) {
                    JavaTree oldNode = oldCurrentHeightNodeList.get(i);
                    JavaTree newNode = newCurrentHeightNodeList.get(j);

                    if (oldNode.isIsomorphicTo(newNode)) {
                        multiMappingStore.link(oldNode, newNode);
                        marksForOldNodeList[i] = true;
                        marksForNewNodeList[j] = true;
                    }
                }
            }

            for (int i = 0; i < marksForOldNodeList.length; i++) {
                if (marksForOldNodeList[i] == false) {
                    oldHeightIndexedPriorityList.open(oldCurrentHeightNodeList.get(i));
                }
            }
            for (int j = 0; j < marksForNewNodeList.length; j++) {
                if (marksForNewNodeList[j] == false) {
                    newHeightIndexedPriorityList.open(newCurrentHeightNodeList.get(j));
                }
            }
            oldHeightIndexedPriorityList.updateHeight();
            newHeightIndexedPriorityList.updateHeight();
        }

        filterMappings(multiMappingStore);
    }

    public void filterMappings(MultiMappingStore multiMappingStore) {
        // Select unique mappingStore first and extract ambiguous mappingStore.
        ArrayList<Mapping> ambiguousList = new ArrayList<>();
        HashSet<JavaTree> ignored = new HashSet<>();
        for (JavaTree old : multiMappingStore.getOldMapKeySet()) {
            if (multiMappingStore.isMappingUnique(old)) {
                addMappingRecursively(old, multiMappingStore.getNewNodeSet(old).iterator().next());
            } else if (!ignored.contains(old)) {
                Set<JavaTree> aNews = multiMappingStore.getNewNodeSet(old);
                Set<JavaTree> aOlds = multiMappingStore.getOldNodeSet(multiMappingStore.getNewNodeSet(old).iterator().next());
                for (JavaTree aold : aOlds) {
                    for (JavaTree anew : aNews) {
                        ambiguousList.add(new Mapping(aold, anew));
                    }
                }
                ignored.addAll(aOlds);
            }
        }

        // Rank the mappingStore by score.
        Set<JavaTree> oldIgnored = new HashSet<>();
        Set<JavaTree> newIgnored = new HashSet<>();
        Collections.sort(ambiguousList, new MappingComparator(ambiguousList, mappingStore, Math.max(oldRoot.getSize(), newRoot.getSize())));

        // Select the best ambiguous mappingStore
        retainBestMapping(ambiguousList, oldIgnored, newIgnored);
    }

    protected void retainBestMapping(ArrayList<Mapping> ambiguousList, Set<JavaTree> oldIgnored, Set<JavaTree> newIgnored) {
        while (ambiguousList.size() > 0) {
            Mapping mapping = ambiguousList.remove(0);
            if (!(oldIgnored.contains(mapping.getFirst()) || newIgnored.contains(mapping.getSecond()))) {
                addMappingRecursively(mapping.getFirst(), mapping.getSecond());
                oldIgnored.add(mapping.getFirst());
                newIgnored.add(mapping.getSecond());
            }
        }
    }
}
