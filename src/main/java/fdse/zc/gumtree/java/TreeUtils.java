package fdse.zc.gumtree.java;

public class TreeUtils {
    public int id = 0;
    public int computeSize(TreeNode root) {
        int size = 1;
        if(root.isLeaf()){
            root.setSize(size);
            return size;
        }

        int tempSize = 0;
        for(TreeNode node : root.getChildren()){
            tempSize = computeSize(node);
            size += tempSize;
        }
        root.setSize(size);
        return size;
    }

    public void computeDepth(TreeNode root, int parentDepth) {
        int depth = parentDepth + 1;
        root.setDepth(depth);

        for(TreeNode node : root.getChildren()){
            computeDepth(node, depth);
        }
    }


    public int computeHeight(TreeNode root) {
        int maxHeight = 0;
        if (root.isLeaf()) {
            root.setHeight(maxHeight);
            return maxHeight;
        }

        int height = 0;
        for (TreeNode node : root.getChildren()) {
            height = computeHeight(node);
            if (height > maxHeight) maxHeight = height;
        }
        maxHeight++;
        root.setHeight(maxHeight);
        return maxHeight;
    }

    public void computeId(TreeNode root) {
        for (TreeNode node : root.getChildren()) {
            computeId(node);
        }
        root.setId(id++);
    }
}
