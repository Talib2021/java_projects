import java.util.Scanner;

public class Knapsackproblem {

    public static int knapsack(int[] values, int[] weights, int capacity, int[] selectedItems) {
        int n = values.length;
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], values[i - 1] + dp[i - 1][w - weights[i - 1]]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        int w = capacity;
        int itemCount = 0;

        for (int i = n; i > 0 && w > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selectedItems[itemCount] = i;
                itemCount++;
                w -= weights[i - 1];
            }
        }

        return dp[n][capacity];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of items: ");
        int n = scanner.nextInt();

        int[] values = new int[n];
        int[] weights = new int[n];

        System.out.println("Enter the values of items:");
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
        }

        System.out.println("Enter the weights of items:");
        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }

        System.out.print("Enter the capacity of the knapsack: ");
        int capacity = scanner.nextInt();

        scanner.close();

        int[] selectedItems = new int[n];
        int maxValue = knapsack(values, weights, capacity, selectedItems);

        System.out.println("Maximum value: " + maxValue);
        System.out.print("Selected items: ");
        for (int i = 0; i < n && selectedItems[i] != 0; i++) {
            System.out.print("Item " + selectedItems[i] + " "+"whose value is "+ values[selectedItems[i]-1]);
             System.out.print("\n");
        }
    }
}