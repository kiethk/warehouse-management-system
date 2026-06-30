package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Batch {

    private String batchCode;
    private LocalDate expiryDate;
    private int quantity;
    private double price;

    /*
    =================================================

    HUY:

    Batch đại diện cho một lô hàng.

    Ví dụ:

    Batch B001

    quantity = 30
    expiryDate = 2026-07-01
    price = 25000

    Product Egg:

        Front

        B001
        B002
        B003

        Rear

    Queue sẽ quản lý thứ tự nhập/xuất.

    =================================================
    */


    // TODO HUY:
    // constructor
    public Batch(String batchCode, LocalDate expiryDate, int quantity, double price) {
        this.batchCode = batchCode;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.price = price;
    }


    // TODO HUY:
    // getter/setter
    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    // TODO HUY:
    // isExpiringSoon(int days)
    /**
     * Kiểm tra xem lô hàng có sắp hết hạn trong vòng số 'days' ngày hay không.
     * Logic: Tính khoảng cách từ ngày hiện tại (LocalDate.now()) đến ngày hết hạn (expiryDate).
     * Nếu khoảng cách nhỏ hơn hoặc bằng số 'days' truyền vào VÀ lô hàng chưa bị quá hạn,
     * hoặc nếu lô hàng đã quá hạn rồi (ngày hết hạn trước ngày hiện tại), đều trả về true.
     */
    public boolean isExpiringSoon(int days) {
        LocalDate today = LocalDate.now();
        
        // Nếu đã quá hạn tính đến hôm nay
        if (expiryDate.isBefore(today)) {
            return true;
        }
        
        // Tính số ngày còn lại từ hôm nay đến ngày hết hạn
        long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);
        
        // Nếu số ngày còn lại nằm trong khoảng cảnh báo (nhỏ hơn hoặc bằng số ngày yêu cầu)
        return daysUntilExpiry <= days;
    }


    // TODO HUY:
    // display()
    public void display() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchCode='" + batchCode + '\'' +
                ", expiryDate=" + expiryDate +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}