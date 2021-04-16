/**
 * public class AVLNode
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 * <p>
 * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
 * arguments. Changing these would break the automatic tester, and would result in worse grade.
 * <p>
 * However, you are allowed (and required) to implement the given functions, and can add functions of your own
 * according to your needs.
 */

public class AVLTree {
    private AVLNode root;
    public AVLNode min; //change to private!!!
    public AVLNode max; //change to private!!!
    private int size;

    /**
     * This constructor creates an empty AVLTree.
     */
    public AVLTree(){
        this.root = new AVLNode();
        this.min = root;
        this.max = root;
        this.size = 0;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return this.size == 0; // to be replaced by student code
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public Boolean search(int k) {
        AVLNode curr = root;
        while (curr.isRealNode()){
            if (curr.getKey()==k) { //key found
                return curr.getValue();
            }
            else if (k>curr.getKey()) { //go right
                curr = curr.getRight();
            }
            else { //go left
                curr = curr.getLeft();
            }
        }

        return null;
    }

    /**
     * public int insert(int k, boolean i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
	 * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
	 * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, boolean i) {
        //if the tree is empty
        if (this.empty()){
            this.root = new AVLNode(k, i, 0, null, null, i, new AVLNode(), new AVLNode(), null);
            this.min = root;
            this.max = root;
            this.size = 1;
            return 0;
        }

        //find location to insert (or check if key already exists)
        AVLNode curr = root;
        AVLNode prev = null;
        while (curr.isRealNode()){
            prev = curr;
            if (curr.getKey()==k){//key already exists
                return -1;
            }
            else if (k>curr.getKey()){//go right
                curr = curr.getRight();
            }
            else{//go left
                curr = curr.getLeft();
            }
        }

        //insert new node
        AVLNode newNode= new AVLNode(k,i,0,null,null,i,new AVLNode(), new AVLNode(),prev);
        if (prev.getLeft() == curr){
            prev.setLeft(newNode);

            //update successor/predecessor
            newNode.setPredecessor(prev.getPredecessor());
            newNode.setSuccessor(prev);
            prev.setPredecessor(newNode);
        }
        else{
            prev.setRight(newNode);

            //update successor/predecessor
            newNode.setPredecessor(prev);
            newNode.setSuccessor(prev.getSuccessor());
            prev.setSuccessor(newNode);
        }

        //maintaining size,min and max
        this.size += 1;
        if (newNode.getKey()<this.min.getKey()){
            this.min = newNode;
        }
        if (newNode.getKey()>this.max.getKey()){
            this.max = newNode;
        }

        //fixing after insertion
        while (prev!=null){
            //if |BF|<2, check height and decide to go up or terminate
            if (Math.abs(prev.getBF())<2){
                int expectedHeight = Math.max(prev.getLeft().getHeight(),prev.getRight().getHeight())+1;
                if (prev.getHeight() == expectedHeight){
                    break;
                }
                else{//go up
                    prev.setHeight(expectedHeight);
                    prev = prev.getParent();
                    continue;
                }
            }
            else{ //|BF|=2 -> found a criminal
                if (prev.getBF()<0){
                    if (prev.getRight().getBF()<0){
                        leftRotation(prev);
                    }
                    else{
                        rightLeftRotation(prev);
                    }
                }
                else{
                    if (prev.getLeft().getBF()>0){
                        rightRotation(prev);
                    }
                    else{
                        leftRightRotation(prev);
                    }
                }
                return 1;//change!!!
            }
        }
        return 42;    // change!!!
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        return 42;    // to be replaced by student code
    }

    private void leftRotation(AVLNode crim){
        AVLNode crimP = crim.getParent();
        AVLNode crimR = crim.getRight();

        //change crim's subtree's root
        crimR.setParent(crimP);
        if (crimP == null){//criminal=root
            this.root = crimR;
        }
        else {
            if (crimP.getLeft() == crim) {
                crimP.setLeft(crimR);
            } else {
                crimP.setRight(crimR);
            }
        }

        //connecting the criminal to its new children
        crim.setParent(crimR);
        crim.setRight(crimR.getLeft());
        crimR.getLeft().setParent(crim);
        crimR.setLeft(crim);

        crim.setHeight(Math.max(crim.getLeft().getHeight(),crim.getRight().getHeight())+1);
    }
    private void leftRightRotation(AVLNode crim){
        return;
    }
    private void rightRotation(AVLNode crim){
        AVLNode crimP = crim.getParent();
        AVLNode crimL = crim.getLeft();

        //change crim's subtree's root
        crimL.setParent(crimP);
        if (crimP == null){//criminal=root
            this.root = crimL;
        }
        else {
            if (crimP.getLeft() == crim) {
                crimP.setLeft(crimL);
            }
            else {
                crimP.setRight(crimL);
            }
        }

        //connecting the criminal to its new children
        crim.setParent(crimL);
        crim.setLeft(crimL.getRight());
        crimL.getRight().setParent(crim);
        crimL.setRight(crim);

        crim.setHeight(Math.max(crim.getLeft().getHeight(),crim.getRight().getHeight())+1);
    }
    private void rightLeftRotation(AVLNode crim){
        return;
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public Boolean min() {
        if (this.empty()) {
            return null;
        }

        return this.min.getValue();
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public Boolean max() {
        if (this.empty()) {
            return null;
        }

        return this.min.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size()];

        AVLNode currNode = this.min;
        for (int i = 0; currNode != null; i++) {
            arr[i] = currNode.getKey();
            currNode = currNode.getSuccessor();
        }

        return arr;
    }

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.size()];

        AVLNode currNode = this.min;
        for (int i = 0; currNode != null; i++) {
            arr[i] = currNode.getValue();
            currNode = currNode.getSuccessor();
        }

        return arr;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.size; // to be replaced by student code
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public AVLNode getRoot() {
        return this.root;
    }

    /**
     * public boolean prefixXor(int k)
     *
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     *
     * precondition: this.search(k) != null
     *
     */
    public boolean prefixXor(int k){
        return false;
    }

    /**
     * public AVLNode successor
     *
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     */
    public AVLNode successor(AVLNode node){
        return node.getSuccessor();
    }

    /**
     * public boolean succPrefixXor(int k)
     *
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     *
     * precondition: this.search(k) != null
     */
    public boolean succPrefixXor(int k){
        return false;
    }


    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     * <p>
     * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
     * arguments. Changing these would break the automatic tester, and would result in worse grade.
     * <p>
     * However, you are allowed (and required) to implement the given functions, and can add functions of your own
     * according to your needs.
     */
    public class AVLNode {
        private int key;
        private boolean info;
        private int height;
        private AVLNode successor;
        private AVLNode predecessor;
        private boolean prefixXor;
        private AVLNode left;
        private AVLNode right;
        private AVLNode parent;

        private AVLNode(int key, boolean info, int height, AVLNode successor, AVLNode predecessor, boolean prefixXor, AVLNode left, AVLNode right, AVLNode parent){
            this.key = key;
            this.info = info;
            this.height = height;
            this.successor = successor;
            this.predecessor = predecessor;
            this.prefixXor = prefixXor;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        private AVLNode(){
            this.key = -1;
            this.height = -1;
        }

        //returns node's key (for virtual node return -1)
        public int getKey() {
            return this.key;
        }

        //returns node's value [info] (for virtual node return null)
        public Boolean getValue() {
            if (!this.isRealNode()){
                return null;
            }
            return info;
        }

        //sets left child
        public void setLeft(AVLNode node) {
            this.left = node;
        }

        //returns left child (if there is no left child return null)
        public AVLNode getLeft() {
//            if (!this.left.isRealNode()){
//                return null;
//            }
            return this.left;
        }

        //sets right child
        public void setRight(AVLNode node) {
            this.right = node;
        }

        //returns right child (if there is no right child return null)
        public AVLNode getRight() {
//            if (!this.right.isRealNode()){
//                return null;
//            }
            return this.right;
        }

        //sets parent
        public void setParent(AVLNode node) {
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        public AVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            if (this.key == -1){
                return false;
            }
            return true;
        }

        // sets the height of the node
        public void setHeight(int height) {
            this.height = height;
        }

        // Returns the height of the node (-1 for virtual nodes)
        public int getHeight() {
            return this.height;
        }

        private int getBF(){
            return this.left.getHeight() - this.right.getHeight();
        }

        private AVLNode getSuccessor(){
            return this.successor;
        }

        private void setSuccessor(AVLNode successor){
            this.successor = successor;
        }

        private AVLNode getPredecessor(){
            return this.predecessor;
        }

        private void setPredecessor(AVLNode predecessor){
            this.predecessor = predecessor;
        }

    }


}


