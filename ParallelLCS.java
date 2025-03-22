package lab10;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelLCS {
    public static int lcs(String X, String Y) {
        int m = X.length();
        int n = Y.length();
        int[][] dp = new int[m + 1][n + 1];
        ForkJoinPool pool = new ForkJoinPool();

        for (int i = 1; i <= m; i++) {
            pool.invoke(new LCSRowTask(dp, X, Y, i, n));
        }

        return dp[m][n];
    }

    static class LCSRowTask extends RecursiveAction {
        private final int[][] dp;
        private final String X;
        private final String Y;
        private final int i;
        private final int n;

        public LCSRowTask(int[][] dp, String X, String Y, int i, int n) {
            this.dp = dp;
            this.X = X;
            this.Y = Y;
            this.i = i;
            this.n = n;
        }

        @Override
        protected void compute() {
            for (int j = 1; j <= n; j++) {
                if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
    }

    public static void main(String[] args) {
        String X = "AGGTAB";
        String Y = "GXTXAYB";
        System.out.println("Length of LCS: " + lcs(X, Y));
    }
}

