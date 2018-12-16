package fdse.zc.gumtree.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import fdse.zc.gumtree.HashGenerator;
import fdse.zc.gumtree.ITree;
import fdse.zc.gumtree.TreeUtils;

public class JavaTree implements ITree {
    public static final String NO_LABEL = "";

    private ASTNode astNode;
    private int nodeTypeNumber;
    private String nodeTypeName;
    private String nodeLabel;
    private int startPosition;
    private int length;
    private int startLineNumber;
    private int endLineNumber;

    private int height;
    private int size;
    private int depth;
    private int id;
    private int hash;
    private ITree parent;
    private List<ITree> children = new ArrayList<>();;

    public JavaTree(int nodeTypeNumber, String nodeTypeName, String nodeLabel, int startPosition, int length, int startLineNumber, int endLineNumber, ASTNode astNode) {
        this.nodeTypeNumber = nodeTypeNumber;
        this.nodeTypeName = nodeTypeName;
        this.nodeLabel = (nodeLabel == null) ? NO_LABEL : nodeLabel;
        this.startPosition = startPosition;
        this.length = length;
        this.startLineNumber = startLineNumber;
        this.endLineNumber = endLineNumber;
        this.astNode = astNode;
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
        this.children = new ArrayList<ITree>();
    }

    private JavaTree(){

    }

    public static ITree fakeParentTreeNode(ITree child){
        ITree parent = new JavaTree();
        if(child != null){
            parent.addChild(child);
            child.setParent(parent);
        }
        return parent;
    }

    public ITree deepCopy() {
        ITree copy = new JavaTree(this);
        for (ITree child : getChildren())
            copy.addChild(child.deepCopy());
        return copy;
    }

    public void addChild(ITree child) {
        children.add(child);
        child.setParent(this);
    }

    public void setParentAndUpdateChildren(ITree parent) {
        this.parent = parent;
        if (this.parent != null)
            parent.getChildren().add(this);
    }

    public ArrayList<ITree> getPreOrderTreeNodeList() {
        ArrayList<ITree> list = new ArrayList<>();
        getPreOrderTreeNodeList(this, list);
        return list;
    }

    public void getPreOrderTreeNodeList(ITree node, ArrayList<ITree> list) {
        list.add(node);
        if(node.isLeaf()) {
            return;
        }
        for(ITree child : node.getChildren()){
            getPreOrderTreeNodeList(child, list);
        }
    }

    public ArrayList<ITree> getPostOrderTreeNodeList() {
        ArrayList<ITree> list = new ArrayList<>();
        getPostOrderTreeNodeList(this, list);
        return list;
    }

    public void getPostOrderTreeNodeList(ITree node, ArrayList<ITree> list) {
        for(ITree child : node.getChildren()){
            getPostOrderTreeNodeList(child, list);
        }
        list.add(node);
    }

    public ArrayList<ITree> getDescendants() {
        ArrayList<ITree> list = getPreOrderTreeNodeList();
        list.remove(0);
        return list;
    }

    public ArrayList<ITree> getBreadthFirstTreeNodeList(){
        ArrayList<ITree> list = new ArrayList<>();
        ArrayList<ITree> temp = new ArrayList<>();
        temp.add(this);
        while (temp.size() > 0){
            ITree node = temp.remove(0);
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

    public void setParent(ITree parent) {
        this.parent = parent;
    }

    public ITree getParent() {
        return parent;
    }

    public ITree getChild(int position) {
        return getChildren().get(position);
    }

    public int getChildPosition(ITree child) {
        return getChildren().indexOf(child);
    }

    public int getPositionInParent(){
        ITree parent = getParent();
        if(parent == null)
            return -1;
        else
            return parent.getChildPosition(this);
    }

    public List<ITree> getChildren() {
        return children;
    }

    public ASTNode getASTNode() {
      return astNode;
    }

    public boolean isLeaf() {
        return getChildren().size() == 0;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isSameType(ITree node) {
        return getNodeTypeNumber() == node.getNodeTypeNumber();
    }

    public boolean isIsomorphicTo(ITree node) {
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
        for (ITree c: this.getChildren())
            b.append(c.toStaticHashString());
        b.append(")]");
        return b.toString();
    }

    public String toShortString() {
        return String.format("%d%s%s", getNodeTypeNumber(), "@@", getNodeLabel());
    }

    public String toString(){
        /*
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
        */
        return "-->" + astNode.toString() + "<--start" + startLineNumber + " end " + endLineNumber;
    }
}