package fdse.zc.gumtree.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fdse.zc.gumtree.ITree;

public class JavaTree implements ITree {
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
    private JavaTree parent;
    private List<JavaTree> children = new ArrayList<>();;

    public JavaTree(int nodeTypeNumber, String nodeTypeName, String nodeLabel, int startPosition, int length) {
        this.nodeTypeNumber = nodeTypeNumber;
        this.nodeTypeName = nodeTypeName;
        this.nodeLabel = (nodeLabel == null) ? NO_LABEL : nodeLabel;
        this.startPosition = startPosition;
        this.length = length;
    }

    private JavaTree(JavaTree other) {
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
        this.children = new ArrayList<JavaTree>();
    }

    private JavaTree(){

    }

    public static JavaTree fakeParentTreeNode(JavaTree child){
        JavaTree parent = new JavaTree();
        if(child != null){
            parent.addChild(child);
            child.setParent(parent);
        }
        return parent;
    }

    public JavaTree deepCopy() {
        JavaTree copy = new JavaTree(this);
        for (JavaTree child : getChildren())
            copy.addChild(child.deepCopy());
        return copy;
    }

    public void addChild(JavaTree child) {
        children.add(child);
        child.setParent(this);
    }

    public void setParentAndUpdateChildren(JavaTree parent) {
        this.parent = parent;
        if (this.parent != null)
            parent.getChildren().add(this);
    }

    public ArrayList<JavaTree> getPreOrderTreeNodeList() {
        ArrayList<JavaTree> list = new ArrayList<>();
        getPreOrderTreeNodeList(this, list);
        return list;
    }

    public void getPreOrderTreeNodeList(JavaTree node, ArrayList<JavaTree> list) {
        list.add(node);
        if(node.isLeaf()) {
            return;
        }
        for(JavaTree child : node.getChildren()){
            getPreOrderTreeNodeList(child, list);
        }
    }

    public ArrayList<JavaTree> getPostOrderTreeNodeList() {
        ArrayList<JavaTree> list = new ArrayList<>();
        getPostOrderTreeNodeList(this, list);
        return list;
    }

    public void getPostOrderTreeNodeList(JavaTree node, ArrayList<JavaTree> list) {
        for(JavaTree child : node.getChildren()){
            getPostOrderTreeNodeList(child, list);
        }
        list.add(node);
    }

    public ArrayList<JavaTree> getDescendants() {
        ArrayList<JavaTree> list = getPreOrderTreeNodeList();
        list.remove(0);
        return list;
    }

    public ArrayList<JavaTree> getBreadthFirstTreeNodeList(){
        ArrayList<JavaTree> list = new ArrayList<>();
        ArrayList<JavaTree> temp = new ArrayList<>();
        temp.add(this);
        while (temp.size() > 0){
            JavaTree node = temp.remove(0);
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

    public void setParent(JavaTree parent) {
        this.parent = parent;
    }

    public JavaTree getParent() {
        return parent;
    }

    public JavaTree getChild(int position) {
        return getChildren().get(position);
    }

    public int getChildPosition(JavaTree child) {
        return getChildren().indexOf(child);
    }

    public int getPositionInParent(){
        JavaTree parent = getParent();
        if(parent == null)
            return -1;
        else
            return parent.getChildPosition(this);
    }

    public List<JavaTree> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return getChildren().size() == 0;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isSameType(JavaTree node) {
        return getNodeTypeNumber() == node.getNodeTypeNumber();
    }

    public boolean isIsomorphicTo(JavaTree node) {
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
        for (JavaTree c: this.getChildren())
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