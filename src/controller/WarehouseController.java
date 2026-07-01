package controller;

import model.MyAVLTree;
import model.Product;

public class WarehouseController {

    private MyAVLTree avlIndex;

    public WarehouseController(){

        avlIndex = new MyAVLTree();

    }

    /*
    =================================================

    KIỆT:

    Search product bằng AVL

    Time Complexity:

    O(log n)

    Example:

    search("P0002")

    AVL:

            P003
           /    \
        P001    P005
            \
            P002

    Result:

    Product(P0002)

    =================================================
    */

    public Product searchProduct(String barcode){

        return avlIndex.search(barcode);

    }


    /*
    =================================================

    KIỆT:

    Display toàn bộ Product

    Dùng:

    AVL in-order traversal

    Kết quả:

    P001
    P002
    P003
    P004

    Time Complexity:

    O(n)

    =================================================
    */

    public void displayAllProducts(){

        if(avlIndex.getHeight()==0){

            System.out.println(
                    "Warehouse is empty"
            );

            return;

        }

        System.out.println();
        System.out.println(
                "===== PRODUCT LIST ====="
        );

        avlIndex.inOrder();

    }


    /*
    =================================================

    KIỆT:

    Insert Product vào AVL

    Chỉ insert Product mới.

    Nếu Product đã tồn tại:

        không insert lại

    =================================================
    */

    public void addProduct(Product product){

        Product existing=
                searchProduct(
                        product.getBarcode()
                );

        if(existing!=null){

            System.out.println(
                    "Barcode already exists"
            );

            return;

        }

        avlIndex.insert(product);

        System.out.println(
                "Product added successfully"
        );

    }



    /*
    =================================================

    HUY:

    Search Product

    ↓

    lấy batchQueue

    ↓

    enqueue batch

    ↓

    dispatch FIFO

    =================================================
    */


    // TODO HUY
    public void addBatch(){

    }


    // TODO HUY
    public void dispatchFIFO(){

    }


    /*
    =================================================

    KIỆT:

    Getter để Main hoặc FileIO
    truy cập AVL khi cần

    =================================================
    */

    public MyAVLTree getAvlIndex() {

        return avlIndex;

    }

}