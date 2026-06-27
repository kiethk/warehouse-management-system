package model;

import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.TreeNode;

public class MyAVLTree {

    private TreeNode root;

    /*
    =================================================

    KIỆT:

    Key:
    product.getBarcode()

    Example:

            P003
           /    \
        P001    P005

    Purpose:

    - Search product O(log n)
    - Insert product
    - Delete product
    - Display product list

    =================================================
    */

    public MyAVLTree() {
        root = null;
    }

    // =====================================================
    // PUBLIC METHODS
    // =====================================================

    /*
        Insert product vào cây AVL
    */
    public void insert(Product product) {
        root = insertRecursive(root, product);
    }

    /*
        Xóa product theo barcode
    */
    public void delete(String barcode) {
        root = deleteRecursive(root, barcode);
    }

    /*
        Search product theo barcode

        Return:
            Product nếu tìm thấy
            null nếu không tìm thấy
    */
    public Product search(String barcode) {

        TreeNode node = searchRecursive(root, barcode);

        return (node == null)
                ? null
                : node.data;
    }

    /*
        In-order traversal

        Kết quả:
        P001
        P002
        P003
        P004
    */
    public void inOrder() {
        inOrderRecursive(root);
    }

    /*
        Hiển thị chiều cao cây
    */
    public int getHeight() {
        return getHeight(root);
    }

    /*
        Display cây dạng text
    */
    public void displayTreeStructure() {

        System.out.println("\nAVL Tree:");

        displayTree(root, "", true);

    }


    // =====================================================
    // INSERT
    // =====================================================

    private TreeNode insertRecursive(
            TreeNode root,
            Product product) {

        // BST insert

        if (root == null) {
            return new TreeNode(product);
        }

        int cmp = product.getBarcode()
                .compareTo(
                        root.data.getBarcode()
                );

        if (cmp < 0) {

            root.left =
                    insertRecursive(
                            root.left,
                            product
                    );

        } else if (cmp > 0) {

            root.right =
                    insertRecursive(
                            root.right,
                            product
                    );

        } else {

            // duplicate barcode

            return root;
        }


        // update height

        root.height = Math.max(
                getHeight(root.left),
                getHeight(root.right)
        ) + 1;


        // rebalance

        int balance =
                getBalanceFactor(root);


        // LL

        if (balance > 1
                && product.getBarcode()
                        .compareTo(
                                root.left.data.getBarcode()
                        ) < 0) {

            return rotateRight(root);

        }

        // RR

        if (balance < -1
                && product.getBarcode()
                        .compareTo(
                                root.right.data.getBarcode()
                        ) > 0) {

            return rotateLeft(root);

        }

        // LR

        if (balance > 1
                && product.getBarcode()
                        .compareTo(
                                root.left.data.getBarcode()
                        ) > 0) {

            root.left =
                    rotateLeft(root.left);

            return rotateRight(root);

        }

        // RL

        if (balance < -1
                && product.getBarcode()
                        .compareTo(
                                root.right.data.getBarcode()
                        ) < 0) {

            root.right =
                    rotateRight(root.right);

            return rotateLeft(root);

        }

        return root;

    }


    // =====================================================
    // DELETE
    // =====================================================

    private TreeNode deleteRecursive(
            TreeNode root,
            String barcode) {

        if (root == null) {
            return null;
        }

        int cmp =
                barcode.compareTo(
                        root.data.getBarcode()
                );

        if (cmp < 0) {

            root.left =
                    deleteRecursive(
                            root.left,
                            barcode
                    );

        } else if (cmp > 0) {

            root.right =
                    deleteRecursive(
                            root.right,
                            barcode
                    );

        } else {

            // found

            if (root.left == null) {
                return root.right;
            }

            if (root.right == null) {
                return root.left;
            }

            TreeNode successor =
                    findMin(root.right);

            root.data =
                    successor.data;

            root.right =
                    deleteRecursive(
                            root.right,
                            successor.data.getBarcode()
                    );

        }


        root.height = Math.max(
                getHeight(root.left),
                getHeight(root.right)
        ) + 1;

        int balance =
                getBalanceFactor(root);


        // LL

        if (balance > 1
                && getBalanceFactor(root.left) >= 0) {

            return rotateRight(root);

        }

        // LR

        if (balance > 1
                && getBalanceFactor(root.left) < 0) {

            root.left =
                    rotateLeft(root.left);

            return rotateRight(root);

        }

        // RR

        if (balance < -1
                && getBalanceFactor(root.right) <= 0) {

            return rotateLeft(root);

        }

        // RL

        if (balance < -1
                && getBalanceFactor(root.right) > 0) {

            root.right =
                    rotateRight(root.right);

            return rotateLeft(root);

        }

        return root;

    }


    // =====================================================
    // SEARCH
    // =====================================================

    private TreeNode searchRecursive(
            TreeNode root,
            String barcode) {

        if (root == null) {
            return null;
        }

        int cmp =
                barcode.compareTo(
                        root.data.getBarcode()
                );

        if (cmp == 0) {
            return root;
        }

        if (cmp < 0) {

            return searchRecursive(
                    root.left,
                    barcode
            );

        }

        return searchRecursive(
                root.right,
                barcode
        );

    }


    // =====================================================
    // TRAVERSAL
    // =====================================================

    private void inOrderRecursive(
            TreeNode root) {

        if (root == null) {
            return;
        }

        inOrderRecursive(root.left);

        root.data.display();

        inOrderRecursive(root.right);

    }


    // =====================================================
    // ROTATION
    // =====================================================

    private TreeNode rotateRight(TreeNode y) {

        TreeNode x = y.left;

        TreeNode T2 = x.right;

        x.right = y;

        y.left = T2;

        y.height =
                Math.max(
                        getHeight(y.left),
                        getHeight(y.right)
                ) + 1;

        x.height =
                Math.max(
                        getHeight(x.left),
                        getHeight(x.right)
                ) + 1;

        return x;

    }


    private TreeNode rotateLeft(TreeNode x) {

        TreeNode y = x.right;

        TreeNode T2 = y.left;

        y.left = x;

        x.right = T2;

        x.height =
                Math.max(
                        getHeight(x.left),
                        getHeight(x.right)
                ) + 1;

        y.height =
                Math.max(
                        getHeight(y.left),
                        getHeight(y.right)
                ) + 1;

        return y;

    }


    // =====================================================
    // HELPERS
    // =====================================================

    private int getHeight(TreeNode node) {

        return (node == null)
                ? 0
                : node.height;

    }


    private int getBalanceFactor(
            TreeNode node) {

        return (node == null)
                ? 0
                : getHeight(node.left)
                - getHeight(node.right);

    }


    private TreeNode findMin(
            TreeNode node) {

        while (node.left != null) {

            node = node.left;

        }

        return node;

    }


    private void displayTree(
            TreeNode node,
            String indent,
            boolean last) {

        if (node != null) {

            System.out.print(indent);

            if (last) {

                System.out.print("└── ");

                indent += "    ";

            } else {

                System.out.print("├── ");

                indent += "|   ";

            }

            System.out.println(
                    node.data.getBarcode()
            );

            displayTree(
                    node.left,
                    indent,
                    false
            );

            displayTree(
                    node.right,
                    indent,
                    true
            );

        }

    }

}