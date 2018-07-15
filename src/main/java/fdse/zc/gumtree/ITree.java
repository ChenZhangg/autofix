package fdse.zc.gumtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface to represent abstract syntax trees.
 */
public interface ITree {

    String OPEN_SYMBOL = "[(";
    String CLOSE_SYMBOL = ")]";
    String SEPARATE_SYMBOL = "@@";
    int NO_ID = Integer.MIN_VALUE;
    String NO_LABEL = "";
    int NO_VALUE = -1;


  
    public ArrayList<ITree> getPreOrderTreeNodeList();
    public ArrayList<ITree> getPostOrderTreeNodeList();
    public ArrayList<ITree> getBreadthFirstTreeNodeList();
    /**
     * Add the given tree as a child, and update its parent.
     */
    void addChild(ITree t);

    /**
     * @return the position of the child, or -1 if the given child is not in the children list.
     */
    int getChildPosition(ITree child);

    /**
     * @param position the child position, starting at 0
     */
    ITree getChild(int position);

    List<ITree> getChildren();

    /**
     * @return all the descendants (children, children of children, etc.) of the tree,
     *     using a pre-order.
     */
    List<ITree> getDescendants();

    /**
     * Set the parent of this node. The parent won't have this node in its
     * children list
     */
    void setParent(ITree parent);

    /**
     * Set the parent of this node. The parent will have this node in its
     * children list, at the last position
     */
    void setParentAndUpdateChildren(ITree parent);
    public void setNodeTypeNumber(int nodeTypeNumber);

    public int getNodeTypeNumber();

    public void setNodeTypeName(String nodeTypeName);

    public String getNodeTypeName();

    public void setNodeLabel(String nodeLabel);

    public String getNodeLabel();

    public void setStartPosition(int startPosition);

    public int getStartPosition();

    public void setLength(int length);

    public int getLength();

    public void setHeight(int height);

    public int getHeight();

    public void setSize(int size);

    public int getSize();

    public void setDepth(int depth);

    public int getDepth();

    public void setId(int id);

    public int getId();

    public void setHash(int hash);

    public int getHash();

    public int getPositionInParent();
    /**
     * @return a boolean indicating if the tree has at least one child or not
     */
    boolean isLeaf();
    /**
     * @return a boolean indicating if the tree has a parent or not
     */
    boolean isRoot();

    ITree getParent();
    ITree deepCopy();
    boolean isSameType(ITree t);
    boolean isIsomorphicTo(ITree tree);
    void refresh();
    String toStaticHashString();
    String toShortString();
}
