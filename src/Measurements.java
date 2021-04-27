import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Measurements {
    public static void main(String[] args) {
        Q0();
        Q1();
    }



    public static void Q0() {
        for (int i = 1; i <= 5; i++) {
            // create tree
            AVLTree tree = new AVLTree();
            int treeSize = 500 * i;
            List<Integer> keysList = new ArrayList<>();

            // insert i * 500 different keys, save the
            while (tree.size() < treeSize) {
                int key = (int)(Math.random() * treeSize);

                if (tree.insert(key, true) != -1) {
                    keysList.add(key);
                }
            }

            // sort keys
            Collections.sort(keysList);

            for (int k = 0; k < keysList.size(); k++) {
                tree.prefixXor(keysList.get(k));
            }
            // call succPrefixXor for all keys
            for (int k = 0; k < keysList.size(); k++) {
                tree.succPrefixXor(keysList.get(k));
            }
        }
    }

    public static void Q1() {
        for (int i = 1; i <= 5; i++) {
            // create tree
            AVLTree tree = new AVLTree();
            int treeSize = 500 * i;
            List<Integer> keysList = new ArrayList<>();

            // insert i * 500 different keys, save the
            while (tree.size() < treeSize) {
                int key = (int)(Math.random() * treeSize);

                if (tree.insert(key, true) != -1) {
                    keysList.add(key);
                }
            }

            // sort keys
            Collections.sort(keysList);

            // measure fast prefixXor
            long start = System.nanoTime();
            long end100 = 0, endAll = 0;

            for (int k = 0; k < keysList.size(); k++) {
                tree.prefixXor(keysList.get(k));

                if (k == 99) {
                    end100 = System.nanoTime();
                }
            }
            endAll = System.nanoTime();

            // measure slow prefixXor
            long startSucc = System.nanoTime();
            long end100Succ = 0, endAllSucc = 0;

            // call succPrefixXor for all keys
            for (int k = 0; k < keysList.size(); k++) {
                tree.succPrefixXor(keysList.get(k));

                if (k == 99) {
                    end100Succ = System.nanoTime();
                }
            }
            endAllSucc = System.nanoTime();

            // calculate averages
            float fastAvg    = (endAll - start) / treeSize;
            float fastAvg100 =  (end100 - start) / treeSize;
            float slowAvg    = (endAllSucc - startSucc) / treeSize;
            float slowAvg100 =  (end100Succ - startSucc) / treeSize;

            System.out.println("iteration " + i + "\t fast: " + fastAvg + "\t slow: " + slowAvg +
                               "\t fast-100: " + fastAvg100 + "\t slow-100: " + slowAvg100);
        }
    }
}
