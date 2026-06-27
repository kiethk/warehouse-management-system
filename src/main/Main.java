package main;

import model.MyAVLTree;
import model.Product;

public class Main {

    public static void main(String[] args) {

        MyAVLTree tree = new MyAVLTree();

        tree.insert(
                new Product(
                        "P003",
                        "Milk"
                )
        );

        tree.insert(
                new Product(
                        "P001",
                        "Egg"
                )
        );

        tree.insert(
                new Product(
                        "P005",
                        "Soap"
                )
        );

        tree.insert(
                new Product(
                        "P002",
                        "Shampoo"
                )
        );

        System.out.println("===== In-order =====");

        tree.inOrder();

        System.out.println();

        System.out.println(
                "Tree Height: "
                + tree.getHeight()
        );

        System.out.println();

        tree.displayTreeStructure();

        System.out.println();

        Product p =
                tree.search("P002");

        if(p!=null){

            System.out.println(
                    "Found: "
                    + p
            );

        }
        else{

            System.out.println(
                    "Not found"
            );

        }

    }

}