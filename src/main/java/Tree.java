/*
 * The driven force to made this:
 * Without visualizing the tree, the debugging process may be harsh,
 * so this tree visualizer may be helpful when developing tree operation functions.
 *
 *
 * README: This project depend on Processing as dependency and
 * using Maven to manage dependencies, go to pom.xml to synchronize if the ide report error (JDK-11)
 * (Better open it using IntelliJ, there will be glitches on Eclipse)
 * I wrote a simple visualizer using Processing to visualize the tree in real time, and you can apply
 * rotateCCW and rotateCW in the prompt by input key value of the tree and rotate mode
 * , then you can see the change of the tree in realtime (p.s the display of the node may overlap, as the visualizer is simple)
 *   key value and height is displayed as <height>-<key> on the node
 *   Modified on 25-Feb-2021
 *
 *  README:
 *  The Tree visualizer has been upgraded, it can calculate the offset of Nodes when drawing, so
 *  the Node over lapping problem has been solved.
 *  Also the tree can be drag by mouse or move by pressing direction key
 *  @see Node, this visualizer only requires same Node class, every tree that implement
 *  the Node that same as the Node class in this project can be visualized
 *  Modified on 6-Mar-2021.
 *
 * */


import java.util.*;

public class Tree<E extends Comparable<E>> {
    Node<E> root;

    public Tree(E obj) {
        root = new Node<E>(obj);
    }

    public Tree() {
        root = null;
    }


    //test method

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Tree<Integer> t = new Tree<>(7);
        t.add(3);
        t.add(5);
        t.add(6);
        t.add(1);
        t.add(9);
        t.add(8);
        t.add(10);

        Visualizer.treeVisualize(t); //TODO:

