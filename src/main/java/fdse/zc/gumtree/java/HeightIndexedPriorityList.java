package fdse.zc.gumtree.java;

import java.util.ArrayList;

public class HeightIndexedPriorityList {
    public static final int  MIN_HEIGHT = 1;

    private int maxHeight;
    private ArrayList<JavaTree>[] array;
    private int currentIdx;
    @SuppressWarnings("unchecked")
    public HeightIndexedPriorityList(JavaTree node) {
        maxHeight = node.getHeight();
        int listSize = maxHeight - MIN_HEIGHT + 1;
        array = (ArrayList<JavaTree>[])new ArrayList[listSize];
        addTreeNode(node);
    }

    private int getArrayIndex(int height){ return maxHeight - height; }

    private void addTreeNode(JavaTree node) {
        if(node.getHeight() >= MIN_HEIGHT){
            int arrayIndex = getArrayIndex(node.getHeight());
            if(array[arrayIndex] == null) array[arrayIndex] = new  ArrayList<JavaTree>();
            array[arrayIndex].add(node);
        }
    }

    public ArrayList<JavaTree> open(){
        ArrayList<JavaTree> list = pop();
        if(list == null)
            return null;
        for(JavaTree node : list){
            open(node);
        }
        updateHeight();
        return list;
    }

    public void open(JavaTree node) {
        for (JavaTree childNode : node.getChildren())
            addTreeNode(childNode);
    }

    public ArrayList<JavaTree> pop(){
        if(currentIdx == -1) {
            return null;
        }else{
            ArrayList<JavaTree> list = array[currentIdx];
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
