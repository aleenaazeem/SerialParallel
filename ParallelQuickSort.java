package lab10;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelQuickSort {
    public static void main(String[] args) {
        int[] array = {10, 7, 8, 9, 1, 5};
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new QuickSortTask(array, 0, array.length - 1));

        System.out.println("Sorted Array:");
        for (int num : array) {
            System.out.print(num + " ");
        }
    }

    static class QuickSortTask extends RecursiveAction {
        private final int[] array;
        private final int low;
        private final int high;

        public QuickSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (low < high) {
                int pi = partition(array, low, high);
                QuickSortTask leftTask = new QuickSortTask(array, low, pi - 1);
                QuickSortTask rightTask = new QuickSortTask(array, pi + 1, high);
                invokeAll(leftTask, rightTask);
            }
        }

        private int partition(int[] array, int low, int high) {
            int pivot = array[high];
            int i = (low - 1);

            for (int j = low; j < high; j++) {
                if (array[j] <= pivot) {
                    i++;
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }

            int temp = array[i + 1];
            array[i + 1] = array[high];
            array[high] = temp;

            return i + 1;
        }
    }
}
