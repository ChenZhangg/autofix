package fdse.zc.gumtree;

import java.util.ArrayList;

public class HeightIndexedPriorityList {
    public static final int  MIN_HEIGHT = 1;

    private int maxHeight;
    private ArrayList<ITree>[] array;
    private int currentIdx;
    @SuppressWarnings("unchecked")
    public HeightIndexedPriorityList(ITree node) {
        maxHeight = node.getHeight();
        int listSize = maxHeight - MIN_HEIGHT + 1;
        array = (ArrayList<ITree>[])new ArrayList[listSize];
        addTreeNode(node);
    }

    private int getArrayIndex(int height){ return maxHeight - height; }

    private void addTreeNode(ITree node) {
        if(node.getHeight() >= MIN_HEIGHT){
            int arrayIndex = getArrayIndex(node.getHeight());
            if(array[arrayIndex] == null) array[arrayIndex] = new  ArrayList<ITree>();
            array[arrayIndex].add(node);
        }
    }

    public ArrayList<ITree> open(){
        ArrayList<ITree> list = pop();
        if(list == null)
            return null;
        for(ITree node : list){
            open(node);
        }
        updateHeight();
        return list;
    }

    public void open(ITree node) {
        for (ITree childNode : node.getChildren())
            addTreeNode(childNode);
    }

    public ArrayList<ITree> pop(){
        if(currentIdx == -1) {
            return null;
        }else{
            ArrayList<ITree> list = array[currentIdx];
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
