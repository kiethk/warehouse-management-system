package fileio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;
import model.Batch;
import model.Product;
import model.MyAVLTree;

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
                        = LocalDate.parse(data[3]);

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
            String filePath) {

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

        return "";

    }

}
