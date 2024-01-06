import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ASD3 {
    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(args[0]))) {
            int k = scanner.nextInt();
            AVLTree avlTree = new AVLTree();

            int index = 0;
            while (scanner.hasNextInt()) {
                avlTree.insert(index, scanner.nextInt());
                index++;
            }

            int pointerIndex = 0;
            int pointerNumber;
            int toMoveBy;

            // Przeprowadzenie operacji
            for (int i = 0; i < k; i++) {
                pointerNumber = avlTree.getNumberAt(pointerIndex);
                if (pointerNumber % 2 == 0) {
                    int indexToDelete = (pointerIndex + 1) % avlTree.getSize();

                    if (indexToDelete < pointerIndex)
                        pointerIndex--;

                    avlTree.delete(indexToDelete);
                    toMoveBy = avlTree.getLastDeleted();

                    if (avlTree.isEmpty()) {
                        System.out.println();
                        break;
                    }
                } else {
                    avlTree.insert(pointerIndex + 1, pointerNumber - 1);
                    toMoveBy = pointerNumber;
                }
                pointerIndex = (pointerIndex + toMoveBy) % avlTree.getSize();
            }

            // Wypisanie danych zaczynając od wskaźnika
            avlTree.printFromPointer(pointerIndex);
        }
    }
}

class Node {
    private int number;
    private Node leftChild;
    private Node rightChild;
    private int leftEleCount;
    private int rightEleCount;
    private int height;

    public Node(int number) {
        this.number = number;
        this.height = 1;
        this.leftChild = null;
        this.rightChild = null;
        this.leftEleCount = 0;
        this.rightEleCount = 0;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
        this.leftEleCount = (leftChild == null ? 0 : leftChild.getSubtreeEle());
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
        this.rightEleCount = (rightChild == null ? 0 : rightChild.getSubtreeEle());
    }

    public int getLeftEleCount() {
        return leftEleCount;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSubtreeEle() {
        return leftEleCount + 1 + rightEleCount;
    }

    public int getIndex(int prevCount) {
        return leftEleCount + prevCount;
    }
}

class AVLTree {
    private Node root;
    private int size;
    private int lastDeleted;

    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    public void insert(int index, int number) {
        root = insert(index, 0, number, root);
        size++;
    }

    public void delete(int index) {
        root = delete(index, 0, root, true);
    }

    private Node insert(int index, int prevAboveCount, int number, Node node) {
        if (node == null) {
            return new Node(number);
        }

        if (index < node.getIndex(prevAboveCount)) {
            node.setLeftChild(insert(
                    index,
                    prevAboveCount,
                    number,
                    node.getLeftChild()
            ));
        } else if (index > node.getIndex(prevAboveCount)) {
            node.setRightChild(insert(
                    index,
                    prevAboveCount + node.getLeftEleCount() + 1,
                    number,
                    node.getRightChild()
            ));
        } else {
            Node tmp = new Node(number);
            tmp.setLeftChild(node.getLeftChild());
            node.setLeftChild(null);
            updateHeight(node);
            tmp.setRightChild(balanceTree(node));

            node = tmp;
        }

        updateHeight(node);

        return balanceTree(node);
    }

    private Node delete(int index, int prevAboveCount, Node node, boolean isMainOperation) {
        // Taki element nie istnieje w drzewie
        if (node == null) {
            return null;
        }

        if (index < node.getIndex(prevAboveCount)) {
            node.setLeftChild(delete(
                    index,
                    prevAboveCount,
                    node.getLeftChild(),
                    isMainOperation
            ));
        } else if (index > node.getIndex(prevAboveCount)) {
            node.setRightChild(delete(
                    index,
                    prevAboveCount + node.getLeftEleCount() + 1,
                    node.getRightChild(),
                    isMainOperation
            ));
        } else {
            // Znaleziono węzeł do usunięcia
            if (isMainOperation) {
                lastDeleted = node.getNumber();
                size--;
            }

            // 0 dzieci lub 1 dziecko
            if (node.getLeftChild() == null) {
                return node.getRightChild();
            } else if (node.getRightChild() == null) {
                return node.getLeftChild();
            }

            // 2 dzieci
            node.setNumber(getMax(node.getLeftChild()));
            node.setLeftChild(delete(
                    node.getIndex(prevAboveCount) - 1,
                    prevAboveCount,
                    node.getLeftChild(),
                    false
            ));
        }

        updateHeight(node);

        return balanceTree(node);
    }

