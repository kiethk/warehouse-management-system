package main;

import controller.WarehouseController;
import model.Batch;
import model.MyAVLTree;
import model.Product;
import fileio.FileIO;
import view.MenuView;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // =========================
        // Khởi tạo cây AVL
        // =========================
        MyAVLTree tree = new MyAVLTree();

        String initialFilePath = chooseInitialWarehouseFile(sc);
        FileIO.loadWarehouseData(initialFilePath, tree);

        // Truyền AVL vào Controller
        WarehouseController controller = new WarehouseController(tree);

        int choice;

        do {

            MenuView.displayMainMenu();

            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:

                    System.out.println("\n===== ADD BATCH =====");

                    System.out.print("Product Barcode: ");
                    String barcode = sc.nextLine();

                    System.out.print("Batch Code: ");
                    String batchCode = sc.nextLine();

                    System.out.print("Expiry Date (yyyy-MM-dd): ");
                    LocalDate expiryDate = LocalDate.parse(sc.nextLine());

                    System.out.print("Quantity: ");
                    int quantity = Integer.parseInt(sc.nextLine());

                    System.out.print("Price: ");
                    double price = Double.parseDouble(sc.nextLine());

                    Batch batch = new Batch(
                            batchCode,
                            expiryDate,
                            quantity,
                            price);

                    controller.addBatch(barcode, batch);

                    break;
                case 2:

                    System.out.println("\n===== ADD FROM CSV FILE =====");

                    String addFilePath = chooseAddFile(sc);
                    FileIO.processAddFile(
                            addFilePath,
                            controller);

                    break;

                case 3:

                    System.out.println("\n===== DISPATCH PRODUCT =====");

                    System.out.print("Product Barcode: ");
                    barcode = sc.nextLine();

                    System.out.print("Dispatch Quantity: ");
                    quantity = Integer.parseInt(sc.nextLine());

                    controller.dispatchFIFO(
                            barcode,
                            quantity);

                    break;

                case 4:

                    System.out.println("\n===== BULK DISPATCH FROM CSV =====");

                    String dispatchFilePath = chooseDispatchFile(sc);
                    FileIO.processDispatchFile(
                            dispatchFilePath,
                            controller);

                    break;

                case 5:

                    System.out.println("\n===== SEARCH PRODUCT =====");

                    System.out.print("Product Barcode: ");
                    barcode = sc.nextLine();

                    Product foundProduct = controller.searchProduct(barcode);

                    if (foundProduct == null) {
                        System.out.println("Product not found!");
                    } else {
                        foundProduct.display();
                    }

                    break;

                case 6:

                    System.out.println("\n===== SEARCH BATCH =====");

                    System.out.print("Batch Code: ");
                    String searchBatchCode = sc.nextLine();

                    controller.searchBatch(searchBatchCode);

                    break;

                case 7:

                    System.out.println("\n===== DISPLAY ALL PRODUCTS =====");

                    controller.displayAllProducts();

                    break;

                case 8:

                    System.out.println("\n===== DISPLAY EXPIRING PRODUCTS =====");

                    System.out.print("Enter warning days (e.g. 7): ");
                    int warningDays = Integer.parseInt(sc.nextLine());

                    controller.displayExpiringProducts(warningDays);

                    break;

                case 9:

                    System.out.println("\n===== EXPORT EXPIRING PRODUCTS TO CSV =====");

                    System.out.print("Enter warning days (e.g. 7): ");
                    warningDays = Integer.parseInt(sc.nextLine());

                    FileIO.saveExpiringProductsData(
                            "data/output/expiring_products.csv",
                            controller.getAllProducts(),
                            warningDays);

                    break;

                case 10:

                    System.out.println("\n===== SAVE DATA =====");

                    FileIO.saveWarehouseData(
                            "data/output/warehouse_updated.csv",
                            controller.getAllProducts());

                    break;

                case 0:

                    System.out.println("Exiting program...");

                    break;

                default:

                    System.out.println("Invalid choice!");

            }

        } while (choice != 0);

        sc.close();

    }

    private static String chooseInitialWarehouseFile(Scanner sc) {

        System.out.println("Choose initial warehouse file:");
        System.out.println("1. warehouse_100.csv");
        System.out.println("2. warehouse_500.csv");
        System.out.println("3. warehouse_1000.csv");
        System.out.println("4. warehouse_5000.csv");
        System.out.println("5. warehouse_10000.csv");
        System.out.print("Enter your choice: ");

        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                return "data/initial/warehouse_100.csv";
            case "2":
                return "data/initial/warehouse_500.csv";
            case "3":
                return "data/initial/warehouse_1000.csv";
            case "4":
                return "data/initial/warehouse_5000.csv";
            case "5":
                return "data/initial/warehouse_10000.csv";
            default:
                System.out.println("Invalid choice, defaulting to warehouse_100.csv");
                return "data/initial/warehouse_100.csv";
        }

    }

    private static String chooseAddFile(Scanner sc) {

        System.out.println("Choose add CSV file:");
        System.out.println("1. add_100.csv");
        System.out.println("2. add_500.csv");
        System.out.println("3. add_1000.csv");
        System.out.print("Enter your choice: ");

        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                return "data/transactions/add_100.csv";
            case "2":
                return "data/transactions/add_500.csv";
            case "3":
                return "data/transactions/add_1000.csv";
            default:
                System.out.println("Invalid choice, defaulting to add_100.csv");
                return "data/transactions/add_100.csv";
        }

    }

    private static String chooseDispatchFile(Scanner sc) {

        System.out.println("Choose dispatch CSV file:");
        System.out.println("1. dispatch_100.csv");
        System.out.println("2. dispatch_500.csv");
        System.out.println("3. dispatch_1000.csv");
        System.out.print("Enter your choice: ");

        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                return "data/transactions/dispatch_100.csv";
            case "2":
                return "data/transactions/dispatch_500.csv";
            case "3":
                return "data/transactions/dispatch_1000.csv";
            default:
                System.out.println("Invalid choice, defaulting to dispatch_100.csv");
                return "data/transactions/dispatch_100.csv";
        }

    }

}