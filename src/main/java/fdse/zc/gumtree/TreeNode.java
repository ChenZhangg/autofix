package fdse.zc.gumtree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeNode {
    public static final String NO_LABEL = "";

    private int nodeTypeNumber;
    private String nodeTypeName;
    private String nodeLabel;
    private int startPosition;
    private int length;
    private int height;
    private int size;
    private int depth;
    private int id;
    private int hash;
    private TreeNode parent;
    private List<TreeNode> children = new ArrayList<>();;

    public TreeNode(int nodeTypeNumber, String nodeTypeName, String nodeLabel, int startPosition, int length) {
        this.nodeTypeNumber = nodeTypeNumber;
        this.nodeTypeName = nodeTypeName;
        this.nodeLabel = (nodeLabel == null) ? NO_LABEL : nodeLabel;
        this.startPosition = startPosition;
        this.length = length;
    }

    private TreeNode(TreeNode other) {
        this.nodeTypeNumber = other.getNodeTypeNumber();
        this.nodeTypeName = other.getNodeTypeName();
        this.nodeLabel = other.getNodeLabel();
        this.startPosition = other.getStartPosition();
        this.length = other.getLength();
        this.height = other.getHeight();
        this.size = other.getSize();
        this.depth = other.getDepth();
        this.hash = other.getHash();
        this.depth = other.getDepth();
        this.id = other.getId();
        this.children = new ArrayList<TreeNode>();
    }

    private TreeNode(){

    }

    public static TreeNode fakeParentTreeNode(TreeNode child){
        TreeNode parent = new TreeNode();
        if(child != null){
            parent.addChild(child);
            child.setParent(parent);
        }
        return parent;
    }

    public TreeNode deepCopy() {
        TreeNode copy = new TreeNode(this);
        for (TreeNode child : getChildren())
            copy.addChild(child.deepCopy());
        return copy;
    }

    public void addChild(TreeNode child) {
        children.add(child);
        child.setParent(this);
    }

    public void setParentAndUpdateChildren(TreeNode parent) {
        this.parent = parent;
        if (this.parent != null)
            parent.getChildren().add(this);
    }

    public ArrayList<TreeNode> getPreOrderTreeNodeList() {
        ArrayList<TreeNode> list = new ArrayList<>();
        getPreOrderTreeNodeList(this, list);
        return list;
    }

    public void getPreOrderTreeNodeList(TreeNode node, ArrayList<TreeNode> list) {
        list.add(node);
        if(node.isLeaf()) {
            return;
        }
        for(TreeNode child : node.getChildren()){
            getPreOrderTreeNodeList(child, list);
        }
    }

    public ArrayList<TreeNode> getPostOrderTreeNodeList() {
        ArrayList<TreeNode> list = new ArrayList<>();
        getPostOrderTreeNodeList(this, list);
        return list;
    }

    public void getPostOrderTreeNodeList(TreeNode node, ArrayList<TreeNode> list) {
        for(TreeNode child : node.getChildren()){
            getPostOrderTreeNodeList(child, list);
        }
        list.add(node);
    }

    public ArrayList<TreeNode> getDescendants() {
        ArrayList<TreeNode> list = getPreOrderTreeNodeList();
        list.remove(0);
        return list;
    }

    public ArrayList<TreeNode> getBreadthFirstTreeNodeList(){
        ArrayList<TreeNode> list = new ArrayList<>();
        ArrayList<TreeNode> temp = new ArrayList<>();
        temp.add(this);
        while (temp.size() > 0){
            TreeNode node = temp.remove(0);
            list.add(node);
            temp.addAll(node.getChildren());
        }
        return list;
    }

    public void setNodeTypeNumber(int nodeTypeNumber) {
        this.nodeTypeNumber = nodeTypeNumber;
    }

    public int getNodeTypeNumber() {
        return nodeTypeNumber;
    }

    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight(){
        return height;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setHash(int hash){
        this.hash = hash;
    }

    public int getHash() {
        return hash;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode getChild(int position) {
        return getChildren().get(position);
    }

    public int getChildPosition(TreeNode child) {
        return getChildren().indexOf(child);
    }

    public int getPositionInParent(){
        TreeNode parent = getParent();
        if(parent == null)
            return -1;
        else
            return parent.getChildPosition(this);
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return getChildren().size() == 0;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isSameType(TreeNode node) {
        return getNodeTypeNumber() == node.getNodeTypeNumber();
    }

    public boolean isIsomorphicTo(TreeNode node) {
        if (this.getHash() != node.getHash()) {
            return false;
        } else {
            return this.toStaticHashString().equals(node.toStaticHashString());
        }
    }

    public void refresh() {
        TreeUtils treeUtils = new TreeUtils();
        treeUtils.computeHeight(this);
        treeUtils.computeSize(this);
        treeUtils.computeDepth(this, -1);
        new HashGenerator().hash(this);
    }

    public String toStaticHashString() {
        StringBuilder b = new StringBuilder();
        b.append("[(");
        b.append(this.toShortString());
        for (TreeNode c: this.getChildren())
            b.append(c.toStaticHashString());
        b.append(")]");
        return b.toString();
    }

    public String toShortString() {
        return String.format("%d%s%s", getNodeTypeNumber(), "@@", getNodeLabel());
    }

    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("nodeTypeNumber:" + nodeTypeNumber);
        b.append("  nodeTypeName:" + nodeTypeName);
        b.append("  id:" + id);
        b.append("  nodeLabel:" + nodeLabel);
        b.append("  startPosition:" + startPosition);
        b.append("  length:" + length);
        b.append("  height:" + height);
        b.append("  size:" + size);
        b.append("  depth:" + depth);
        b.append("  hash:" + hash);
        return b.toString();
    }
}