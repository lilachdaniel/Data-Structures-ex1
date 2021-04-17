import java.util.Arrays;

public class Tester {
    public static void main(String[] args){
        AVLTree tree = new AVLTree();
        // display(tree);
//        tree.insert(6,true);
//        tree.insert(7,true);
//        tree.insert(8,true);
//        tree.insert(9,true);
//        tree.insert(10,true);
//        tree.insert(11,true);
//        display(tree);

//        for (int i = 1 ; i < 20 ; i++){
//            tree.insert(i, true);
//            display(tree);
//            System.out.println("size:" + tree.size() + "\tmin:" + tree.min.getKey() +
//                               "\tmax:" + tree.max.getKey());
//        }

//        for (int i = 20 ; i>1 ; i--){
//            tree.insert(i, true);
//            display(tree);
//            System.out.println("size:" + tree.size() + "\tmin:" + tree.min.getKey() +
//                    "\tmax:" + tree.max.getKey());
//        }
//        int sizeBefore = tree.size();
//        tree.insert(20, false);
//        if (tree.size() != sizeBefore)
//            System.out.println("Error in insert - inserted a duplicate");

        //left then right rotation
        AVLTree tree4 = new AVLTree();
        tree4.insert(9,true);
        tree4.insert(7,true);
        tree4.insert(8,true);
        display(tree4);

        // Tests for keysToArray (mainly tests maintenance of successor/predecessor)
        AVLTree tree2 = new AVLTree();
        for (int i = 0; i < 10; i++) {
            tree2.insert(i, true);
            int[] keysArr = tree2.keysToArray();
            System.out.println(Arrays. toString(keysArr));
            tree2.insert(19-i, true);
            keysArr = tree2.keysToArray();
            System.out.println(Arrays. toString(keysArr));
        }
        display(tree2);
        int[] keysArr = tree2.keysToArray();
        for (int i = 0; i < keysArr.length; i++) {
            System.out.print(keysArr[i]+ " ");
            if (keysArr[i] != i)
                System.out.println("ERROR - avltree.keysToArray (or successor/predecessor maintenance)");
        }

        // Tests for infoToArray (mainly tests maintenance of successor/predecessor)
        AVLTree tree3 = new AVLTree();
        for (int i = 0; i < 20; i++) {
            tree3.insert(i, (i % 2 == 0));
        }
        boolean[] infoArr = tree3.infoToArray();
        for (int i = 0; i < infoArr.length; i++) {
            if (infoArr[i] != (i % 2 == 0))
                System.out.println("ERROR - avltree.infoToArray (or successor/predecessor maintenance)");
        }


        // Tests for search
//        System.out.println("Testing search.....");
//        AVLTree tree1 = new AVLTree();
//        if (tree1.search(5) != null)
//            System.out.println("ERROR - avltree.search (when tree is empty)");
//        for (int i = 0; i < 10; i++) {
//            tree1.insert(i, true);
//            tree1.insert(20-i, true);
//        }
//        if (tree1.search(5) == null)
//            System.out.println("ERROR - avltree.search (regular tree)");

    }
    public static void display(AVLTree tree) {
        final int height = 5, width = 64;

        int len = width * height * 2 + 2;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 1; i <= len; i++)
            sb.append(i < len - 2 && i % width == 0 ? "\n" : ' ');

        displayR(sb, width / 2, 1, width / 4, width, tree.getRoot(), " ");
        System.out.println(sb);
    }

    private static void displayR(StringBuilder sb, int c, int r, int d, int w, AVLTree.AVLNode n,
                          String edge) {
        if (n.isRealNode()) {
            displayR(sb, c - d, r + 2, d / 2, w, n.getLeft(), " /");

            String s = String.valueOf(n.getKey()) + "," + String.valueOf(n.getHeight());
            int idx1 = r * w + c - (s.length() + 1) / 2;
            int idx2 = idx1 + s.length();
            int idx3 = idx1 - w;
            if (idx2 < sb.length())
                sb.replace(idx1, idx2, s).replace(idx3, idx3 + 2, edge);

            displayR(sb, c + d, r + 2, d / 2, w, n.getRight(), "\\ ");
        }
    }
}
