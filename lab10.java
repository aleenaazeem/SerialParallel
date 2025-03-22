package lab10;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class PerformanceComparison {
    // These sizes will be used to test QuickSort with increasing array lengths
    private static final int[] TEST_ARRAY_SIZES = {100, 1000, 10000, 100000};

    // Number of test cases to average LCS performance over
    private static final int NUM_LCS_TESTS = 1000;

    // Different lengths of strings for which LCS will be computed
    private static final int[] LCS_STRING_LENGTHS = {10, 50, 100};

    // Random generator for input data
    private static final Random RANDOM_GENERATOR = new Random();

    public static void main(String[] args) {
        // Begin QuickSort performance testing
        evaluateQuickSortPerformance();

        // Begin Dijkstra performance testing
        evaluateDijkstraPerformance();

        // Begin LCS performance testing
        evaluateLCSPerformance();
    }

    // Method to test and compare Serial and Parallel QuickSort
    private static void evaluateQuickSortPerformance() {
        System.out.println("\nQuickSort Execution Time Comparison:");

        // Loop through each test size to evaluate both methods
        for (int currentSize : TEST_ARRAY_SIZES) {
            // Generate a random array of integers for sorting
            int[] dataForSerial = generateRandomIntArray(currentSize);
            // Clone the original array for parallel use to ensure fairness
            int[] dataForParallel = dataForSerial.clone();

            // Start timing the Serial QuickSort execution
            long serialStart = System.nanoTime();
            SerialQuickSort.quickSort(dataForSerial, 0, currentSize - 1);
            long serialDuration = System.nanoTime() - serialStart;

            // Initialize the ForkJoinPool for parallel sorting
            ForkJoinPool forkJoinPool = new ForkJoinPool();

            // Start timing the Parallel QuickSort execution
            long parallelStart = System.nanoTime();
            forkJoinPool.invoke(new ParallelQuickSort.QuickSortTask(dataForParallel, 0, currentSize - 1));
            long parallelDuration = System.nanoTime() - parallelStart;

            // Output the time taken by each sorting approach
            System.out.printf("Array Size: %d | Serial Time: %d ns | Parallel Time: %d ns\n",
                    currentSize, serialDuration, parallelDuration);
        }
    }

    // Method to test and compare Serial and Parallel Dijkstra algorithms
    private static void evaluateDijkstraPerformance() {
        System.out.println("\nDijkstra Shortest Path Timing:");

        // Create a random graph with 1000 nodes for this test
        int[][] testGraph = createRandomWeightedGraph(1000);

        // Measure execution time for Serial Dijkstra
        long serialStart = System.nanoTime();
        SerialDijkstra.dijkstra(testGraph, 0);
        long serialElapsed = System.nanoTime() - serialStart;

        // Measure execution time for Parallel Dijkstra
        long parallelStart = System.nanoTime();
        ParallelDijkstra.dijkstra(testGraph, 0);
        long parallelElapsed = System.nanoTime() - parallelStart;

        // Display timing results for both implementations
        System.out.printf("Graph Size: 1000 | Serial Time: %d ns | Parallel Time: %d ns\n",
                serialElapsed, parallelElapsed);
    }

    // Method to test and compare Serial and Parallel LCS implementations
    private static void evaluateLCSPerformance() {
        System.out.println("\nLCS (Longest Common Subsequence) Timing Results:");

        // Loop through different string lengths to test LCS
        for (int stringLength : LCS_STRING_LENGTHS) {
            long totalSerialTime = 0;
            long totalParallelTime = 0;

            // Repeat the LCS test multiple times to average the result
            for (int i = 0; i < NUM_LCS_TESTS; i++) {
                // Generate two random strings of the specified length
                String first = generateRandomLowercaseString(stringLength);
                String second = generateRandomLowercaseString(stringLength);

                // Time the Serial LCS algorithm
                long serialStart = System.nanoTime();
                SerialLCS.lcs(first, second);
                totalSerialTime += System.nanoTime() - serialStart;

                // Time the Parallel LCS algorithm
                long parallelStart = System.nanoTime();
                ParallelLCS.lcs(first, second);
                totalParallelTime += System.nanoTime() - parallelStart;
            }

            // Compute and print average execution times for each method
            long averageSerial = totalSerialTime / NUM_LCS_TESTS;
            long averageParallel = totalParallelTime / NUM_LCS_TESTS;

            System.out.printf("String Length: %d | Avg Serial Time: %d ns | Avg Parallel Time: %d ns\n",
                    stringLength, averageSerial, averageParallel);
        }
    }

    // Utility to generate a random integer array of given size
    private static int[] generateRandomIntArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = RANDOM_GENERATOR.nextInt(10000); // Random number from 0 to 9999
        }
        return array;
    }

    // Utility to create a random weighted directed graph as an adjacency matrix
    private static int[][] createRandomWeightedGraph(int nodeCount) {
        int[][] graph = new int[nodeCount][nodeCount];

        // Randomly populate weights between nodes
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if (i != j && RANDOM_GENERATOR.nextBoolean()) {
                    // Assign a random weight between 1 and 100
                    graph[i][j] = RANDOM_GENERATOR.nextInt(100) + 1;
                }
            }
        }
        return graph;
    }

    // Utility to generate a string of random lowercase letters
    private static String generateRandomLowercaseString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // Append a character from 'a' to 'z' randomly
            result.append((char) ('a' + RANDOM_GENERATOR.nextInt(26)));
        }
        return result.toString();
    }
}
