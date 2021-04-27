import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Measurements {
    public static void main(String[] args) {
        Q0();
        System.out.println("\n\n---------------- Q1 ------------------");
        Q1();
        System.out.println("\n\n---------------- Q2 ------------------");
        Q2();
    }



    public static void Q0() {
        for (int i = 1; i <= 10; i++) {
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
                int key = (int)(Math.random() * 1000000);

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

    public static void Q2() {
        for (int i = 1; i <= 5; i++) {
            AVLTree avl = new AVLTree();
            BST bst = new BST();
            insertA(avl, bst, i);

            avl = new AVLTree();
            bst = new BST();
            insertB(avl, bst, i);

            avl = new AVLTree();
            bst = new BST();
            insertC(avl, bst, i);
        }
    }

    public static void insertA(AVLTree avl, BST bst, int i) {
        // measure insertions to AVL
        long startAVL = System.nanoTime();
        for (int k = 1; k <= 1000 * i; k++) {
            avl.insert(k, true);
        }
        long endAVL = System.nanoTime();
        float avgAVL = (endAVL - startAVL) / avl.size();

        long startBST = System.nanoTime();
        for (int k = 1; k < 1000 * i; k++) {
            bst.insert(k, true);
        }
        long endBST = System.nanoTime();
        float avgBST = (endBST - startBST) / bst.size();

        System.out.print("iteration " + i + "\tavl-A: " + avgAVL + "\tbst-A: " + avgBST + "\t");
    }

    public static void insertB(AVLTree avl, BST bst, int i) {
        long startAVL = System.nanoTime();
        insertBRecAVL(avl, 1, 1000 * i);
        long endAVL = System.nanoTime();
        float avgAVL = (endAVL - startAVL) / avl.size();

        long startBST = System.nanoTime();
        insertBRecBST(bst, 1, 1000 * i);
        long endBST = System.nanoTime();
        float avgBST = (endBST - startBST) / bst.size();

        System.out.print("iteration " + i + "\tavl-B: " + avgAVL + "\tbst-B: " + avgBST + "\t");
    }

    public static void insertBRecAVL(AVLTree avl, int start, int end) {
        if (end >= start) {
            int median = (int) ((start + end) / 2);
            avl.insert(median, true);

            insertBRecAVL(avl, median + 1, end);
            insertBRecAVL(avl, start, median - 1);
        }
    }

    public static void insertBRecBST(BST bst, int start, int end) {
        if (end >= start) {
            int median = (int) ((start + end) / 2);
            bst.insert(median, true);

            insertBRecBST(bst, median + 1, end);
            insertBRecBST(bst, start, median - 1);
        }
    }

    public static void insertC(AVLTree avl, BST bst, int i) {
        long startAVL = System.nanoTime();
        while (avl.size() < 1000 * i) {
            int key = (int)(Math.random() * 1000000);
            avl.insert(key, true);
        }
        long endAVL = System.nanoTime();
        float avgAVL = (endAVL - startAVL) / avl.size();

        long startBST = System.nanoTime();
        while (bst.size() < 1000 * i) {
            int key = (int)(Math.random() * 1000000);
            bst.insert(key, true);
        }
        long endBST = System.nanoTime();
        float avgBST = (endBST - startBST) / bst.size();

        System.out.println("iteration " + i + "\tavl-C: " + avgAVL + "\tbst-C: " + avgBST);
    }
}











