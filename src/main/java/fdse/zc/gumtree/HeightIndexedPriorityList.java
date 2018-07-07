package fdse.zc.gumtree;

import java.util.ArrayList;

public class HeightIndexedPriorityList {
    public static final int  MIN_HEIGHT = 1;

    private int maxHeight;
    private ArrayList<TreeNode>[] array;
    private int currentIdx;
    @SuppressWarnings("unchecked")
    public HeightIndexedPriorityList(TreeNode node) {
        maxHeight = node.getHeight();
        int listSize = maxHeight - MIN_HEIGHT + 1;
        array = (ArrayList<TreeNode>[])new ArrayList[listSize];
        addTreeNode(node);
    }

    private int getArrayIndex(int height){ return maxHeight - height; }

    private void addTreeNode(TreeNode node) {
        if(node.getHeight() >= MIN_HEIGHT){
            int arrayIndex = getArrayIndex(node.getHeight());
            if(array[arrayIndex] == null) array[arrayIndex] = new  ArrayList<TreeNode>();
            array[arrayIndex].add(node);
        }
    }

    public ArrayList<TreeNode> open(){
        ArrayList<TreeNode> list = pop();
        if(list == null)
            return null;
        for(TreeNode node : list){
            open(node);
        }
        updateHeight();
        return list;
    }

    public void open(TreeNode node) {
        for (TreeNode childNode : node.getChildren())
            addTreeNode(childNode);
    }

    public ArrayList<TreeNode> pop(){
        if(currentIdx == -1) {
            return null;
        }else{
            ArrayList<TreeNode> list = array[currentIdx];
            array[currentIdx] = null;
            return list;
        }
    }

    public int peekMax(){
        return (currentIdx == -1) ? -1 : (maxHeight - currentIdx);
    }

    public void updateHeight(){
        currentIdx = -1;
        for(int i = 0; i< array.length; i++){
            if(array[i] != null){
                currentIdx = i;
                break;
            }
        }
    }
}
