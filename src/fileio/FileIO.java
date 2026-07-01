package fileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import model.Batch;
import model.LinearNode;
import model.Product;
import model.MyAVLTree;
import controller.WarehouseController;

/*
=================================================

TEAM ARCHITECTURE

FileIO chịu trách nhiệm:

CSV File

    ↓

Đọc dữ liệu

    ↓

Tạo Product + Batch

    ↓

Đưa vào AVL Tree


Ví dụ CSV:

barcode,productName,batchCode,expiryDate,quantity,price

P0001,Egg,B001,2026-07-01,30,25000
P0001,Egg,B002,2026-07-15,40,26000
P0002,Milk,B003,2026-08-01,20,30000


Load Flow:

Dòng 1:

search(P0001)

Không có

↓

create Product

↓

insert AVL

↓

addBatch(B001)


Dòng 2:

search(P0001)

Đã tồn tại

↓

addBatch(B002)


Kết quả:

AVL

        P0002
       /
    P0001

P0001

Front

B001
B002

Rear

=================================================
 */
public class FileIO {


    /*
    =================================================

    KIỆT:

    Đọc file warehouse CSV ban đầu

    Input:

    data/initial/warehouse_100.csv

    Output:

    AVL đã chứa Product và Queue Batch

    =================================================
     */
    // TODO KIỆT
    public static void loadWarehouseData(
            String filePath,
            MyAVLTree tree) {

        try {

            BufferedReader br
                    = new BufferedReader(
                            new FileReader(filePath)
                    );

            // skip header
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {

                String[] data
                        = line.split(",");

                String barcode
                        = data[0];

                String productName
                        = data[1];

                String batchCode
                        = data[2];

                LocalDate expiryDate
                        = parseFlexibleDate(data[3]);

                int quantity
                        = Integer.parseInt(data[4]);

                double price
                        = Double.parseDouble(data[5]);

                // search product
                Product product
                        = tree.search(barcode);

                // create if not exist
                if (product == null) {

                    product
                            = new Product(
                                    barcode,
                                    productName
                            );

                    tree.insert(product);

                }

                // create batch
                Batch batch
                        = new Batch(
                                batchCode,
                                expiryDate,
                                quantity,
                                price
                        );

                // add batch
                product.addBatch(batch);

            }

            br.close();

            System.out.println(
                    "Load successful."
            );

        } catch (Exception e) {

            System.out.println(
                    "Load failed: "
                    + e.getMessage()
            );

        }

    }