    private int getMax(Node node) {
        if (isEmpty()) {
            return 0;
        }

        while (node.getRightChild() != null) {
            node = node.getRightChild();
        }

        return node.getNumber();
    }

    public boolean isEmpty() {
        return root == null;
    }

    private Node balanceTree(Node node) {
        int balanceFactor = getBalanceFactorFor(node);

        if (balanceFactor < -1) {
            // Jeśli różne znaki to dodatkowa rotacja
            if (getBalanceFactorFor(node.getRightChild()) > 0) {
                node.setRightChild(rightRotation(node.getRightChild()));
            }

            return leftRotation(node);
        }

        if (balanceFactor > 1) {
            // Jeśli różne znaki to dodatkowa rotacja
            if (getBalanceFactorFor(node.getLeftChild()) < 0) {
                node.setLeftChild(leftRotation(node.getLeftChild()));
            }

            return rightRotation(node);
        }

        return node;
    }

    private Node leftRotation(Node node) {
        Node rightNode = node.getRightChild();
        Node centerNode = rightNode.getLeftChild();

        node.setRightChild(centerNode);
        rightNode.setLeftChild(node);

        updateHeight(node);
        updateHeight(rightNode);

        return rightNode;
    }

    private Node rightRotation(Node node) {
        Node leftNode = node.getLeftChild();
        Node centerNode = leftNode.getRightChild();

        node.setLeftChild(centerNode);
        leftNode.setRightChild(node);

        updateHeight(node);
        updateHeight(leftNode);

        return leftNode;
    }

    private int getBalanceFactorFor(Node node) {
        return node == null ? 0 : height(node.getLeftChild()) - height(node.getRightChild());
    }

    private void updateHeight(Node node) {
        int maxHeight = height(node.getLeftChild()) > height(node.getRightChild()) ? height(node.getLeftChild()) : height(node.getRightChild());
        node.setHeight(maxHeight + 1);
    }

    private int height(Node node) {
        return node == null ? 0 : node.getHeight();
    }

    public void inorder() {
        inorder(root);
    }

    private void inorder(Node node) {
        if (node != null) {
            inorder(node.getLeftChild());
            System.out.print(node.getNumber() + " ");
            inorder(node.getRightChild());
        }
    }

    public Node get(int index) {
        return get(root, 0, index);
    }

    private Node get(Node node, int prevAboveCount, int index) {
        if (node == null) {
            return null;
        }

        int nodeIndex = node.getIndex(prevAboveCount);
        if (index < nodeIndex) {
            return get(node.getLeftChild(), prevAboveCount, index);
        } else if (index > nodeIndex) {
            return get(node.getRightChild(), prevAboveCount + node.getLeftEleCount() + 1, index);
        } else {
            return node;
        }
    }

    public int getSize() {
        return size;
    }

    public int getLastDeleted() {
        return lastDeleted;
    }

    public void printFromPointer(int pointerIndex) {
        printAfterPointer(root, 0, pointerIndex);
        printBeforePointer(root, 0, pointerIndex);
    }

    private void printBeforePointer(Node node, int prevAboveCount, int pointerIndex) {
        if (node == null)
            return;

        printBeforePointer(node.getLeftChild(), prevAboveCount, pointerIndex);

        if (node.getIndex(prevAboveCount) < pointerIndex) {
            System.out.print(node.getNumber() + " ");
        }

        printBeforePointer(node.getRightChild(), prevAboveCount + node.getLeftEleCount() + 1, pointerIndex);
    }

    private void printAfterPointer(Node node, int prevAboveCount, int pointerIndex) {
        if (node == null)
            return;

        printAfterPointer(node.getLeftChild(), prevAboveCount, pointerIndex);

        if (node.getIndex(prevAboveCount) >= pointerIndex) {
            System.out.print(node.getNumber() + " ");
        }

        printAfterPointer(node.getRightChild(), prevAboveCount + node.getLeftEleCount() + 1, pointerIndex);
    }

    public int getNumberAt(int pointerIndex) {
        return get(pointerIndex).getNumber();
    }
}