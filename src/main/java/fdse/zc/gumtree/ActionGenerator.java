package fdse.zc.gumtree;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionGenerator {
    private TreeNode oldRoot;
    private TreeNode newRoot;
    private TreeNode oldRootClone;
    private MappingStore mappingStore;
    private MappingStore mappingStoreClone;
    private int lastId;
    private Set<TreeNode> oldInOrder;
    private Set<TreeNode> newInOrder;
    private TIntObjectMap<TreeNode> oldRootMap;
    private TIntObjectMap<TreeNode> oldRootMapClone;
    private ArrayList<Action> actions = new ArrayList<>();
    public ActionGenerator(TreeNode oldRoot, TreeNode newRoot, MappingStore mappingStore){
        this.oldRoot = oldRoot;
        this.newRoot = newRoot;
        this.oldRootClone = this.oldRoot.deepCopy();

        this.oldRootMap = new TIntObjectHashMap();
        for(TreeNode node : this.oldRoot.getPreOrderTreeNodeList()){
            this.oldRootMap.put(node.getId(), node);
        }

        this.oldRootMapClone = new TIntObjectHashMap();
        for(TreeNode node : this.oldRootClone.getPreOrderTreeNodeList()){
            this.oldRootMapClone.put(node.getId(), node);
        }

        this.mappingStore = new MappingStore();
        for(Mapping m : mappingStore.asSet()){
            this.mappingStore.link(this.oldRootMapClone.get(m.getFirst().getId()), m.getSecond());
        }
        this.mappingStoreClone = this.mappingStore.copy();
    }

    public List<Action> generate(){
        TreeNode oldFakeRoot = TreeNode.fakeParentTreeNode(oldRootClone);
        TreeNode newFakeRoot = TreeNode.fakeParentTreeNode(newRoot);

        oldInOrder = new HashSet<>();
        newInOrder = new HashSet<>();
        lastId = oldRootClone.getSize() + 1;
        mappingStoreClone.link(oldFakeRoot, newFakeRoot);
        for(TreeNode x : newRoot.getBreadthFirstTreeNodeList()){
            TreeNode w = null;
            TreeNode y = x.getParent();
            TreeNode z = mappingStoreClone.getOldTreeNode(y);
            if(!mappingStoreClone.hasNewTreeNode(x)){
                int k = findPos(x);
                w = TreeNode.fakeParentTreeNode(null);
                w.setId(newId());

                Action ins = new Insert(x, oldRootMap.get(z.getId()), k);
                actions.add(ins);

                oldRootMap.put(w.getId(), x);
                mappingStoreClone.link(w, x);
                z.getChildren().add(k, w);
                w.setParent(z);
            } else{
                w = mappingStoreClone.getOldTreeNode(x);
                if (!x.equals(newRoot)) { // TODO => x != origDst // Case of the root
                    TreeNode v = w.getParent();
                    if (!w.getNodeLabel().equals(x.getNodeLabel())) {
                        actions.add(new Update(oldRootMap.get(w.getId()), x.getNodeLabel()));
                        w.setNodeLabel(x.getNodeLabel());
                    }
                    if (!z.equals(v)) {
                        int k = findPos(x);
                        Action mv = new Move(oldRootMap.get(w.getId()), oldRootMap.get(z.getId()), k);
                        actions.add(mv);
                        int oldk = w.getPositionInParent();
                        z.getChildren().add(k, w);
                        w.getParent().getChildren().remove(oldk);
                        w.setParent(z);
                    }
                }
            }
            oldInOrder.add(w);
            newInOrder.add(x);
            alignChildren(w, x);
        }
        for (TreeNode w : oldRootClone.getPostOrderTreeNodeList()) {
            if (!mappingStoreClone.hasOldTreeNode(w)) {
                actions.add(new Delete(oldRootMap.get(w.getId())));
            }
        }
        return actions;
    }

    private void alignChildren(TreeNode w, TreeNode x) {
        oldInOrder.removeAll(w.getChildren());
        newInOrder.removeAll(x.getChildren());

        List<TreeNode> s1 = new ArrayList<>();
        for (TreeNode c: w.getChildren())
            if (mappingStoreClone.hasOldTreeNode(c))
                if (x.getChildren().contains(mappingStoreClone.getNewTreeNode(c)))
                    s1.add(c);

        List<TreeNode> s2 = new ArrayList<>();
        for (TreeNode c: x.getChildren())
            if (mappingStoreClone.hasNewTreeNode(c))
                if (w.getChildren().contains(mappingStoreClone.getOldTreeNode(c)))
                    s2.add(c);

        List<Mapping> lcs = lcs(s1, s2);

        for (Mapping m : lcs) {
            System.out.println("s1 size" + s1.size() + "M: " + m);
            oldInOrder.add(m.getFirst());
            newInOrder.add(m.getSecond());
        }

        for (TreeNode a : s1) {
            for (TreeNode b: s2 ) {
                if (mappingStore.has(a, b)) {
                    if (!lcs.contains(new Mapping(a, b))) {
                        System.out.println("W: " + w);
                        System.out.println("X: " + x);
                        int k = findPos(b);
                        Action mv = new Move(oldRootMap.get(a.getId()), oldRootMap.get(w.getId()), k);
                        actions.add(mv);
                        int oldk = a.getPositionInParent();
                        w.getChildren().add(k, a);
                        if (k  < oldk ) // FIXME this is an ugly way to patch the index
                            oldk ++;
                        a.getParent().getChildren().remove(oldk);
                        a.setParent(w);
                        oldInOrder.add(a);
                        newInOrder.add(b);
                    }
                }
            }
        }
    }

    private int findPos(TreeNode x){
        TreeNode parent = x.getParent();
        List<TreeNode> siblings = parent.getChildren();

        for(TreeNode c : siblings){
            if(newInOrder.contains(c)){
                if(c.equals(x)) return 0;
                else break;
            }
        }
        int xpos = x.getPositionInParent();
        TreeNode v = null;
        for (int i = 0; i < xpos; i++) {
            TreeNode c = siblings.get(i);
            if (newInOrder.contains(c)) v = c;
        }

        if (v == null) return 0;

        TreeNode u = mappingStoreClone.getOldTreeNode(v);

        int upos = u.getPositionInParent();

        return upos + 1;
    }

    private int newId() {
        return ++lastId;
    }

    private List<Mapping> lcs(List<TreeNode> x, List<TreeNode> y) {
        int m = x.size();
        int n = y.size();
        List<Mapping> lcs = new ArrayList<>();

        int[][] opt = new int[m + 1][n + 1];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (mappingStoreClone.getOldTreeNode(y.get(j)).equals(x.get(i))) opt[i][j] = opt[i + 1][j + 1] + 1;
                else  opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
            }
        }

        int i = 0, j = 0;
        while (i < m && j < n) {
            if (mappingStoreClone.getOldTreeNode(y.get(j)).equals(x.get(i))) {
                lcs.add(new Mapping(x.get(i), y.get(j)));
                i++;
                j++;
            } else if (opt[i + 1][j] >= opt[i][j + 1]) i++;
            else j++;
        }

        return lcs;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}
