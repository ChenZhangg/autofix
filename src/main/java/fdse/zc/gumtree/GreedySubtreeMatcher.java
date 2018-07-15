package fdse.zc.gumtree;

import java.util.*;

public class GreedySubtreeMatcher extends Matcher{
    public GreedySubtreeMatcher(ITree oldRoot, ITree newRoot, MappingStore mappingStore) {
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

            ArrayList<ITree> oldCurrentHeightNodeList = oldHeightIndexedPriorityList.pop();
            ArrayList<ITree> newCurrentHeightNodeList = newHeightIndexedPriorityList.pop();

            boolean[] marksForOldNodeList = new boolean[oldCurrentHeightNodeList.size()];
            boolean[] marksForNewNodeList = new boolean[newCurrentHeightNodeList.size()];

            for (int i = 0; i < oldCurrentHeightNodeList.size(); i++) {
                for (int j = 0; j < newCurrentHeightNodeList.size(); j++) {
                    ITree oldNode = oldCurrentHeightNodeList.get(i);
                    ITree newNode = newCurrentHeightNodeList.get(j);

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
        HashSet<ITree> ignored = new HashSet<>();
        for (ITree old : multiMappingStore.getOldMapKeySet()) {
            if (multiMappingStore.isMappingUnique(old)) {
                addMappingRecursively(old, multiMappingStore.getNewNodeSet(old).iterator().next());
            } else if (!ignored.contains(old)) {
                Set<ITree> aNews = multiMappingStore.getNewNodeSet(old);
                Set<ITree> aOlds = multiMappingStore.getOldNodeSet(multiMappingStore.getNewNodeSet(old).iterator().next());
                for (ITree aold : aOlds) {
                    for (ITree anew : aNews) {
                        ambiguousList.add(new Mapping(aold, anew));
                    }
                }
                ignored.addAll(aOlds);
            }
        }

        // Rank the mappingStore by score.
        Set<ITree> oldIgnored = new HashSet<>();
        Set<ITree> newIgnored = new HashSet<>();
        Collections.sort(ambiguousList, new MappingComparator(ambiguousList, mappingStore, Math.max(oldRoot.getSize(), newRoot.getSize())));

        // Select the best ambiguous mappingStore
        retainBestMapping(ambiguousList, oldIgnored, newIgnored);
    }

    protected void retainBestMapping(ArrayList<Mapping> ambiguousList, Set<ITree> oldIgnored, Set<ITree> newIgnored) {
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
