package fdse.zc.gumtree;

public class TreeUtils {
    public int id = 0;
    public int computeSize(ITree root) {
        int size = 1;
        if(root.isLeaf()){
            root.setSize(size);
            return size;
        }

        int tempSize = 0;
        for(ITree node : root.getChildren()){
            tempSize = computeSize(node);
            size += tempSize;
        }
        root.setSize(size);
        return size;
    }

    public void computeDepth(ITree root, int parentDepth) {
        int depth = parentDepth + 1;
        root.setDepth(depth);

        for(ITree node : root.getChildren()){
            computeDepth(node, depth);
        }
    }


    public int computeHeight(ITree root) {
        int maxHeight = 0;
        if (root.isLeaf()) {
            root.setHeight(maxHeight);
            return maxHeight;
        }

        int height = 0;
        for (ITree node : root.getChildren()) {
            height = computeHeight(node);
            if (height > maxHeight) maxHeight = height;
        }
        maxHeight++;
        root.setHeight(maxHeight);
        return maxHeight;
    }

    public void computeId(ITree root) {
        for (ITree node : root.getChildren()) {
            computeId(node);
        }
        root.setId(id++);
    }
}
