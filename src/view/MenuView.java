package view;

public class MenuView {

    /*
     * =================================================
     * 
     * QUANG:
     * 
     * Menu dùng để test toàn bộ hệ thống
     * 
     * Chức năng:
     * 
     * 1 Add Batch
     * 
     * 2 Dispatch Product
     * 
     * 3 Search Product
     * 
     * 4 Display All Products
     * 
     * 5 Display Expiring Products
     * 
     * 6 Save Data
     * 
     * 7 Load Data
     * 
     * 0 Exit
     * 
     * =================================================
     */

    // TODO QUANG
    public static void displayMainMenu() {

        System.out.println();
        System.out.println("========== WAREHOUSE MENU ==========");
        System.out.println("1. Add Batch (Manual)");
        System.out.println("2. Bulk Add from CSV");
        System.out.println("3. Dispatch Product (Manual)");
        System.out.println("4. Bulk Dispatch from CSV");
        System.out.println("5. Search Product");
        System.out.println("6. Search Batch");
        System.out.println("7. Display All Products");
        System.out.println("8. Display Expiring Products");
        System.out.println("9. Export Expiring Products to CSV");
        System.out.println("10. Save Data");
        System.out.println("0. Exit");
        System.out.println("====================================");

    }

}