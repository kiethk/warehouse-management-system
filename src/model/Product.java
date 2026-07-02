package model;

public class Product {

    private String barcode;

    private String name;

    private MyQueue<Batch> batchQueue;


    public Product() {
        this.batchQueue = new MyQueue<>();
    }

    // Constructor đầy đủ
    public Product(String barcode, String name) {
        this.barcode = barcode;
        this.name = name;

        this.batchQueue = new MyQueue<>();
    }

    public void addBatch(Batch batch) {
        batchQueue.enqueue(batch); // Đã mở khóa việc thêm Batch vào Queue
    }


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