        for (; ; ) {
            int key = 0;
            try {
                System.out.println("Input key: ");
                key = in.nextInt();
            } catch (InputMismatchException e) {
                in.next();
                continue;
            }
            System.out.println("Available commands: cw, ccw, add, zig");
            String cmd = in.next();

            switch (cmd.toLowerCase().trim()) {
                case "cw":
                    t.rotateCW(key);
                    break;
                case "ccw":
                    t.rotateCCW(key);
                    break;
                case "add":
                    t.add(key);
                    break;
                case "zig":
                    t.zigZigZagZag(key);
                    break;
                default:
                    System.out.println("unknown command\n");
            }

        }
    }

    public static void main0(String[] args) {
        Tree<Integer> t = new Tree<>(50);
        for (int i = 510; i < 1000; i += 10) {
            t.add(i);
        }

        for (int i = 490; i >= 0; i -= 10) {
            t.add(i);
        }

        for (int i = 711; i < 720; i++) {
            t.add(i);
        }

        for (int i = 300; i < 310; i++) {
            t.add(i);
        }

        Tree<Integer> t2 = new Tree<>();

        for (int i = 0; i < 10; i++) {
            if (i == 6)
                continue;
            t2.add(i);

        }
        t2.add(6);
        for (int i = -1; i > -10; i--) {
            t2.add(i);
        }

        t2.rotateCCW(5);
        t2.rotateCCW(0);


        t.rotateCW(170);
        System.out.println(t.rotateCW(300));
        t.rotateCCW(714);
        t.rotateCCW(650);
        t.rotateCCW(300);

        Visualizer.treeVisualize(t); //fixme

        System.out.println(t.getHeight(10));

        System.out.println(t.root.height);
    }

    public static void main2(String[] args) {
        Tree<Integer> t = new Tree<Integer>();
        for (int i = 50; i < 100; i += 10) {
            t.add(i);
        }

        for (int i = 40; i >= 0; i -= 10) {
            t.add(i);
        }


        t.printPreorder();
    }

    public  Node nodeBuilder(E obj){
        return new Node(obj);
    }

    /**
     * @return 1 , node on parent left, 0 on parent right, -1 no parent
     */
    public int onParentLeft(Node n) {
        if (n.parent == null) {
            return -1;
        }
        if (n.parent.key.compareTo(n.key) > 0) {
            return 1;
        } else {
            return 0;
        }
    }



    public boolean zigZigZagZag(E key) {
        int[] situation = new int[2];
        Node target = find(key);
        Node parent = target.parent;
        if (target == null)
            return false;

        situation[0] = onParentLeft(target);
        if (situation[0] == -1) {
            return false;
        }
        Node grandParent = parent.parent;
        situation[1] = onParentLeft(parent);

        if (situation[1] == -1) {
            if (situation[0] == 1) {
                rotateCW((E) parent.key);
            } else {
                rotateCCW(((E) parent.key));
            }
        } else if (situation[0] == situation[1]) {
            if (situation[0] == 1) {
                rotateCW((E) grandParent.key);
                rotateCW((E) parent.key);

            } else {
                rotateCCW((E) grandParent.key);
                rotateCCW((E) parent.key);
            }
        } else if (situation[0] != situation[1]) {
            if (situation[0] == 1) {
                rotateCW((E) parent.key);
                rotateCCW((E) grandParent.key);
            } else {
                rotateCCW((E) parent.key);
                rotateCW((E) grandParent.key);
            }
        }


        return true;
    }


    public boolean add(E obj) {
        if (root == null) {
            root = new Node(obj);
            return true;
        }
        return add(root, new Node(obj)) >= 0;
    }

    public void printPreorder() {
        printPreorder(root);
    }

    private void printPreorder(Node root) {
        System.out.println(root.key.toString());

        if (root.hasLeft()) {
            printPreorder(root.left);
        }

        if (root.hasRight()) {
            printPreorder(root.right);
        }
    }

    public boolean rotateCW(E key) {
        Node target = find(key);
        if (target == null) {
            return false;
        }
        if (target.hasLeft()) {
            Node child = target.left;
            if (child.hasRight()) {
                target.left = child.right;
                child.right.parent = target;
            } else {
                target.left = null;
            }
            rotateParentUpdate(target, child);
            child.parent = target.parent;
            target.parent = child;
            child.right = target;
            updateHeight(root);
            return true;
        }
        return false;
    }


    public boolean rotateCCW(E key) {
        Node target = find(key);
        if (target == null) {
            return false;
        }
        if (target.hasRight()) {
            Node child = target.right;
            if (child.hasLeft()) {
                target.right = child.left;
                child.left.parent = target;
            } else {
                target.right = null;
            }
            rotateParentUpdate(target, child);
            child.parent = target.parent;
            target.parent = child;
            child.left = target;
            updateHeight(root);
            return true;
        }
        return false;
    }

    //assist method for rotateCW and rotateCCW
    private void rotateParentUpdate(Node target, Node child) {
        if (target.parent != null) {
            if (target.parent.key.compareTo(target.key) > 0) {
                target.parent.left = child;
            } else {
                target.parent.right = child;
            }
        } else {
            root = child;
        }
    }


    /**
     * @param root    the root of a tree(or subtree)
     * @param newNode the newly added Node to the tree
     * @return if return -1, there is duplicate node, add operation is failed(duplicate)
     * if return value >0, is the value of path length from newly added Node to child of current Node
     * if return 0, indicates no need to update height
     * @description this one seems complicate, but straight forward, implementation is basically same as the possible
     * solution 2, but perhaps better performance, as it only check-update within the path.
     * <p><p/>
     * <p>
     * When adding the newNode, there is two situation:
     * one is the parent of newly added leaf has no child
     * before add operation. In this case the height update should be checked through the path.
     * <p>
     * The other is parent of newly added leaf has a child
     * so the height of its parent is unchanged after adding.
     * <p>
     * If it is the first case, the method return 1, and previous level of
     * recursion caller will know the update-check should be performed.
     * If during the update-check there is node that its height is higher than
     * the height count from the path, return 0. Telling the previous recursion caller no
     * need to perform update-check.
     * If second case, return 0, there is no check needed through the path.
     * <p/>
     */
    public int add(Node root, Node newNode) {
        int doUpdate;

        if (root.key.compareTo(newNode.key) == 0) {
            return -1;
        }

        //left
        if (root.key.compareTo(newNode.key) > 0) {
            if (root.hasLeft()) {
                doUpdate = add(root.left, newNode);
                if (doUpdate > 0) {
                    if (root.height < doUpdate + 1) {
                        root.height = doUpdate + 1;
                        return ++doUpdate;   // return the height of current pass to caller(parent)
                    } else {
                        return 0;
                    }
                }
                return doUpdate;

            } else {
                root.left = newNode;
                newNode.parent = root;
                if (!root.hasRight()) {
                    root.height++;
                    return 1;
                }
                return 0;
            }

        } else { //right
            if (root.hasRight()) {
                doUpdate = add(root.right, newNode);
                if (doUpdate > 0) {
                    if (root.height < doUpdate + 1) {
                        root.height = doUpdate + 1;
                        return ++doUpdate;           // return height of current root node to caller

                    } else {
                        return 0;              // if height of current node is larger than the path length,
                        // no need to perform update operation to caller(parent node)
                    }
                }
                return doUpdate;
            } else {
                root.right = newNode;
                newNode.parent = root;
                if (!root.hasLeft()) {
                    root.height++;
                    return 1;
                }
                return 0;
            }
        }
    }

    private Node find(E key, Node root) {
        Node result = null;
        if (root.key.equals(key)) {
            return root;
        }
        if (root.hasLeft()) {
            result = find(key, root.left);
        }
        if (root.hasRight() && result == null) {
            result = find(key, root.right);
        } else {
            return result; // if not null, result is found on left subtree, no need to go through
        }
        return result;
    }


    public int updateHeight(Node n) {

        if (n == null) {
            return -1;
        }

        int heightOfLeftSubtree = updateHeight(n.left);
        int heightOfRightSubtree = updateHeight(n.right);

        n.height = Math.max(heightOfLeftSubtree, heightOfRightSubtree) + 1;
        return n.height;
    }

    /**
     * @param key the key value wanted to search in the tree
     * @return null if not found, or Node in the tree contains the indicated key.
     */
    public Node find(E key) {
        return find(key, root);
    }

    public int getHeight(E key) {
        try {
            return find(key).height;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public void chkEmptyTree() {
        if (root == null) {
            throw new RuntimeException("Empty Tree");
        }
    }


    class Node<E extends Comparable<E>> {
        E key = null;
        Node left = null;
        Node right = null;
        int height = 0;
        Node parent;

        public Node() {
        }

        public Node(E obj) {
            this.key = obj;

        }

        public Node(Node<E> left, Node<E> right, E value) {
            this.left = left;
            this.right = right;
            this.key = value;
        }

        public boolean hasLeft() {
            return this.left != null;
        }

        public boolean hasRight() {
            return this.right != null;
        }
    }
}
