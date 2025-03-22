package lab10;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelDijkstra {
    public static void dijkstra(int[][] graph, int src) {
        int V = graph.length;
        int[] dist = new int[V];
        boolean[] visited = new boolean[V];

        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int count = 0; count < V - 1; count++) {
            int u = findMinDistance(dist, visited);
            if (u == -1) break;
            visited[u] = true;

            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new RelaxEdgesTask(graph, dist, visited, u));
        }

        printSolution(dist);
    }

    private static int findMinDistance(int[] dist, boolean[] visited) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < dist.length; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private static void printSolution(int[] dist) {
        System.out.println("Vertex \t Distance from Source");
        for (int i = 0; i < dist.length; i++) {
            System.out.println(i + " \t\t " + dist[i]);
        }
    }

    static class RelaxEdgesTask extends RecursiveAction {
        private final int[][] graph;
        private final int[] dist;
        private final boolean[] visited;
        private final int u;

        public RelaxEdgesTask(int[][] graph, int[] dist, boolean[] visited, int u) {
            this.graph = graph;
            this.dist = dist;
            this.visited = visited;
            this.u = u;
        }

        @Override
        protected void compute() {
            for (int v = 0; v < graph.length; v++) {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE
                        && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] graph = {
                {0, 10, 20, 0, 0},
                {10, 0, 5, 1, 0},
                {20, 5, 0, 2, 8},
                {0, 1, 2, 0, 3},
                {0, 0, 8, 3, 0}
        };
        dijkstra(graph, 0);
    }
}


