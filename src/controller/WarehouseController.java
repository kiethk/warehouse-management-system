package controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import model.LinearNode;
import model.Batch;
import model.MyAVLTree;
import model.Product;
import java.util.List;
// Import TreeNode giả định theo cấu trúc của Kiệt
// import model.TreeNode; 

public class WarehouseController {

    private MyAVLTree avlIndex;

    public WarehouseController() {
        this(new MyAVLTree());
    }

    public WarehouseController(MyAVLTree avlIndex) {
        this.avlIndex = (avlIndex == null)
                ? new MyAVLTree()
                : avlIndex;
    }

    /*
    =================================================
    KIỆT:
    Search product bằng AVL
    =================================================
     */
    // TODO KIỆT
    public Product searchProduct(String barcode) {

        return avlIndex.search(barcode);

    }

    public void searchBatch(String batchCode) {

        List<Product> products = getAllProducts();

        if (products.isEmpty()) {
            System.out.println("Warehouse is empty");
            return;
        }

        for (Product product : products) {
            LinearNode<Batch> current = product.getBatchQueue().getFront();

            while (current != null) {
                Batch batch = current.getData();

                if (batch.getBatchCode().equalsIgnoreCase(batchCode)) {
                    System.out.println("Batch found:");
                    System.out.println("Product Barcode: " + product.getBarcode());
                    System.out.println("Product Name: " + product.getName());
                    System.out.println("Batch Code: " + batch.getBatchCode());
                    System.out.println("Expiry Date: " + batch.getExpiryDate());
                    System.out.println("Quantity: " + batch.getQuantity());
                    System.out.println("Price: " + batch.getPrice());
                    return;
                }

                current = current.getNext();
            }
        }

        System.out.println("Batch not found: " + batchCode);
    }

    // TODO KIỆT
    public void displayAllProducts() {

        if (avlIndex.getHeight() == 0) {

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

    public void displayExpiringProducts(int days) {

        if (days < 0) {
            System.out.println("Days must be >= 0.");
            return;
        }

        List<Product> products = getAllProducts();

        if (products.isEmpty()) {
            System.out.println("Warehouse is empty");
            return;
        }

        System.out.println();
        System.out.println("===== EXPIRING BATCHES (" + days + " DAYS) =====");
        System.out.printf("%-10s %-15s %-12s %-12s %-10s %-10s%n",
                "Barcode",
                "Product",
                "Batch",
                "Expiry",
                "Qty",
                "Status");

        int matchCount = 0;
        LocalDate today = LocalDate.now();

        for (Product product : products) {
            LinearNode<Batch> current = product.getBatchQueue().getFront();

            while (current != null) {
                Batch batch = current.getData();

                if (batch.isExpiringSoon(days)) {
                    long dayDiff = ChronoUnit.DAYS.between(today, batch.getExpiryDate());
                    String status = (dayDiff < 0)
                            ? "Expired"
                            : dayDiff + " day(s)";

                    System.out.printf("%-10s %-15s %-12s %-12s %-10d %-10s%n",
                            product.getBarcode(),
                            product.getName(),
                            batch.getBatchCode(),
                            batch.getExpiryDate(),
                            batch.getQuantity(),
                            status);

                    matchCount++;
                }

                current = current.getNext();
            }
        }

        if (matchCount == 0) {
            System.out.println("No expiring batches found.");
        }
    }

    public List<Product> getAllProducts() {
        return avlIndex.toProductList();
    }

    public void addBatch(String barcode, String productName, Batch newBatch) {
        Product targetProduct = searchProduct(barcode);

        if (targetProduct == null) {
            targetProduct = new Product(barcode, productName);
            avlIndex.insert(targetProduct);
        }

        targetProduct.addBatch(newBatch);
        System.out.println("Đã nhập thành công lô hàng " + newBatch.getBatchCode()
                + " vào sản phẩm " + barcode);
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
     *
     * @param barcode Mã sản phẩm cần nhập thêm lô (ví dụ: "P001")
     * @param newBatch Lô hàng mới (ví dụ: Batch B004)
     */
    public void addBatch(String barcode, Batch newBatch) {
        Product targetProduct = searchProduct(barcode);

        if (targetProduct == null) {
            System.out.println("Product not found: " + barcode);
            return;
        }

        // BƯỚC 2: Thêm Batch vào Queue
        targetProduct.addBatch(newBatch);
        System.out.println("Đã nhập thành công lô hàng " + newBatch.getBatchCode()
                + " vào sản phẩm " + barcode);
    }

    // TODO HUY
    /**
     * Hàm xuất kho (Dispatch) tuân thủ nghiêm ngặt nguyên tắc FIFO.
     *
     * @param barcode Mã sản phẩm cần xuất (ví dụ: "P001")
     * @param dispatchQuantity Số lượng cần xuất (ví dụ: 50)
     */
    public void dispatchFIFO(String barcode, int dispatchQuantity) {
        Product targetProduct = searchProduct(barcode);

        if (targetProduct == null) {
            System.out.println("Product not found: " + barcode);
            return;
        }

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

        if (targetProduct.getBatchQueue().isEmpty()) {
            avlIndex.delete(barcode);
            System.out.println("Sản phẩm " + barcode + " đã hết hàng và được xóa khỏi kho.");
        }
    }

}
