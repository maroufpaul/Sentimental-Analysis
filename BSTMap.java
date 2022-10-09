import java.util.ArrayList;
import java.util.List;

/**
 * A map implemented with a binary search tree.
 */
public class BSTMap<K extends Comparable<K>, V> {

    private Node<K, V> root;    // points to the root of the BST.

    /**
     * Create a new, empty BST.
     */
    public BSTMap() {
        root = null;
    }

    /**
     * Put (add a key-value pair) into this BST.  If the key already exists, the old
     * value will be overwritten with the new one.
     */
    public void put(K newKey, V newValue)
    {
        if(root == null ) // if tree is empty
        {
         Node<K,V> newNode = new Node<>();
         newNode.key = newKey;
         newNode.value = newValue;
         root = newNode;
        }
        else {put(root,newKey, newValue);}
    }
    /**
     * Helper function for put.
     */
    private void put(Node<K, V> curr, K newKey, V newValue)
    {
        if ((curr.key).compareTo(newKey)==0){ // if newKey already exists, update the value of key to newValue
            curr.value = newValue;
        }
        else if(newKey.compareTo(curr.key) < 0){ // key should be inserted in the left branch
            if(curr.left == null){ // is the left branch empty?
                Node<K,V> newNode = new Node<>();
                newNode.key = newKey;
                newNode.value = newValue;
                curr.left = newNode;
            }
            else{put(curr.left,newKey,newValue);}
        }
        else {  // key should be inserted in the right branch
            if(curr.right == null ){ // is the right branch empty?
                Node<K,V> newNode = new Node<> ();
                newNode.key = newKey ;
                newNode.value = newValue;
                curr.right = newNode;}
             else {put(curr.right,newKey,newValue);}
        }
    }

    /**
     * Get a value from this BST, based on its key.  If the key doesn't already exist in the BST,
     * this method returns null.
     */
    public V get(K searchKey)
    {
        return get(root,searchKey);
    }

    /** helper function for get.
     */
    private V get (Node<K,V> curr, K searchKey){
        if(curr == null){ // reached a leaf node, not found −> key not in tree, return null
            return null;
        }
        else if (searchKey.compareTo(curr.key)==0){ // key found, return value
            return curr.value;
        }
        else if (searchKey.compareTo(curr.key)< 0){ // searchKey too small −> go left

            return get(curr.left,searchKey);

        }
        else {       // searchKey too big −> go right
            return get(curr.right,searchKey);
        }

    }

    /**
     * Test if a key is present in this BST.  Returns true if the key is found, false if not.
     */
    public boolean containsKey(K searchKey)
    {
       return containsKey(root ,searchKey);
    }
    private boolean containsKey(Node<K,V> curr, K searchKey){
        if (curr == null){ // reached a leaf node, not found −> key not in tree, return false
            return false;
        }
        else if (searchKey.compareTo(curr.key) == 0){  // key found, return true
            return true;
        }
        else if (searchKey.compareTo(curr.key) < 0){ // searchKey too small −> go left
            return containsKey(curr.left ,searchKey);
        }
        else {   // searchKey too big −> go right
            return containsKey(curr.right ,searchKey);
        }

    }

    /**
     * Given a key, remove the corresponding key-value pair from this BST.  Returns true
     * for a successful deletion, or false if the key wasn't found in the tree.
     */
    public void remove(K removeKey)
    {
        Node<K,V> curr = root; // Will point to the node to be deleted
        Node<K,V> parent = null; // Will point to the parent of the node to be deleted.

        while (curr != null && removeKey.compareTo((K)curr.key) !=0){

            // Descend through the tree, looking for the node that contains removekey.
            // Stop when we find it, or when we encounter a null pointer.
            parent = curr;
             if(removeKey.compareTo(curr.key) < 0){
                 curr = curr.left;
             }
             else {
                 curr = curr.right;
             }
        }
        // At this point, curr is null, or we’ve successfully found removeKey in the tree.
        if(curr == null){
            return;  // removeKey wasn’t found in the tree. do nothing
        }
        // We’ve found removeKey at the "curr" node, so remaining code will
        // delete curr from the tree.

        // Handle 2−child situation first.
        if(curr.left !=null && curr.right !=null){

            // Find inorder successor of curr.
            Node<K,V> successor = curr.right;
            Node<K,V> successorParent = curr;

            while (successor.left !=null){

                successorParent = successor;
                successor = successor.left;
            }
            // Copy the inorder successor’s key and value into curr.
            curr.key = successor.key;
            curr.value = successor.value;

            // Continue with the code below that will delete the successor node, which
            // is guaranteed to have 0 children or 1 child.
            curr = successor;
            parent = successorParent;
        }
        // Handle 0−child or 1−child situation.
        Node<K,V> subtree; // Will point to the subtree of curr that exists, if there is one,
        // or null if it has 0 children.
        if (curr.left == null && curr.right == null){ // 0 children
            subtree = null;
        }
        else if (curr.left != null){
            subtree = curr.left;
        }
        else {
            subtree = curr.right;
        }
        // Attach subtree to the correct child pointer of the parent node, if it exists.
        // If there is no parent, then we are deleting the root node, and the subtree becomes the new root.
        if (parent == null) {
            root = subtree;
        }
        else if (parent.left == curr){ // Deleting parent’s left child.
            parent.left = subtree;
        }
        else {    // Deleting parent’s left child.
            parent.right = subtree;
        }
    }

