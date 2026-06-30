package controller;

import model.Batch;
import model.MyAVLTree;
import model.Product;
// Import TreeNode giả định theo cấu trúc của Kiệt
// import model.TreeNode; 

public class WarehouseController {

    private MyAVLTree avlIndex;

    public WarehouseController(){
        avlIndex = new MyAVLTree();
    }

    /*
    =================================================
    KIỆT:
    Search product bằng AVL
    =================================================
    */

    // TODO KIỆT
    public void searchProduct(){
        // Phần của Kiệt: duyệt cây AVL để hiển thị hoặc trả về Product
    }

    // TODO KIỆT
    public void displayAllProducts(){
        // Phần của Kiệt: InOrder traversal cây AVL để in toàn bộ sản phẩm
    }


    /*
    =================================================
    HUY:
    Search Product -> lấy batchQueue -> enqueue batch -> dispatch FIFO
    =================================================
    */

    // TODO HUY
    /**
     * Hàm thêm một lô hàng mới vào sản phẩm (Nhập kho).
     * @param barcode Mã sản phẩm cần nhập thêm lô (ví dụ: "P001")
     * @param newBatch Lô hàng mới (ví dụ: Batch B004)
     */
    public void addBatch(String barcode, Batch newBatch) {
        /*
         * BƯỚC 1: Dùng hàm của Kiệt để tìm Product.
         * (Giả sử avlIndex có hàm search(barcode) trả về node chứa Product)
         * * Ví dụ: 
         * TreeNode node = avlIndex.search(barcode);
         * if (node == null) { System.out.println("Product not found!"); return; }
         * Product targetProduct = node.getProduct();
         */
        
        // Mock Product để Huy test logic Queue (Xóa phần mock này khi ráp code với Kiệt)
        Product targetProduct = new Product(barcode, "Mock Product"); 
        
        // BƯỚC 2: Thêm Batch vào Queue
        targetProduct.addBatch(newBatch);
        System.out.println("Đã nhập thành công lô hàng " + newBatch.getBatchCode() 
                         + " vào sản phẩm " + barcode);
    }


    // TODO HUY
    /**
     * Hàm xuất kho (Dispatch) tuân thủ nghiêm ngặt nguyên tắc FIFO.
     * @param barcode Mã sản phẩm cần xuất (ví dụ: "P001")
     * @param dispatchQuantity Số lượng cần xuất (ví dụ: 50)
     */
    public void dispatchFIFO(String barcode, int dispatchQuantity) {
        /*
         * BƯỚC 1: Tìm Product (Tương tự như addBatch, cần hàm của Kiệt)
         */
        // Mock Product tạm thời (Xóa khi ráp code)
        Product targetProduct = new Product(barcode, "Mock Product"); 
        
        System.out.println("\nYêu cầu xuất " + dispatchQuantity + " sản phẩm (" + barcode + ").");

        // BƯỚC 2: Kiểm tra tổng tồn kho
        int totalAvailable = targetProduct.getTotalQuantity();
        if (totalAvailable < dispatchQuantity) {
            System.out.println("Lỗi: Không đủ hàng trong kho! (Tồn: " + totalAvailable + ", Yêu cầu: " + dispatchQuantity + ")");
            return;
        }

        // BƯỚC 3: Xử lý rút hàng theo FIFO
        int remainingToDispatch = dispatchQuantity;

        while (remainingToDispatch > 0 && !targetProduct.getBatchQueue().isEmpty()) {
            // Lấy lô hàng ở đầu (Front) ra xem (peek)
            Batch frontBatch = targetProduct.getBatchQueue().peek();
            int batchQty = frontBatch.getQuantity();

            if (batchQty <= remainingToDispatch) {
                // TRƯỜNG HỢP 1: Lô hàng hiện tại nhỏ hơn hoặc bằng số lượng cần lấy
                // Lấy sạch toàn bộ lô hàng này
                remainingToDispatch -= batchQty;
                
                // Lấy ra khỏi Queue (Dequeue)
                targetProduct.getBatchQueue().dequeue();
                
                System.out.println(" -> Đã xuất " + batchQty + " từ lô [" + frontBatch.getBatchCode() + "] (Lô này đã xuất hết).");
            } else {
                // TRƯỜNG HỢP 2: Lô hàng hiện tại lớn hơn số lượng cần lấy
                // Chỉ trừ đi phần cần thiết, KHÔNG dequeue lô này
                frontBatch.setQuantity(batchQty - remainingToDispatch);
                
                System.out.println(" -> Đã xuất " + remainingToDispatch + " từ lô [" + frontBatch.getBatchCode() + "] (Lô này còn lại: " + frontBatch.getQuantity() + ").");
                
                // Đã lấy đủ hàng
                remainingToDispatch = 0;
            }
        }

        System.out.println("Hoàn tất xuất hàng! Tổng tồn kho hiện tại: " + targetProduct.getTotalQuantity());
    }

}