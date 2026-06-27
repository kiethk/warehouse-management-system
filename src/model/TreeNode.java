package model;

public class TreeNode {

    Product data;

    TreeNode left;

    TreeNode right;

    int height;

    public TreeNode() {
    }

    public TreeNode(Product data, TreeNode left, TreeNode right, int height) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.height = height;
    }

    public TreeNode(Product data) {

        this.data = data;
        this.left = null;
        this.right = null;
        this.height = 1;

    }

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