    /**
     * Return the number of key-value pairs in this BST.
     */
    public int size()
    {
        return size(root);
    }

    /** helper function for size.
     */
    private  int size(Node<K,V> node)
    {
        if (node == null) // if is empty
            return 0;
        else
            return(size(node.left) + 1 + size(node.right));
    }


    /**
     * Return the height of this BST.
     */

    public int height(){
        return height(root);
    }

    /** helper function for height.
     */
    private int height(Node<K,V> node)
    {
        if (node == null)
            return -1;
        else
        {
            // compute the height of each subtree
            int leftHeight = height(node.left);
            int rightHeight = height(node.right);

            /* use the larger one */
            if (leftHeight > rightHeight)
                return (leftHeight + 1);
            else
                return (rightHeight + 1);
        }
    }

    /**
     * Return a List of the keys in this BST, ordered by a preorder traversal.
     */
    public List<K> preorderKeys()
    {
        ArrayList<K> AKeys = new  ArrayList<K>(); // arraylist to hold keys
       return preorderKeys(root,AKeys);
    }

    private List<K> preorderKeys(Node<K,V> node,ArrayList<K> AKeys)
    {

        if (node != null)
        {
            AKeys.add(node.key); // data of node
            preorderKeys(node.left,AKeys); // recur on left subtree
            preorderKeys(node.right,AKeys); // recur on right subtree
        }
        return AKeys;
    }

    /**
     * Return a List of the keys in this BST, ordered by a inorder traversal.
     */
    public List<K> inorderKeys()
    {
        ArrayList<K> AKeys1 = new  ArrayList<K>();
        return inorderKeys(root,AKeys1);
    }

    private List<K> inorderKeys(Node<K,V> node, ArrayList<K> AKeys1)
    {

        if (node != null)
        {
            inorderKeys(node.left,AKeys1); // recur on left subtree
            AKeys1.add(node.key); // data of node
            inorderKeys(node.right,AKeys1); // recur on right subtree
        }
        return AKeys1;
    }



    /**
     * Return a List of the keys in this BST, ordered by a postorder traversal.
     */
    public List<K> postorderKeys()
    {
        ArrayList<K> AKeys2 = new  ArrayList<K>();
       return postorderKeys(root,AKeys2);
    }

    private List<K> postorderKeys(Node<K,V> node, ArrayList<K> AKeys2)
    {
        if (node != null)
        {
            postorderKeys(node.left,AKeys2); // recur on left subtree
            postorderKeys(node.right,AKeys2); // recur on right subtree
            AKeys2.add(node.key); // data of node
        }
        return AKeys2;
    }


    /**
     * It is very common to have private classes nested inside other classes.  This is most commonly used when
     * the nested class has no meaning apart from being a helper class or utility class for the outside class.
     * In this case, this Node class has no meaning outside of this BSTMap class, so we nest it inside here
     * so as to not prevent another class from declaring a Node class as well.
     *
     * Note that even though the members of node are public, because the class itself is private
     */
    private static class Node<K extends Comparable<K>, V> {
        public K key = null;
        public V value = null;
        public Node<K, V> left = null;     // you may initialize member variables of a class when they are defined;
        public Node<K, V> right = null;    // this behaves as if they were initialized in a constructor.
    }
}
