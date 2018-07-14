package fdse.zc.gumtree.java;

import org.simmetrics.StringMetrics;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ZsMatcher extends Matcher{
    private ZsTree zsSrc;
    private ZsTree zsDst;

    private double[][] treeDist;
    private double[][] forestDist;

    private static JavaTree getFirstLeaf(JavaTree t) {
        JavaTree current = t;
        while (!current.isLeaf())
            current = current.getChild(0);

        return current;
    }

    public ZsMatcher(JavaTree src, JavaTree dst, MappingStore store) {
        super(src, dst, store);
        this.zsSrc = new ZsTree(src);
        this.zsDst = new ZsTree(dst);
    }

    private double[][] computeTreeDist() {

        treeDist = new double[zsSrc.nodeCount + 1][zsDst.nodeCount + 1];
        forestDist = new double[zsSrc.nodeCount + 1][zsDst.nodeCount + 1];

        for (int i = 1; i < zsSrc.kr.length; i++) {
            for (int j = 1; j < zsDst.kr.length; j++) {
                forestDist(zsSrc.kr[i], zsDst.kr[j]);

            }
        }

        return treeDist;
    }

    private void forestDist(int i, int j) {
        forestDist[zsSrc.lld(i) - 1][zsDst.lld(j) - 1] = 0;
        for (int di = zsSrc.lld(i); di <= i; di++) {
            double costDel =  getDeletionCost(zsSrc.tree(di));
            forestDist[di][zsDst.lld(j) - 1] = forestDist[di - 1][zsDst.lld(j) - 1] + costDel;
            for (int dj = zsDst.lld(j); dj <= j; dj++) {
                double costIns = getInsertionCost(zsDst.tree(dj));
                forestDist[zsSrc.lld(i) - 1][dj] = forestDist[zsSrc.lld(i) - 1][dj - 1] + costIns;

                if ((zsSrc.lld(di) == zsSrc.lld(i) && (zsDst.lld(dj) == zsDst.lld(j)))) {
                    double costUpd = getUpdateCost(zsSrc.tree(di), zsDst.tree(dj));
                    forestDist[di][dj] = Math.min(Math.min(forestDist[di - 1][dj] + costDel,
                            forestDist[di][dj - 1] + costIns),
                            forestDist[di - 1][dj - 1] + costUpd);
                    treeDist[di][dj] = forestDist[di][dj];
                } else {
                    forestDist[di][dj] = Math.min(Math.min(forestDist[di - 1][dj] + costDel,
                            forestDist[di][dj - 1] + costIns),
                            forestDist[zsSrc.lld(di) - 1][zsDst.lld(dj) - 1]
                                    + treeDist[di][dj]);
                }
            }
        }
    }

    public void match() {
        computeTreeDist();

        boolean rootNodePair = true;

        ArrayDeque<int[]> treePairs = new ArrayDeque<>();

        // push the pair of trees (ted1,ted2) to stack
        treePairs.addFirst(new int[] { zsSrc.nodeCount, zsDst.nodeCount });

        while (!treePairs.isEmpty()) {
            int[] treePair = treePairs.removeFirst();

            int lastRow = treePair[0];
            int lastCol = treePair[1];

            // compute forest distance matrix
            if (!rootNodePair)
                forestDist(lastRow, lastCol);

            rootNodePair = false;

            // compute mapping for current forest distance matrix
            int firstRow = zsSrc.lld(lastRow) - 1;
            int firstCol = zsDst.lld(lastCol) - 1;

            int row = lastRow;
            int col = lastCol;

            while ((row > firstRow) || (col > firstCol)) {
                if ((row > firstRow)
                        && (forestDist[row - 1][col] + 1D == forestDist[row][col])) {
                    // node with postorderID row is deleted from ted1
                    row--;
                } else if ((col > firstCol)
                        && (forestDist[row][col - 1] + 1D == forestDist[row][col])) {
                    // node with postorderID col is inserted into ted2
                    col--;
                } else {
                    // node with postorderID row in ted1 is renamed to node col
                    // in ted2
                    if ((zsSrc.lld(row) - 1 == zsSrc.lld(lastRow) - 1)
                            && (zsDst.lld(col) - 1 == zsDst.lld(lastCol) - 1)) {
                        // if both subforests are trees, map nodes
                        JavaTree tSrc = zsSrc.tree(row);
                        JavaTree tDst = zsDst.tree(col);
                        if (tSrc.getNodeTypeNumber() == tDst.getNodeTypeNumber())
                            addMapping(tSrc, tDst);
                        else
                            throw new RuntimeException("Should not map incompatible nodes.");
                        row--;
                        col--;
                    } else {
                        // pop subtree pair
                        treePairs.addFirst(new int[] { row, col });
                        // continue with forest to the left of the popped
                        // subtree pair

                        row = zsSrc.lld(row) - 1;
                        col = zsDst.lld(col) - 1;
                    }
                }
            }
        }
    }

    private double getDeletionCost(JavaTree n) {
        return 1D;
    }

    private double getInsertionCost(JavaTree n) {
        return 1D;
    }

    private double getUpdateCost(JavaTree n1, JavaTree n2) {
        if (n1.getNodeTypeNumber() == n2.getNodeTypeNumber())
            if ("".equals(n1.getNodeLabel()) || "".equals(n2.getNodeLabel()))
                return 1D;
            else
                return 1D - StringMetrics.qGramsDistance().compare(n1.getNodeLabel(), n2.getNodeLabel());
        else
            return Double.MAX_VALUE;
    }

    private static final class ZsTree {

        private int start; // internal array position of leafmost leaf descendant of the root node

        private int nodeCount; // number of nodes

        private int leafCount;

        private int[] llds; // llds[i] stores the postorder-ID of the
        // left-most leaf descendant of the i-th node in postorder
        private JavaTree[] labels; // labels[i] is the tree of the i-th node in postorder

        private int[] kr;

        private ZsTree(JavaTree t) {
            this.start = 0;
            this.nodeCount = t.getSize();
            this.leafCount = 0;
            this.llds = new int[start + nodeCount];
            this.labels = new JavaTree[start + nodeCount];

            int idx = 1;
            Map<JavaTree,Integer> tmpData = new HashMap<>();
            for (JavaTree n: t.getPostOrderTreeNodeList()) {
                tmpData.put(n, idx);
                this.setITree(idx, n);
                this.setLld(idx,  tmpData.get(ZsMatcher.getFirstLeaf(n)));
                if (n.isLeaf())
                    leafCount++;
                idx++;
            }

            setKeyRoots();
        }

        public void setITree(int i, JavaTree tree) {
            labels[i + start - 1] = tree;
            if (nodeCount < i)
                nodeCount = i;
        }

        public void setLld(int i, int lld) {
            llds[i + start - 1] = lld + start - 1;
            if (nodeCount < i)
                nodeCount = i;
        }

        public boolean isLeaf(int i) {
            return this.lld(i) == i;
        }

        public int lld(int i) {
            return llds[i + start - 1] - start + 1;
        }

        public JavaTree tree(int i) {
            return labels[i + start - 1];
        }

        public void setKeyRoots() {
            kr = new int[leafCount + 1];
            boolean[] visited = new boolean[nodeCount + 1];
            Arrays.fill(visited, false);
            int k = kr.length - 1;
            for (int i = nodeCount; i >= 1; i--) {
                if (!visited[lld(i)]) {
                    kr[k] = i;
                    visited[lld(i)] = true;
                    k--;
                }
            }
        }
    }
}
