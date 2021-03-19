import processing.core.PApplet;

import java.lang.reflect.Array;

public class Visualizer extends PApplet {
    //=========applet Settings==========
    static int width = 800;
    static int height = 800;
    int x = width / 2;
    int y = height / 6;
    boolean drag;
    boolean visualizeHeap = false;

    //========static fields========
    static Tree tree ;
    static int[] heapArray;
    static int[] arrayBuffer; //used to check changes of heap and synchronize

    //=============================

    //===========Applet boot, static field setting============
    public static void treeVisualize(Tree tree) {
        Visualizer v = new Visualizer();
        v.tree = tree;
        String[] appletArgs = new String[]{"Visualizer"};
        v.main(appletArgs);
    }


    public static void heapVisualize(int[] array){
        Visualizer v = new Visualizer();

        v.tree = parseHeapArrayToTree(array);
        v.visualizeHeap = true;
        arrayBuffer = new int[array.length];
        heapArray = array ;
        System.arraycopy(array, 0, arrayBuffer, 0, array.length);
        String[] appletArgs = new String[]{"Visualizer"};
        v.main(appletArgs);
    }

    public static void heapSynchronize(){
       for(int i = 0; i < heapArray.length; i++){
           if(heapArray[i] != arrayBuffer[i]){
               tree = parseHeapArrayToTree(heapArray);
               System.arraycopy(heapArray, 0, arrayBuffer, 0, heapArray.length);

           }
       }
    }



    private static Tree parseHeapArrayToTree(int[] array) {
        Tree<Integer> t = new Tree<Integer>();
        Tree.Node[] nodes = new Tree.Node[array[0] + 1];
        for (int i = 1; i <= array[0]; i++) {
            nodes[i] = t.nodeBuilder(array[i]);
        }

        for (int i = 0; i * 2 < nodes.length; i++) {


            nodes[i].left = nodes[i * 2];
            nodes[i*2].parent = nodes[i];
            if (i * 2 + 1 < nodes.length) {
                nodes[i].right = nodes[i * 2 + 1];
                nodes[i*2+1].parent = nodes[i];

            }

        }
        t.root = nodes[1]; //fixme
        return t;
    }

    public void setup() {
        size(width, height);
        frameRate(60);
    }

    public void draw() {

        keyPressed();
        keyReleased();
        background(255);

        if(visualizeHeap){
            heapSynchronize();
        }

        drawTree(tree);
        fill(0, 255, 0);

        if (mousePressed && mouseListener(x, y, 30)) {
            drag = true;
        }
        if (drag) {
            x = mouseX;
            y = mouseY;
        }
        if (keyCode == UP && keyPressed) {
            y -= 10;
        }

        if (keyCode == DOWN && keyPressed) {
            y += 10;
        }

        if (keyCode == RIGHT && keyPressed) {
            x += 10;
        }

        if (keyCode == LEFT && keyPressed) {
            x -= 10;
        }

    }


    public void scroll() {
        if (mouseX > (int) width * 0.8) {
            x += width / 40;
        }
        if (mouseX < (int) width * 0.2) {
            x -= width / 40;
        }
        if (mouseY > (int) height * 0.8) {
            y += height / 40;
        }
        if (mouseY < (int) width * 0.2) {
            y -= height / 40;
        }

        if (x < 0) {
            x = 0;
        }
        if (x > width) {
            x = width;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > height) {
            y = height;
        }
    }


    public void drawTree(Tree t, int x, int y) {
        drawNode(t.root, x, y, 100, 100, false);
    }

    public void drawTree(Tree t) {
        drawNode(t.root, x, y, 100, 100, false);
    }


    public void drawNode(Tree.Node n, int x, int y, int colorL, int colorR, boolean isRight) {

        //offset calculation, make sure the nodes won't overlap
        int lOffset = getTreeRightOffset(n.left, 20) + 20;
        int rOffset = getTreeLeftOffset(n.right, 20) + 20;
        if (rOffset > 5 * lOffset && lOffset != 0) {
            rOffset = (rOffset + lOffset) / 2;
            lOffset = rOffset;
        }
        if (lOffset > 5 * rOffset && rOffset != 0) {
            lOffset = (lOffset + rOffset) / 2;
            rOffset = lOffset;
        }

        //draw right Node
        if (n.hasRight()) {
            line(x, y, x + rOffset, y + 20);
            drawNode(n.right, x + rOffset, y + 20, colorL, colorR + 20, true);

        }

        //draw left Node
        if (n.hasLeft()) {
            line(x, y, x - lOffset, y + 20);
            drawNode(n.left, x - lOffset, y + 20, colorL + 20, colorR, false);

        }


        if (isRight) {
            fill(200, colorL, colorR);
        } else {
            fill(colorL, colorR, 200);
        }

        if (mouseListener(x, y, 20)) {
            showKey(n.key.toString(), "" + n.height);
            fill(255, 255, 0);

        }
        ellipse(x, y, 20, 20);
        fill(0);
        if (n.key.toString().length() <= 3 || mouseListener(x, y, 20)) {
            text(n.key.toString(), x - 4, y + 5);
        } else {
            text("...", x - 4, y + 5);
        }
//        text(n.height + "-" + n.key.toString(), x, y);
    }


    public int getTreeRightOffset(Tree.Node r, int horizonInterval) {
        if (r == null) {
            return -horizonInterval;
        }
        if (r.right == null) {
            return 0;
        }
        int rOffset = getTreeRightOffset(r.right, horizonInterval);
        int lOffset = getTreeRightOffset(r.left, horizonInterval) - 2 * horizonInterval;

// if r.right has left tree, the length offset should be considered, else may cause Node overlap
        return Math.max(rOffset, lOffset) + horizonInterval + getTreeLeftOffset(r.right, horizonInterval);
    }


    public int getTreeLeftOffset(Tree.Node l, int horizonInterval) {
        if (l == null) {
            return -horizonInterval;
        }
        if (l.left == null) {
            return 0;
        }
        int rOffset = getTreeLeftOffset(l.right, horizonInterval) - 2 * horizonInterval;
        int lOffset = getTreeLeftOffset(l.left, horizonInterval);


        return Math.max(rOffset, lOffset) + horizonInterval + getTreeRightOffset(l.left, horizonInterval);
    }

    public boolean mouseListener(int x, int y, int detectSize) {
        return mouseX < x + detectSize / 2 && mouseX > x - detectSize / 2 && mouseY < y + detectSize / 2 && mouseY > y - detectSize / 2;
    }

    public void showKey(String... str) {
        fill(0);
        text("Current key: " + str[0], 10, 20);
        text("Node height: " + str[1], 10, 40);
        text("Drag root or press direction keys to move the tree", 10, 60);

    }

    public void mouseReleased() {
        drag = false;
    }
}
