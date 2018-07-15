package fdse.zc.gumtree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GreedyBottomUpMatcher extends Matcher{

    protected TreeNodeMap srcIds;
    protected TreeNodeMap dstIds;

    protected TreeNodeMap mappedOld;
    protected TreeNodeMap mappedNew;

    public GreedyBottomUpMatcher(ITree oldRoot, ITree newRoot, MappingStore mappingStore) {
        super(oldRoot, newRoot, mappingStore);
        srcIds = new TreeNodeMap(oldRoot);
        dstIds = new TreeNodeMap(newRoot);
        mappedOld = new TreeNodeMap();
        mappedNew = new TreeNodeMap();
        for (Mapping m : mappingStore.asSet()) {
            mappedOld.putTreeNodes(m.getFirst());
            mappedNew.putTreeNodes(m.getSecond());
        }
    }

    public void match() {
        for (ITree t: oldRoot.getPostOrderTreeNodeList())  {
            if (t.isRoot()) {
                addMapping(t, newRoot);
                lastChanceMatch(t, newRoot);
                break;
            } else if (!(isOldMatched(t) || t.isLeaf())) {

                List<ITree> candidates = getNewCandidates(t);
                ITree best = null;
                double max = -1D;

                for (ITree cand: candidates) {
                    double sim = jaccardSimilarity(t, cand);
                    if (sim > max && sim >= 0.5) {
                        max = sim;
                        best = cand;
                    }
                }
                if (best != null) {
                    lastChanceMatch(t, best);
                    addMapping(t, best);
                }
            }
        }
    }
    protected List<ITree> getNewCandidates(ITree old) {
        List<ITree> seeds = new ArrayList<>();
        for (ITree c: old.getDescendants()) {
            ITree m = mappingStore.getNewTreeNode(c);
            if (m != null) seeds.add(m);
        }
        List<ITree> candidates = new ArrayList<>();
        Set<ITree> visited = new HashSet<>();
        for (ITree seed: seeds) {
            while (seed.getParent() != null) {
                ITree parent = seed.getParent();
                if (visited.contains(parent))
                    break;
                visited.add(parent);
                if (parent.getNodeTypeNumber() == old.getNodeTypeNumber() && !isNewMatched(parent) && !parent.isRoot())
                    candidates.add(parent);
                seed = parent;
            }
        }

        return candidates;
    }

    protected double jaccardSimilarity(ITree oldNode, ITree newNode) {
        double num = (double) numberOfCommonDescendants(oldNode, newNode);
        double den = (double) oldNode.getDescendants().size() + (double) newNode.getDescendants().size() - num;
        return num / den;
    }

    protected int numberOfCommonDescendants(ITree oldNode, ITree newNode) {
        Set<ITree> dstDescendants = new HashSet<>(newNode.getDescendants());
        int common = 0;

        for (ITree t : oldNode.getDescendants()) {
            ITree m = mappingStore.getNewTreeNode(t);
            if (m != null && dstDescendants.contains(m))
                common++;
        }

        return common;
    }

    protected void lastChanceMatch(ITree oldNode, ITree newNode) {
        ITree cOld = oldNode.deepCopy();
        ITree cNew = newNode.deepCopy();
        removeMatched(cOld, true);
        removeMatched(cNew, false);

        if (cOld.getSize() < 1000
                || cNew.getSize() < 1000) {
            Matcher m = new ZsMatcher(cOld, cNew, new MappingStore());
            m.match();
            for (Mapping candidate: m.getMappings().asSet()) {
                ITree left = srcIds.getTreeNode(candidate.getFirst().getId());
                ITree right = dstIds.getTreeNode(candidate.getSecond().getId());

                if (left.getId() == oldNode.getId() || right.getId() == newNode.getId()) {
//                    System.err.printf("Trying to map already mapped source node (%d == %d || %d == %d)\n",
//                            left.getId(), oldNode.getId(), right.getId(), newNode.getId());
                    continue;
                } else if (!isMappingAllowed(left, right)) {
//                    System.err.printf("Trying to map incompatible nodes (%s, %s)\n",
//                            left.toShortString(), right.toShortString());
                    continue;
                } else if (!left.getParent().isSameType(right.getParent())) {
//                    System.err.printf("Trying to map nodes with incompatible parents (%s, %s)\n",
//                            left.getParent().toShortString(), right.getParent().toShortString());
                    continue;
                } else
                    addMapping(left, right);
            }
        }

        mappedOld.putTreeNodes(oldNode);
        mappedNew.putTreeNodes(newNode);
    }

    public ITree removeMatched(ITree node, boolean isOld) {
        for (ITree t: node.getPreOrderTreeNodeList()) {
            if ((isOld && isOldMatched(t)) || ((!isOld) && isNewMatched(t))) {
                if (t.getParent() != null) t.getParent().getChildren().remove(t);
                t.setParent(null);
            }
        }
        node.refresh();
        return node;
    }
    boolean isOldMatched(ITree treeNode) {
        return mappedOld.contains(treeNode);
    }
    boolean isNewMatched(ITree treeNode) {
        return mappedNew.contains(treeNode);
    }

    public boolean isMappingAllowed(ITree src, ITree dst) {
        return src.isSameType(dst)
                && !(isOldMatched(src) || isNewMatched(dst));
    }

    protected void addMapping(ITree src, ITree dst) {
        mappedOld.putTreeNode(src);
        mappedNew.putTreeNode(dst);
        super.addMapping(src, dst);
    }
}
