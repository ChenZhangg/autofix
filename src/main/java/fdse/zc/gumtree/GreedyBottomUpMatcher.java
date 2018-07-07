import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GreedyBottomUpMatcher extends Matcher{

    protected TreeNodeMap srcIds;
    protected TreeNodeMap dstIds;

    protected TreeNodeMap mappedOld;
    protected TreeNodeMap mappedNew;

    public GreedyBottomUpMatcher(TreeNode oldRoot, TreeNode newRoot, MappingStore mappingStore) {
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
        for (TreeNode t: oldRoot.getPostOrderTreeNodeList())  {
            if (t.isRoot()) {
                addMapping(t, newRoot);
                lastChanceMatch(t, newRoot);
                break;
            } else if (!(isOldMatched(t) || t.isLeaf())) {

                List<TreeNode> candidates = getNewCandidates(t);
                TreeNode best = null;
                double max = -1D;

                for (TreeNode cand: candidates) {
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
    protected List<TreeNode> getNewCandidates(TreeNode old) {
        List<TreeNode> seeds = new ArrayList<>();
        for (TreeNode c: old.getDescendants()) {
            TreeNode m = mappingStore.getNewTreeNode(c);
            if (m != null) seeds.add(m);
        }
        List<TreeNode> candidates = new ArrayList<>();
        Set<TreeNode> visited = new HashSet<>();
        for (TreeNode seed: seeds) {
            while (seed.getParent() != null) {
                TreeNode parent = seed.getParent();
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

    protected double jaccardSimilarity(TreeNode oldNode, TreeNode newNode) {
        double num = (double) numberOfCommonDescendants(oldNode, newNode);
        double den = (double) oldNode.getDescendants().size() + (double) newNode.getDescendants().size() - num;
        return num / den;
    }

    protected int numberOfCommonDescendants(TreeNode oldNode, TreeNode newNode) {
        Set<TreeNode> dstDescendants = new HashSet<>(newNode.getDescendants());
        int common = 0;

        for (TreeNode t : oldNode.getDescendants()) {
            TreeNode m = mappingStore.getNewTreeNode(t);
            if (m != null && dstDescendants.contains(m))
                common++;
        }

        return common;
    }

    protected void lastChanceMatch(TreeNode oldNode, TreeNode newNode) {
        TreeNode cOld = oldNode.deepCopy();
        TreeNode cNew = newNode.deepCopy();
        removeMatched(cOld, true);
        removeMatched(cNew, false);

        if (cOld.getSize() < 1000
                || cNew.getSize() < 1000) {
            Matcher m = new ZsMatcher(cOld, cNew, new MappingStore());
            m.match();
            for (Mapping candidate: m.getMappings().asSet()) {
                TreeNode left = srcIds.getTreeNode(candidate.getFirst().getId());
                TreeNode right = dstIds.getTreeNode(candidate.getSecond().getId());

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

    public TreeNode removeMatched(TreeNode node, boolean isOld) {
        for (TreeNode t: node.getPreOrderTreeNodeList()) {
            if ((isOld && isOldMatched(t)) || ((!isOld) && isNewMatched(t))) {
                if (t.getParent() != null) t.getParent().getChildren().remove(t);
                t.setParent(null);
            }
        }
        node.refresh();
        return node;
    }
    boolean isOldMatched(TreeNode treeNode) {
        return mappedOld.contains(treeNode);
    }
    boolean isNewMatched(TreeNode treeNode) {
        return mappedNew.contains(treeNode);
    }

    public boolean isMappingAllowed(TreeNode src, TreeNode dst) {
        return src.isSameType(dst)
                && !(isOldMatched(src) || isNewMatched(dst));
    }

    protected void addMapping(TreeNode src, TreeNode dst) {
        mappedOld.putTreeNode(src);
        mappedNew.putTreeNode(dst);
        super.addMapping(src, dst);
    }
}
