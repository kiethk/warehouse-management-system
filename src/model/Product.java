package model;

public class Product {

    private String barcode;

    private String name;

    private MyQueue<Batch> batchQueue;

    /*
        HUY:
        Queue sẽ chứa Batch

        Front

        B001
        B002
        B003

        Rear
    */

    public Product() {
        this.batchQueue = new MyQueue<>();
    }

    // Constructor đầy đủ
    public Product(String barcode, String name) {
        this.barcode = barcode;
        this.name = name;

        this.batchQueue = new MyQueue<>();
    }

    /*
        KIỆT:
        Khi nhập thêm lô hàng:

        P0001 Egg

        enqueue(B001)
        enqueue(B002)

        Queue của Egg:

        Front

        B001
        B002

        Rear
     */
    public void addBatch(Batch batch) {
        batchQueue.enqueue(batch); // Đã mở khóa việc thêm Batch vào Queue
    }

    /*
        KIỆT:

        Tổng số lượng của Product
        = tổng quantity của tất cả Batch

        Ví dụ:

        B001 = 30
        B002 = 40
        B003 = 20

        Total = 90

        HUY:
        Queue cần hỗ trợ duyệt node để hàm này hoạt động
    */
    public int getTotalQuantity() {
        int total = 0;

        // Bắt đầu duyệt từ node đầu tiên (Front) của Queue
        LinearNode<Batch> current = batchQueue.getFront();

        // Duyệt qua từng node cho đến khi kết thúc (current == null)
        while (current != null) {
            // Lấy data (Batch) từ node hiện tại, sau đó cộng dồn quantity
            total += current.getData().getQuantity();
            
            // Di chuyển sang node tiếp theo
            current = current.getNext();
        }

        return total;
    }

    public void display() {

        System.out.printf(
                "%-10s %-20s %-10d\n",
                barcode,
                name,
                getTotalQuantity()
        );

    }


    // ==========================
    // Getter / Setter
    // ==========================

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyQueue<Batch> getBatchQueue() {
        return batchQueue;
    }

    public void setBatchQueue(MyQueue<Batch> batchQueue) {
        this.batchQueue = batchQueue;
    }


    @Override
    public String toString() {
        return "Product{"
                + "barcode='" + barcode + '\''
                + ", name='" + name + '\''
                + ", totalQuantity=" + getTotalQuantity()
                + '}';
    }

}