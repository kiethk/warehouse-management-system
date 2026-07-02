package benchmark;

import fileio.FileIO;
import model.MyAVLTree;
import model.Product;

import java.util.List;
import java.util.Random;

public class PerformanceTest {

    /*
    ==================================================
    Linear Search
    Baseline algorithm for comparison
    Time Complexity: O(n)
    ==================================================
     */
    public static Product linearSearch(
            List<Product> products,
            String barcode) {

        for (Product p : products) {

            if (p.getBarcode().equals(barcode)) {
                return p;
            }

        }

        return null;
    }


    public static void main(String[] args) {

        testDataset("data/benchmark/benchmark_100.csv");

        testDataset("data/benchmark/benchmark_500.csv");

        testDataset("data/benchmark/benchmark_1000.csv");

        testDataset("data/benchmark/benchmark_5000.csv");

        testDataset("data/benchmark/benchmark_10000.csv");

    }


    public static void testDataset(String filePath) {

        System.out.println();
        System.out.println("==========================");
        System.out.println(filePath);
        System.out.println("==========================");

        MyAVLTree tree = new MyAVLTree();

        /*
        ==========================
        Load dataset
        ==========================
         */
        FileIO.loadWarehouseData(
                filePath,
                tree
        );

        List<Product> products =
                tree.toProductList();

        Random rd = new Random();

        /*
        ==================================
        Generate random search targets
        ==================================
         */
        int testCount = 10000;

        String[] barcodes =
                new String[testCount];

        for (int i = 0; i < testCount; i++) {

            Product p =
                    products.get(
                            rd.nextInt(products.size())
                    );

            barcodes[i] =
                    p.getBarcode();

        }


        /*
        ==================================
        Warm-up JVM
        (avoid JIT influence)
        ==================================
         */
        for(int i=0;i<1000;i++){

            tree.search(
                    barcodes[
                            rd.nextInt(testCount)
                    ]
            );

            linearSearch(
                    products,
                    barcodes[
                            rd.nextInt(testCount)
                    ]
            );

        }


        /*
        ==================================
        AVL SEARCH
        ==================================
         */
        long startAVL =
                System.nanoTime();

        for (int i = 0; i < testCount; i++) {

            tree.search(
                    barcodes[i]
            );

        }

        long endAVL =
                System.nanoTime();


        /*
        ==================================
        LINEAR SEARCH
        ==================================
         */
        long startLinear =
                System.nanoTime();

        for (int i = 0; i < testCount; i++) {

            linearSearch(
                    products,
                    barcodes[i]
            );

        }

        long endLinear =
                System.nanoTime();


        /*
        ==================================
        Calculate average search time
        ==================================
         */

        double avlAvgUs =
                ((endAVL - startAVL)
                        / (double)testCount)
                        /1000.0;

        double linearAvgUs =
                ((endLinear - startLinear)
                        / (double)testCount)
                        /1000.0;


        /*
        ==================================
        Display result
        ==================================
         */

        System.out.println(
                "Dataset size: "
                + products.size()
        );

        System.out.printf(
                "AVL Average Search: %.4f microseconds\n",
                avlAvgUs
        );

        System.out.printf(
                "Linear Average Search: %.4f microseconds\n",
                linearAvgUs
        );

    }

}