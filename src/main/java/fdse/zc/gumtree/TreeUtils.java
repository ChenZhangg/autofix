package fdse.zc.gumtree.java;

public class TreeUtils {
    public int id = 0;
    public int computeSize(JavaTree root) {
        int size = 1;
        if(root.isLeaf()){
            root.setSize(size);
            return size;
        }

        int tempSize = 0;
        for(JavaTree node : root.getChildren()){
            tempSize = computeSize(node);
            size += tempSize;
        }
        root.setSize(size);
        return size;
    }

    public void computeDepth(JavaTree root, int parentDepth) {
        int depth = parentDepth + 1;
        root.setDepth(depth);

        for(JavaTree node : root.getChildren()){
            computeDepth(node, depth);
        }
    }


    public int computeHeight(JavaTree root) {
        int maxHeight = 0;
        if (root.isLeaf()) {
            root.setHeight(maxHeight);
            return maxHeight;
        }

        int height = 0;
        for (JavaTree node : root.getChildren()) {
            height = computeHeight(node);
            if (height > maxHeight) maxHeight = height;
        }
        maxHeight++;
        root.setHeight(maxHeight);
        return maxHeight;
    }

    public void computeId(JavaTree root) {
        for (JavaTree node : root.getChildren()) {
            computeId(node);
        }
        root.setId(id++);
    }
}
