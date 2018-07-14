package fdse.zc.gumtree.java;

import java.security.MessageDigest;

public class HashGenerator {
    public static final int BASE = 33;

    public void hash(TreeNode node) {
        if(node.isLeaf()){
            node.setHash(leafHash(node));
            return;
        }
        for(TreeNode c : node.getChildren()){
            hash(c);
        }
        node.setHash(innerNodeHash(node));
    }

    public int leafHash(TreeNode node) {
        return BASE * hashFunction(inSeed(node)) + hashFunction(outSeed(node));
    }

    public int innerNodeHash(TreeNode node) {
        int size = node.getSize() * 2 - 1;
        int hash = hashFunction(inSeed(node)) * fpow(BASE, size);

        for (TreeNode c: node.getChildren()) {
            size = size - c.getSize() * 2;
            hash += c.getHash() * fpow(BASE, size);
        }

        hash += hashFunction(outSeed(node));
        return hash;
    }

    public static String inSeed(TreeNode node) {
        return "[(" + node.getNodeLabel() + "@@" + node.getNodeTypeName();
    }

    public static String outSeed(TreeNode node) {
        return  node.getNodeTypeName() + "@@" + node.getNodeLabel() + ")]";
    }

    public int hashFunction(String s) {
        return md5(s);
    }

    public int md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(s.getBytes("UTF-8"));
            return byteArrayToInt(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

    public int fpow(int a, int b) {
        if (b == 1)
            return a;
        int result = 1;
        while (b > 0) {
            if ((b & 1) != 0)
                result *= a;
            b >>= 1;
            a *= a;
        }
        return result;
    }
}
