package fdse.zc.gumtree.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GreedyBottomUpMatcher extends Matcher{

    protected TreeNodeMap srcIds;
    protected TreeNodeMap dstIds;

    protected TreeNodeMap mappedOld;
    protected TreeNodeMap mappedNew;

    public GreedyBottomUpMatcher(JavaTree oldRoot, JavaTree newRoot, MappingStore mappingStore) {
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
        for (JavaTree t: oldRoot.getPostOrderTreeNodeList())  {
            if (t.isRoot()) {
                addMapping(t, newRoot);
                lastChanceMatch(t, newRoot);
                break;
            } else if (!(isOldMatched(t) || t.isLeaf())) {

                List<JavaTree> candidates = getNewCandidates(t);
                JavaTree best = null;
                double max = -1D;

                for (JavaTree cand: candidates) {
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
    protected List<JavaTree> getNewCandidates(JavaTree old) {
        List<JavaTree> seeds = new ArrayList<>();
        for (JavaTree c: old.getDescendants()) {
            JavaTree m = mappingStore.getNewTreeNode(c);
            if (m != null) seeds.add(m);
        }
        List<JavaTree> candidates = new ArrayList<>();
        Set<JavaTree> visited = new HashSet<>();
        for (JavaTree seed: seeds) {
            while (seed.getParent() != null) {
                JavaTree parent = seed.getParent();
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

    protected double jaccardSimilarity(JavaTree oldNode, JavaTree newNode) {
        double num = (double) numberOfCommonDescendants(oldNode, newNode);
        double den = (double) oldNode.getDescendants().size() + (double) newNode.getDescendants().size() - num;
        return num / den;
    }

    protected int numberOfCommonDescendants(JavaTree oldNode, JavaTree newNode) {
        Set<JavaTree> dstDescendants = new HashSet<>(newNode.getDescendants());
        int common = 0;

        for (JavaTree t : oldNode.getDescendants()) {
            JavaTree m = mappingStore.getNewTreeNode(t);
            if (m != null && dstDescendants.contains(m))
                common++;
        }

        return common;
    }

    protected void lastChanceMatch(JavaTree oldNode, JavaTree newNode) {
        JavaTree cOld = oldNode.deepCopy();
        JavaTree cNew = newNode.deepCopy();
        removeMatched(cOld, true);
        removeMatched(cNew, false);

        if (cOld.getSize() < 1000
                || cNew.getSize() < 1000) {
            Matcher m = new ZsMatcher(cOld, cNew, new MappingStore());
            m.match();
            for (Mapping candidate: m.getMappings().asSet()) {
                JavaTree left = srcIds.getTreeNode(candidate.getFirst().getId());
                JavaTree right = dstIds.getTreeNode(candidate.getSecond().getId());

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

    public JavaTree removeMatched(JavaTree node, boolean isOld) {
        for (JavaTree t: node.getPreOrderTreeNodeList()) {
            if ((isOld && isOldMatched(t)) || ((!isOld) && isNewMatched(t))) {
                if (t.getParent() != null) t.getParent().getChildren().remove(t);
                t.setParent(null);
            }
        }
        node.refresh();
        return node;
    }
    boolean isOldMatched(JavaTree treeNode) {
        return mappedOld.contains(treeNode);
    }
    boolean isNewMatched(JavaTree treeNode) {
        return mappedNew.contains(treeNode);
    }

    public boolean isMappingAllowed(JavaTree src, JavaTree dst) {
        return src.isSameType(dst)
                && !(isOldMatched(src) || isNewMatched(dst));
    }

    protected void addMapping(JavaTree src, JavaTree dst) {
        mappedOld.putTreeNode(src);
        mappedNew.putTreeNode(dst);
        super.addMapping(src, dst);
    }
}
