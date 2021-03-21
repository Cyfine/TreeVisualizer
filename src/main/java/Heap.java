/*
 * Advertisement:
 * New Feature!
 * The new feature added to the Tree visualizer. It can visualizer array implemented binary tree.
 * See on my GitHub: https://github.com/Cyfine/TreeVisualizer
 * Leave a star give a little support if you like!
 *
 * Readme:
 *  If the deleteMin from an empty Heap, a Runtime exception will be thrown
 *
 *
 *
 * */

import java.util.Random;
import java.util.Scanner;

public class Heap {
    int[] keys;


    public Heap(int size) {
        keys = new int[size + 1];
        keys[0] = 0;
    }

    //default constructor
    public Heap() {

    }

    public static void main(String[] args) {
        Heap heap = new Heap(10);
        int size = heap.keys[0];


        int[] sampleArray = {8, 5, 9, 2, 6, 7, 4, 1, 0}; // eight elements in total: 1 2 4 5 6 7 8 9
        int[] sampleArray2 = {5, 6, 9, 4, 3, 50, 54, 12, 76, 90, 22, 90, 67};
        int[] sampleArray3 = new int[1000];
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            sampleArray3[i] = rand.nextInt(1000);
        }

        Heap test = buildHeap(sampleArray);
        Scanner in = new Scanner(System.in);

        //CLI heap test program:
        for (; ; ) {
            System.out.println("Input command: ");
            String cmd = in.nextLine();
            switch (cmd) {
                case "del":
                    System.out.println("Deleted value:" + test.deleteMin());
                    break;
                case "ins":
                    System.out.println("Input insert value: ");
                    int value = in.nextInt();
                    if (!test.insert(value)) {
                        System.out.println("Insert failed!\n");
                    }
                    break;
            }
        }
    }


    /**
     * @param key the key value insert into the heap
     * @return false if the heap is full or there is duplicate values in the heap
     */
    public boolean insert(int key) {
        if (keys[0] == keys.length - 1 || find(key)) {
            return false;
        } else {
            keys[0]++;
            keys[keys[0]] = key;
        }
        percolateUp(keys[0]);
        return true;
    }

    /**
     * @param index, the index of the element that will be adjusted in the heap
     */
    public void percolateUp(int index) {
        int parentIdx = index / 2;
        if (parentIdx == 0) {
            return;
        }
        int temp;
        if (keys[parentIdx] > keys[index]) {
            temp = keys[parentIdx];
            keys[parentIdx] = keys[index];
            keys[index] = temp;
            percolateUp(parentIdx);
        }
    }

    /**
     * @param idx the index of element in the array that will be adjusted
     */
    public void percolateDown(int idx) {
        int childIdx = 2 * idx;
        boolean dualChild = true;
        if (childIdx + 1 > keys[0]) {
            if (childIdx > keys[0]) {
                return;
            } else {
                dualChild = false;
            }

        }


        // If the Node has two children, it will compare the size of its two children, and choose the index of the smaller child
        if (dualChild && keys[childIdx] > keys[childIdx + 1]) {
            childIdx = childIdx + 1;
        }

        //compare the value of the smaller child with its paret
        if (keys[childIdx] < keys[idx]) {
            int temp = keys[idx];
            keys[idx] = keys[childIdx];
            keys[childIdx] = temp;
            percolateDown(childIdx);
        }

    }

    /**
     * @return false if the key value is not found in the heap array
     */
    public boolean find(int key) {
        return findPos(key) != -1;
    }

    public int deleteMin() {
        if (keys[0] == 0) {
            throw new RuntimeException("Delete from empty Heap!");
        }
        int result = keys[1];
        keys[1] = keys[keys[0]];
        keys[0]--;
        // System.out.println("size after deletion: "+ keys[0]);
        percolateDown(1);


        return result;
    }

    /**
     * @ return position of the found element in the array, return -1 if the element is not found
     */
    public int findPos(int key) {
        for (int i = 1; i <= keys[0]; i++) {
            if (key == keys[i])
                return i;
        }
        return -1;
    }

    /*
     * Build build heap algorithm, percolate down from from the [size/2] element,
     * the percolateDown method can adjust the heap recursively.
     * */
    public static Heap buildHeap(int[] data) {
        Heap heap = new Heap();
        int currentPos = data[0];

        data[data.length - 1] = data[0];
        data[0] = data.length - 1;

        heap.keys = data;

        for (int i = data[0] / 2; i > 0; i--) {
            heap.percolateDown(i);
        }


        return heap;
    }

    /**
     * method to get the depth of Node by index
     */
    public static int depth(int index) {
        double logarithmDepth = Math.log(index + 1) / Math.log(2) - 1;
        if (Math.ceil(logarithmDepth) == Math.floor(logarithmDepth)) {
            return (int) logarithmDepth;
        } else {
            return (int) Math.floor(logarithmDepth) + 1;
        }
    }

}




