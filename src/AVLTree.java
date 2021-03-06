/**
 * names: Lilach Daniel, Ofir Gaash
 * usernames: lilachdaniel, ofirgaash
 * ids: 206377368, 315395210
 */

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
    private AVLNode virtualNode;
    private AVLNode root;
    private AVLNode min;
    private AVLNode max;
    private int size;

    /**
     * This constructor creates an empty AVLTree.
     * Time Complexity - O(1)
     */
    public AVLTree(){
        this.virtualNode = new AVLNode(-1, false, -1, null, null, false, null, null, null);
        this.root = virtualNode;
        this.min = root;
        this.max = root;
        this.size = 0;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     * Time Complexity - O(1)
     */
    public boolean empty() {
        return this.size == 0;
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     * Time Complexity - O(logn)
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
     * Time Complexity - O(logn)
     */
    public int insert(int k, boolean i) {
        //if the tree is empty
        if (this.empty()){ // insert a new node as root
            this.root = new AVLNode(k, i, 0, null, null, i, this.virtualNode, this.virtualNode, null);
            this.min = root;
            this.max = root;
            this.size = 1;
            return 0;
        }

        //find location to insert (or check if key already exists), and save its parent in 'prev'
        AVLNode prev = findLocation(k);
        if (prev.getKey() == k){ //key already exists
            return -1;
        }

        //insert new node
        AVLNode newNode= new AVLNode(k,i,0,null,null,i,this.virtualNode, this.virtualNode,prev);
        if (prev.getKey() > k){
            prev.setLeft(newNode);

            updateSuccPred(prev.getPredecessor(), newNode, prev);
        }
        else {
            prev.setRight(newNode);

            updateSuccPred(prev, newNode, prev.getSuccessor());
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
        return rebalanceTree(prev, false);
    }

    /**
     * returns the location where a node with key 'k' should be inserted,
     * by returning the node it should be inserted under.
     * if key already exists, returns the node where it exists
     * Time Complexity - O(logn)
     */
    private AVLNode findLocation(int k){
        AVLNode curr = this.root;
        AVLNode prev = null;
        while (curr.isRealNode()){
            prev = curr;
            if (curr.getKey()==k){//key already exists
                return curr;
            }
            else if (k>curr.getKey()){//go right
                curr = curr.getRight();
            }
            else{//go left
                curr = curr.getLeft();
            }
        }
        return prev;
    }

    /**
     * receives an inserted node and its predecessor & successor,
     * and updates their predecessor/successor fields accordingly
     * Time Complexity - O(1)
     */
    private void updateSuccPred(AVLNode newNodePred, AVLNode newNode, AVLNode newNodeSucc) {
        // update the successor/predecessor fields of the new node
        newNode.setSuccessor(newNodeSucc);
        newNode.setPredecessor(newNodePred);
        // update the successor/predecessor fields of the predecessor/successor
        if (newNodePred!=null){
            newNodePred.setSuccessor(newNode);
        }
        if (newNodeSucc!=null){
            newNodeSucc.setPredecessor(newNode);
        }
    }

    /**
     * Rebalances the tree after an insertion/deletion according to
     * the algorithm taught in the course.
     * Maintains height and allXor fields,  and balance factor using rotations.
     * Returns the number of balancing operations.
     * Time Complexity - O(logn)
     */
    private int rebalanceTree(AVLNode parNewNode, boolean isDelete){
        int actionCount = 0;

        while (parNewNode!=null){
            //if |BF|<2, maintain height and allxor
            if (Math.abs(parNewNode.getBF())<2){
                int correctHeight = Math.max(parNewNode.getLeft().getHeight(),parNewNode.getRight().getHeight())+1;
                if (parNewNode.getHeight() != correctHeight) {
                    parNewNode.setHeight(correctHeight);
                    actionCount++;
                }
                parNewNode.setAllXor(parNewNode.getLeft().getAllXor() ^ parNewNode.getRight().getAllXor() ^ parNewNode.getValue());
            }
            else{ //|BF|=2 -> found a criminal, perform rotation
                actionCount++;
                if (parNewNode.getBF()<0){
                    if (parNewNode.getRight().getBF()<0 || (isDelete && parNewNode.getRight().getBF()==0)){
                        leftRotation(parNewNode);
                    }
                    else{
                        rightLeftRotation(parNewNode);
                    }
                }
                else{
                    if (parNewNode.getLeft().getBF()>0 || (isDelete && parNewNode.getLeft().getBF()==0)){
                        rightRotation(parNewNode);
                    }
                    else{
                        leftRightRotation(parNewNode);
                    }
                }
            }

            parNewNode = parNewNode.getParent();
        }
        return actionCount;
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     * Time Complexity - O(logn)
     */
    public int delete(int k) {
        // search for the node to remove
        AVLNode toDelete = this.findLocation(k);
        if (toDelete == null || toDelete.getKey() != k) {
            return -1;
        }

        // remove the node, and save its parent for rebalancing
        AVLNode firstToRebalance = actualDelete(toDelete);


        // maintaining AVLTree's size, min, max fields
        this.size--;
        if (this.empty()) {
            this.min = root;
            this.max = root;
        }
        else {
            if (this.min == toDelete) {
                this.min = toDelete.getSuccessor();
            }
            if (this.max == toDelete) {
                this.max = toDelete.getPredecessor();
            }
        }

        // maintain successor/predecessor in toDelete's own successor and predecessor
        if (toDelete.getSuccessor() != null) {
            toDelete.getSuccessor().setPredecessor(toDelete.getPredecessor());
        }
        if (toDelete.getPredecessor() != null) {
            toDelete.getPredecessor().setSuccessor(toDelete.getSuccessor());
        }

        // rebalance tree
        return rebalanceTree(firstToRebalance, true);
    }

    /**
     * receives the node to disconnect from the tree,
     * returns the first node in the rebalancing chain
     * Time Complexity - O(1)
     */
    private AVLNode actualDelete(AVLNode toDelete) {
        AVLNode toDeleteParent = toDelete.getParent();
        AVLNode firstToRebalance;

        // if has one child at most
        if (!toDelete.getLeft().isRealNode() || !toDelete.getRight().isRealNode()) {
            AVLNode newChild;

            // determine the replacement of toDelete, according to specific case
            if (toDelete.getLeft().isRealNode()) { // bypass
                newChild = toDelete.getLeft();
            }
            else if (toDelete.getRight().isRealNode()) { // bypass
                newChild = toDelete.getRight();
            }
            else { // is leaf
                newChild = this.virtualNode;
            }

            // link newChild to toDelete's parent , or update root if toDelete is root
            if (this.getRoot() == toDelete) {
                this.root = newChild;
                this.root.parent = null;
            }
            else {
                if (newChild.isRealNode()) {
                    newChild.setParent(toDeleteParent);
                }

                if (toDeleteParent.getRight() == toDelete) {
                    toDeleteParent.setRight(newChild);
                } else {
                    toDeleteParent.setLeft(newChild);
                }
            }

            // determine what node will be the first to rebalance
            firstToRebalance = toDeleteParent;
        }
        else { // has two real children
            // bypass toDelete's successor
            AVLNode toDeleteSucc = toDelete.getSuccessor();
            AVLNode succParent = toDeleteSucc.getParent();

            if (succParent.getRight() == toDeleteSucc) {
                succParent.setRight(toDeleteSucc.getRight());
            }
            else {
                succParent.setLeft(toDeleteSucc.getRight());
            }

            toDeleteSucc.getRight().setParent(succParent);



            // replace toDelete with toDeleteSucc
            // link toDelete's right child with toDeleteSucc
            toDeleteSucc.setRight(toDelete.getRight());
            toDelete.getRight().setParent(toDeleteSucc);

            // link toDelete's left child with toDeleteSucc
            toDeleteSucc.setLeft(toDelete.getLeft());
            toDelete.getLeft().setParent(toDeleteSucc);

            // link toDelete's parent with toDeleteSucc
            toDeleteSucc.setParent(toDelete.getParent());
            if (toDeleteParent != null) {
                if (toDeleteParent.getLeft() == toDelete) {
                    toDeleteParent.setLeft(toDeleteSucc);
                } else {
                    toDeleteParent.setRight(toDeleteSucc);
                }
            }

            // maintain toDeleteSucc's updated height
            int newHeight = Math.max(toDeleteSucc.getLeft().getHeight(),
                                     toDeleteSucc.getRight().getHeight()) + 1;
            toDeleteSucc.setHeight(newHeight);

            // if deleted the root, update root field
            if (this.getRoot() == toDelete) {
                this.root = toDeleteSucc;
                this.root.parent = null;
            }

            // determine where to start rebalance
            if (succParent == toDelete) {
                firstToRebalance = toDeleteSucc;
            }
            else {
                firstToRebalance = succParent;
            }
        }

        return firstToRebalance;
    }

    /**
     * Performs a left rotation on the given node
     * Time Complexity - O(1)
     */
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
        if (crimR.getLeft().isRealNode()) {
            crimR.getLeft().setParent(crim);
        }
        crimR.setLeft(crim);

        // maintaining the height of the criminal
        crim.setHeight(Math.max(crim.getLeft().getHeight(),crim.getRight().getHeight())+1);

        // maintain height of crim's parent, which is now crimR
        crimR.setHeight(Math.max(crimR.getLeft().getHeight(),
                                             crimR.getRight().getHeight()) + 1);

        // maintaining the allXor of the Crim and crimR
        crim.setAllXor(crim.getLeft().getAllXor()  ^ crim.getRight().getAllXor()  ^ crim.getValue());
        crimR.setAllXor(crimR.getLeft().getAllXor() ^ crimR.getRight().getAllXor() ^ crimR.getValue());
    }

    /**
     * Performs a left-then-right rotation on the given node
     * Time Complexity - O(1)
     */
    private void leftRightRotation(AVLNode crim){
        leftRotation(crim.getLeft());
        rightRotation(crim);
    }

    /**
     * Performs a right rotation on the given node
     * Time Complexity - O(1)
     */
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
        if (crimL.getRight().isRealNode()) {
            crimL.getRight().setParent(crim);
        }
        crimL.setRight(crim);

        // maintaining height of criminal
        crim.setHeight(Math.max(crim.getLeft().getHeight(),crim.getRight().getHeight())+1);

        // maintain the height of crim's parent, which is now crimL
        crimL.setHeight(Math.max(crimL.getLeft().getHeight(), crimL.getRight().getHeight()) + 1);

        // maintaining the allXor of crim and crimL
        crim.setAllXor( crim.getLeft().getAllXor()  ^ crim.getRight().getAllXor()  ^ crim.getValue());
        crimL.setAllXor(crimL.getLeft().getAllXor() ^ crimL.getRight().getAllXor() ^ crimL.getValue());
    }

    /**
     * Perform right-then-left rotation on the given node
     * Time Complexity - O(1)
     */
    private void rightLeftRotation(AVLNode crim){
        rightRotation(crim.getRight());
        leftRotation(crim);
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     * Time Complexity - O(1)
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
     * Time Complexity - O(1)
     */
    public Boolean max() {
        if (this.empty()) {
            return null;
        }

        return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     * Time Complexity - O(n)
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size()];
        if (this.size() == 0) {
            return arr;
        }

        // iterating on all nodes by starting from the minimum
        // and performing getSuccessor after each iteration
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
     * Time Complexity - O(n)
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.size()];
        if (this.size() == 0) {
            return arr;
        }

        // iterating on all nodes by starting from the minimum
        // and performing getSuccessor after each iteration
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
     * Time Complexity - O(1)
     */
    public int size() {
        return this.size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * Time Complexity - O(1)
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
     * Time Complexity - O(logn)
     */
    public boolean prefixXor(int k){
        boolean result = false;
        AVLNode curr = this.getRoot();

        // On each node on the path from the root to the given node,
        // take into account values of the nodes with keys lower than k
        while (curr.isRealNode()){
            if (curr.getKey()==k) { //key found
                return result ^ curr.getLeft().getAllXor() ^ curr.getValue();
            }
            else if (k>curr.getKey()) { //go right
                result = result ^ curr.getLeft().getAllXor() ^ curr.getValue();
                curr = curr.getRight();
            }
            else { //go left
                curr = curr.getLeft();
            }
        }

        // should not get here
        return false;
    }

    /**
     * public AVLNode successor
     *
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     * Time Complexity - O(1)
     */
    public AVLNode successor(AVLNode node){
        return node.getSuccessor();
    }

    /**
     * public boolean succPrefixXor(int k)
     *
     * This function is identical to allXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     *
     * precondition: this.search(k) != null
     * Time Complexity - O(n)
     */
    public boolean succPrefixXor(int k){
        if (this.empty()) {
            return false;
        }

        boolean result = false;

        AVLNode curr = this.min;
        while (curr.getKey() != k) {
            result = result ^ curr.getValue();
            curr = this.successor(curr);
        }

        return result ^ curr.getValue();
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
        private boolean allXor;
        private AVLNode left;
        private AVLNode right;
        private AVLNode parent;

        // Time Complexity - O(1)
        private AVLNode(int key, boolean info, int height, AVLNode successor, AVLNode predecessor, boolean allXor, AVLNode left, AVLNode right, AVLNode parent){
            this.key = key;
            this.info = info;
            this.height = height;
            this.successor = successor;
            this.predecessor = predecessor;
            this.allXor = allXor;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        //returns node's key (for virtual node return -1)
        // Time Complexity - O(1)
        public int getKey() {
            return this.key;
        }

        //returns node's value [info] (for virtual node return null)
        // Time Complexity - O(1)
        public Boolean getValue() {
            if (!this.isRealNode()){
                return null;
            }
            return info;
        }

        //sets left child
        // Time Complexity - O(1)
        public void setLeft(AVLNode node) {
            this.left = node;
        }

        //returns left child
        //if called for virtual node, return value is ignored.
        // Time Complexity - O(1)
        public AVLNode getLeft() {
            return this.left;
        }

        //sets right child
        // Time Complexity - O(1)
        public void setRight(AVLNode node) {
            this.right = node;
        }

        //returns right child
        //if called for virtual node, return value is ignored.
        // Time Complexity - O(1)
        public AVLNode getRight() {
            return this.right;
        }

        //sets parent
        // Time Complexity - O(1)
        public void setParent(AVLNode node) {
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        // Time Complexity - O(1)
        public AVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        // Time Complexity - O(1)
        public boolean isRealNode() {
            if (this.key == -1){
                return false;
            }
            return true;
        }

        // sets the height of the node
        // Time Complexity - O(1)
        public void setHeight(int height) {
            this.height = height;
        }

        // Returns the height of the node (-1 for virtual nodes)
        // Time Complexity - O(1)
        public int getHeight() {
            return this.height;
        }
	
	    //Returns the balance factor of the node
        // Time Complexity - O(1)
        private int getBF(){
            return this.left.getHeight() - this.right.getHeight();
        }
	
	    //Returns the node's successor
        // Time Complexity - O(1)
        private AVLNode getSuccessor(){ // change to private!!!!!!!
            return this.successor;
        }

	    //Sets node's successor
        // Time Complexity - O(1)
        private void setSuccessor(AVLNode successor){
            this.successor = successor;
        }

	    //Returns node's predecessor
        // Time Complexity - O(1)
        private AVLNode getPredecessor(){ // change to private!!!!!!!!
            return this.predecessor;
        }

	    //Sets node's predecessor
        // Time Complexity - O(1)
        private void setPredecessor(AVLNode predecessor){
            this.predecessor = predecessor;
        }

	    //Returns xor value of the node's subtree
        // Time Complexity - O(1)
        private boolean getAllXor() { // change to private!!!!!!!
            return this.allXor;
        }
	
	    //Sets xor value of the node's subtree
        // Time Complexity - O(1)
        private void setAllXor(boolean allXor) {
            this.allXor = allXor;
        }

    }


}