    /*
    =================================================

    QUANG:

    Đọc transaction file

    Ví dụ:

    ADD,P0001,Egg,B010,2026-09-01,20,25000

    DISPATCH,P0001,10

    Sau đó gọi:

    controller.add()

    hoặc

    controller.dispatch()

    =================================================
     */
    // TODO QUANG
        public static void processTransactionFile(
                        String filePath,
                        WarehouseController controller) {

                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

                        br.readLine();

                        String line;

                        while ((line = br.readLine()) != null) {

                                if (line.trim().isEmpty()) {
                                        continue;
                                }

                                String[] data = line.split(",");
                                String action = data[0].trim();

                                if ("ADD".equalsIgnoreCase(action)) {

                                        String barcode = data[1].trim();
                                        String productName = data[2].trim();
                                        String batchCode = data[3].trim();
                                        LocalDate expiryDate = parseFlexibleDate(data[4].trim());
                                        int quantity = Integer.parseInt(data[5].trim());
                                        double price = Double.parseDouble(data[6].trim());

                                        Batch batch = new Batch(
                                                        batchCode,
                                                        expiryDate,
                                                        quantity,
                                                        price);

                                        controller.addBatch(barcode, productName, batch);

                                } else if ("DISPATCH".equalsIgnoreCase(action)) {

                                        String barcode = data[1].trim();
                                        int quantity = Integer.parseInt(data[2].trim());

                                        controller.dispatchFIFO(barcode, quantity);

                                } else {

                                        System.out.println("Unknown transaction type: " + action);

                                }
                        }

                        System.out.println("Transaction processing successful.");

                } catch (Exception e) {

                        System.out.println("Transaction processing failed: " + e.getMessage());

                }

    }

        public static void processAddFile(
                        String filePath,
                        WarehouseController controller) {

                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

                        br.readLine();

                        String line;

                        while ((line = br.readLine()) != null) {

                                if (line.trim().isEmpty()) {
                                        continue;
                                }

                                String[] data = line.split(",");

                                String action = data[0].trim();

                                if (!"ADD".equalsIgnoreCase(action)) {
                                        continue;
                                }

                                String barcode = data[1].trim();
                                String productName = data[2].trim();
                                String batchCode = data[3].trim();
                                LocalDate expiryDate = parseFlexibleDate(data[4].trim());
                                int quantity = Integer.parseInt(data[5].trim());
                                double price = Double.parseDouble(data[6].trim());

                                Batch batch = new Batch(
                                                batchCode,
                                                expiryDate,
                                                quantity,
                                                price);

                                controller.addBatch(barcode, productName, batch);

                        }

                        System.out.println("Add file processing successful.");

                } catch (Exception e) {

                        System.out.println("Add file processing failed: " + e.getMessage());

                }

        }

        public static void processDispatchFile(
                        String filePath,
                        WarehouseController controller) {

                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

                        br.readLine();

                        String line;

                        while ((line = br.readLine()) != null) {

                                if (line.trim().isEmpty()) {
                                        continue;
                                }

                                String[] data = line.split(",");

                                String action = data[0].trim();

                                if (!"DISPATCH".equalsIgnoreCase(action)) {
                                        continue;
                                }

                                String barcode = data[1].trim();
                                int quantity = Integer.parseInt(data[2].trim());

                                controller.dispatchFIFO(barcode, quantity);

                        }

                        System.out.println("Dispatch file processing successful.");

                } catch (Exception e) {

                        System.out.println("Dispatch file processing failed: " + e.getMessage());

                }

        }

    /*
    =================================================

    HUY:

    Ghi dữ liệu hiện tại thành file CSV

    Dùng để:

    warehouse_updated.csv

    =================================================
     */
    // TODO HUY
    public static void saveWarehouseData(
            String filePath,
            List<Product> products) {

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

                        bw.write("barcode,productName,batchCode,expiryDate,quantity,price");
                        bw.newLine();

                        for (Product product : products) {

                                String csvRows = convertProductToCSV(product);

                                if (!csvRows.isEmpty()) {
                                        bw.write(csvRows);
                                }
                        }

                        System.out.println("Save successful.");

                } catch (Exception e) {

                        System.out.println("Save failed: " + e.getMessage());

                }

    }

        public static void saveExpiringProductsData(
                        String filePath,
                        List<Product> products,
                        int days) {

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

                        bw.write("barcode,productName,batchCode,expiryDate,quantity,price");
                        bw.newLine();

                        for (Product product : products) {
                                LinearNode<Batch> current = product.getBatchQueue().getFront();

                                while (current != null) {
                                        Batch batch = current.getData();

                                        if (batch.isExpiringSoon(days)) {
                                                bw.write(product.getBarcode()
                                                                + "," + product.getName()
                                                                + "," + batch.getBatchCode()
                                                                + "," + batch.getExpiryDate()
                                                                + "," + batch.getQuantity()
                                                                + "," + batch.getPrice());
                                                bw.newLine();
                                        }

                                        current = current.getNext();
                                }
                        }

                        System.out.println("Export expiring products successful.");

                } catch (Exception e) {

                        System.out.println("Export expiring products failed: " + e.getMessage());

                }
        }

    /*
    =================================================

    HUY:

    Chuyển Product + Queue<Batch>

    thành CSV row

    Ví dụ:

    P0001,Egg,B001,2026-07-01,30,25000

    P0001,Egg,B002,2026-07-15,40,26000

    =================================================
     */
    // TODO HUY
    private static String convertProductToCSV(
            Product product) {

                StringBuilder builder = new StringBuilder();

                LinearNode<Batch> current = product.getBatchQueue().getFront();

                while (current != null) {

                        Batch batch = current.getData();

                        builder.append(product.getBarcode())
                                        .append(',')
                                        .append(product.getName())
                                        .append(',')
                                        .append(batch.getBatchCode())
                                        .append(',')
                                        .append(batch.getExpiryDate())
                                        .append(',')
                                        .append(batch.getQuantity())
                                        .append(',')
                                        .append(batch.getPrice())
                                        .append(System.lineSeparator());

                        current = current.getNext();

                }

                return builder.toString();

    }

        private static LocalDate parseFlexibleDate(String value) {

                String trimmedValue = value.trim();

                try {
                        return LocalDate.parse(trimmedValue);
                } catch (DateTimeParseException ignored) {
                        return LocalDate.parse(
                                        trimmedValue,
                                        DateTimeFormatter.ofPattern("M/d/yyyy")
                        );
                }

        }

}
