package fdse.zc.gumtree;

import java.util.*;

public class MappingComparator implements Comparator<Mapping> {
    private List<Mapping> ambiguousList;

    private int maxTreeSize;

    private MappingStore mappingStore;

    protected Map<Mapping, Double> similarityMap = new HashMap<>();

    private Map<ITree, List<ITree>> oldDescendants = new HashMap<>();

    private Map<ITree, Set<ITree>> newDescendants = new HashMap<>();

    public MappingComparator(ArrayList<Mapping> ambiguousList, MappingStore mappingStore, int maxTreeSize){
        this.ambiguousList = ambiguousList;
        this.mappingStore = mappingStore;
        this.maxTreeSize = maxTreeSize;
        for (Mapping mapping: ambiguousList)
            similarityMap.put(mapping, similarity(mapping.getFirst(), mapping.getSecond()));
    }

    @Override
    public int compare(Mapping m1, Mapping m2) {
        if (similarityMap.get(m2).compareTo(similarityMap.get(m1)) != 0) {
            return Double.compare(similarityMap.get(m2), similarityMap.get(m1));
        }
        if (m1.getFirst().getId() != m2.getFirst().getId()) {
            return Integer.compare(m1.getFirst().getId(), m2.getFirst().getId());
        }
        return Integer.compare(m1.getSecond().getId(), m2.getSecond().getId());
    }

    protected double similarity(ITree oldNode, ITree newNode) {
        return 100D * siblingsJaccardSimilarity(oldNode.getParent(), newNode.getParent())
                +  10D * posInParentSimilarity(oldNode, newNode) + numberingSimilarity(oldNode, newNode);
    }

    protected double siblingsJaccardSimilarity(ITree oldNode, ITree newNode) {
        double num = (double) numberOfCommonDescendants(oldNode, newNode);
        double den = (double) oldDescendants.get(oldNode).size() + (double) newDescendants.get(newNode).size() - num;
        return num / den;
    }

    protected int numberOfCommonDescendants(ITree oldNode, ITree newNode) {
        if (!oldDescendants.containsKey(oldNode)) {
            oldDescendants.put(oldNode, oldNode.getDescendants());
        }
        if (!newDescendants.containsKey(newNode)) {
            newDescendants.put(newNode, new HashSet<>(newNode.getDescendants()));
        }

        int common = 0;

        for (ITree t: oldDescendants.get(oldNode)) {
            ITree m = mappingStore.getNewTreeNode(t);
            if (m != null && newDescendants.get(newNode).contains(m)) {
                common++;
            }
        }

        return common;
    }

    protected double posInParentSimilarity(ITree oldNode, ITree newNode) {
        int posOld = (oldNode.isRoot()) ? 0 : oldNode.getParent().getChildPosition(oldNode);
        int posNew = (newNode.isRoot()) ? 0 : newNode.getParent().getChildPosition(newNode);
        int maxOldPos =  (oldNode.isRoot()) ? 1 : oldNode.getParent().getChildren().size();
        int maxNewPos =  (newNode.isRoot()) ? 1 : newNode.getParent().getChildren().size();
        int maxPosDiff = Math.max(maxOldPos, maxNewPos);
        return 1D - ((double) Math.abs(posOld - posNew) / (double) maxPosDiff);
    }

    protected double numberingSimilarity(ITree oldNode, ITree newNode) {
        return 1D - ((double) Math.abs(oldNode.getId() - newNode.getId())
                / (double) maxTreeSize);
    }
}
