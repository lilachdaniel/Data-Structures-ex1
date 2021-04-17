import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomizedTester {
    public static void main(String[] args) {
        randomTests(1000, 30);
    }

    public static void randomTests(int n, int maxTreeSize) {
        int i;
        for (i = 0; i < n; i++) {
            AVLTree tree = new AVLTree();

            int treeSize = (int)(Math.random() * maxTreeSize);

            List<Integer> keysList = new ArrayList<>();

            for (int k = 0; k < treeSize; k++) {
                int key = (int)(Math.random() * treeSize);
                boolean value = (Math.random() >= 0.5);

                if (tree.insert(key, value) != -1) {
                    keysList.add(key);
                }
            }

            int[] keysArr = new int[keysList.size()];
            for (int k = 0; k < keysList.size(); k++) {
                keysArr[k] = keysList.get(k);
            }
            Arrays.sort(keysArr);

            boolean[] infoArr = new boolean[keysList.size()];
            for (int k = 0; k < keysList.size(); k++) {
                infoArr[k] = tree.search(keysArr[k]);
            }


            if (checkMin(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkMin!");
                break;
            }

            if (checkMax(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkMax!");
                break;
            }

            if (checkSize(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkSize!");
                break;
            }

            if (checkBalanced(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkBalanced!");
                break;
            }

            if (checkPredecessor(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkPredecessor!");
                break;
            }

            if (checkSuccessor(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkSuccessor!");
                break;
            }

            if (checkKeysToArray(tree, keysArr) == false) {
                Tester.display(tree);
                System.out.println(Arrays.toString(keysArr));
                System.out.println("Error in keysToArray!");
                break;
            }

            if (checkInfoToArray(tree, infoArr) == false) {
                Tester.display(tree);
                System.out.println(Arrays.toString(infoArr));
                System.out.println("Error in infoToArray!");
                break;
            }

            if (checkHeight(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkHeight!");
                break;
            }

            if (checkParents(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkParents!");
                break;
            }

            if (checkSuccPrefixXor(tree, infoArr) == false) {
                Tester.display(tree);
                System.out.println("Error in checkSuccPrefixXor!");
                break;
            }

            if (checkPrefixXor(tree) == false) {
                Tester.display(tree);
                System.out.println("Error in checkPrefixXor!");
                break;
            }

        }

        System.out.println("Tested " + (i+1) + " trees");
    }

    public static boolean checkMin(AVLTree tree){
        AVLTree.AVLNode root = tree.getRoot();
        if (tree.empty()){
            return !tree.min.isRealNode();
        }
        while (root.getLeft().isRealNode()){
            root = root.getLeft();
        }
        return root == tree.min;
    }

    public static boolean checkMax(AVLTree tree){
        AVLTree.AVLNode root = tree.getRoot();
        if (tree.empty()){
            return !tree.max.isRealNode();
        }
        while (root.getRight().isRealNode()){
            root = root.getRight();
        }
        return root == tree.max;
    }

    private static int subtreeSize(AVLTree.AVLNode root){
        if (!root.isRealNode()){
            return 0;
        }
        return 1 + subtreeSize(root.getLeft()) + subtreeSize(root.getRight());
    }

    public static boolean checkSize(AVLTree tree){
        return subtreeSize(tree.getRoot()) == tree.size();
    }

    public static boolean checkBalanced(AVLTree tree){
        return subtreeBalanced(tree.getRoot());
    }

    private static boolean subtreeBalanced(AVLTree.AVLNode root){
        if (!root.isRealNode()){
            return true;
        }
        return (Math.abs(root.getBF())<2) && (subtreeBalanced(root.getRight()) && (subtreeBalanced(root.getLeft())));
    }

    public static boolean checkPredecessor(AVLTree tree){
        if (tree.size() == 0) {
            return true;
        }

        AVLTree.AVLNode curr = tree.max;
        AVLTree.AVLNode prev = tree.max.getPredecessor();
        int cnt = 1;
        while (curr != tree.min){
            if (prev.getKey() >= curr.getKey()){
                return false;
            }
            cnt += 1;
            curr = prev;
            prev = prev.getPredecessor();
        }
        return (cnt == tree.size());
    }

    public static boolean checkSuccessor(AVLTree tree){
        if (tree.size() == 0) {
            return true;
        }

        AVLTree.AVLNode curr = tree.min;
        AVLTree.AVLNode next = tree.min.getSuccessor();
        int cnt = 1;
        while (curr != tree.max){
            if (next.getKey() <= curr.getKey()){
                return false;
            }
            cnt += 1;
            curr = next;
            next = next.getSuccessor();
        }
        return (cnt == tree.size());
    }

    public static boolean checkKeysToArray(AVLTree tree, int[] expected) {
        int[] toCheck = tree.keysToArray();

        if (toCheck.length != expected.length)
            return false;

        for (int i = 0; i < toCheck.length; i++) {
            if (toCheck[i] != expected[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkInfoToArray(AVLTree tree, boolean[] expected) {
        boolean[] toCheck = tree.infoToArray();

        if (toCheck.length != expected.length)
            return false;

        for (int i = 0; i < toCheck.length; i++) {
            if (toCheck[i] != expected[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkHeight(AVLTree tree){
        return subtreeHeight(tree.getRoot());
    }

    public static boolean subtreeHeight(AVLTree.AVLNode node) {
        if (!node.isRealNode()) {
            return true;
        }
        if (subtreeHeight(node.getLeft()) == false || subtreeHeight(node.getRight()) == false) {
            return false;
        }

        return (node.getHeight() == Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);
    }

    public static boolean checkParents(AVLTree tree) {
        return subtreeParents(tree.getRoot());
    }

    public static boolean subtreeParents(AVLTree.AVLNode node) {
        if (!node.isRealNode()) {
            return true;
        }

        if (node.getLeft().isRealNode() && node.getLeft().getParent() != node) {
            return false;
        }
        if (node.getRight().isRealNode() && node.getRight().getParent() != node) {
            return false;
        }

        return (subtreeParents(node.getLeft()) && subtreeParents(node.getRight()));
    }

//    public static boolean checkAllXor(AVLTree tree) {
//        return subtreeAllXor(tree.getRoot());
//    }
//
//    public static boolean subtreeAllXor(AVLTree.AVLNode node) {
//        if (!node.isRealNode()) {
//            return true;
//        }
//
//        if (recGetAllXor(node) != node.getAllXor()) {
//            return false;
//        }
//
//        return (subtreeAllXor(node.getLeft()) && subtreeAllXor(node.getRight()));
//    }
//
//    public static boolean recGetAllXor(AVLTree.AVLNode node) {
//        if (!node.isRealNode()) {
//            return false;
//        }
//        return node.getValue() ^ recGetAllXor(node.getLeft()) ^ recGetAllXor(node.getRight());
//    }

    public static boolean checkSuccPrefixXor(AVLTree tree, boolean[] values) {
        if (tree.empty()) {
            return true;
        }

        boolean expected = false;
        int i = 0;
        AVLTree.AVLNode curr = tree.min;

        while (curr != null) {
            boolean currResult = tree.succPrefixXor(curr.getKey());
            expected = expected ^ values[i++];
            if (currResult != expected) {
                return false;
            }

            curr = curr.getSuccessor();
        }

        return true;
    }
    public static boolean checkPrefixXor(AVLTree tree) {
        if (tree.empty()) {
            return true;
        }

        AVLTree.AVLNode curr = tree.min;
        while (curr != null) {
            if (tree.prefixXor(curr.getKey()) != tree.succPrefixXor(curr.getKey())) {
                return false;
            }

            curr = curr.getSuccessor();
        }

        return true;
    }

}

