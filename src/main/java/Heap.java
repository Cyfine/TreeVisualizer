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
        System.out.println(heap.insert(5));
        System.out.println(heap.insert(10));
        System.out.println(heap.insert(2));
        System.out.println(heap.insert(3));
        System.out.println(heap.insert(4));
        System.out.println(heap.insert(9));
        System.out.println(heap.insert(78));
        System.out.println(heap.insert(79));
        System.out.println(heap.insert(74));
        System.out.println(heap.insert(75));
        System.out.println(heap.insert(76));

        int size = heap.keys[0];
//        for (int i = 0; i < size; i++) {
//            System.out.println(heap.deleteMin());
//        }
        Visualizer.heapVisualize(heap.keys);


        System.out.println(depth(8));

    }

    /*
     *             1
     *           2   3
     *          4 5 6 7
     *
     *
     *
     *
     *
     * */

    /**
     * @param key the key value insert into the heap
     * @reuturn false if the heap is full or there is duplicate values in the heap
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
     * @param idx the index of elment in the array that will be adjusted
     */
    public void percolateDown(int idx) {
        int childIdx = 2 * idx;
        if (childIdx > keys[0]) {
            return;
        }
        if (keys[childIdx] > keys[childIdx + 1]) {
            childIdx = childIdx + 1;
        }
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
        int result = keys[1];
        keys[1] = keys[keys[0]];
        keys[0]--;

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

    // this method suppose the data array is not full
    public static Heap buildHeap(int[] data) {
        Heap heap = new Heap();
        int currentPos = data[0];

        data[data.length - 1] = data[0];
        data[0] = data.length - 1;


        return heap;
    }


    public static int depth(int index) {
        double logarithmDepth = Math.log(index + 1) / Math.log(2) - 1;
        if (Math.ceil(logarithmDepth) == Math.floor(logarithmDepth)) {
            return (int) logarithmDepth;
        } else {
            return (int) Math.floor(logarithmDepth) + 1;
        }
    }

}




