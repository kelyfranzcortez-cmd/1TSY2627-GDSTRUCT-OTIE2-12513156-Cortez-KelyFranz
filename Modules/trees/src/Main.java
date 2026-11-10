public class Main {

    static class Node {
        int data;
        Node leftChild;
        Node rightChild;

        Node(int data) {
            this.data = data;
        }
    }

    private Node root;

    public void insert(int value) {
        root = insert(root, value);
    }

    private Node insert(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }
        if (value < node.data) {
            node.leftChild = insert(node.leftChild, value);
        } else if (value > node.data) {
            node.rightChild = insert(node.rightChild, value);
        }
        return node;
    }

    // Gets the node with the least value (leftmost node)
    public Node getMin() {
        if (root == null) return null;
        Node current = root;
        while (current.leftChild != null) {
            current = current.leftChild;
        }
        return current;
    }

    // Gets the node with the greatest value (rightmost node)
    public Node getMax() {
        if (root == null) return null;
        Node current = root;
        while (current.rightChild != null) {
            current = current.rightChild;
        }
        return current;
    }

    // In-order traversal in descending order: right -> node -> left
    public void traverseInOrderDescending() {
        traverseInOrderDescending(root);
    }

    private void traverseInOrderDescending(Node node) {
        if (node == null) return;
        traverseInOrderDescending(node.rightChild);
        System.out.println("Data: " + node.data);
        traverseInOrderDescending(node.leftChild);
    }

    public static void main(String[] args) {
        Main tree = new Main();
        int[] values = {25, 20, 15, 27, 30, 29, 26, 22, 32, 17};
        for (int v : values) {
            tree.insert(v);
        }

        System.out.println("Min: " + tree.getMin().data);
        System.out.println("Max: " + tree.getMax().data);
        System.out.println("Descending:");
        tree.traverseInOrderDescending();
    }
}