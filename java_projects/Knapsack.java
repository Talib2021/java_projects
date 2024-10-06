import java.util.Arrays;
import java.util.Scanner;

public class Knapsack {
    public static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static int knapsack(int[] wt, int[] pft, int cp, int n) {
        int[][] tb = new int[n + 1][cp + 1];

        System.out.println("Value of DP table at intermediate steps: ");
        System.out.print("     weight :    ");
        for (int i = 0; i <= cp; i++)
            System.out.print(i + "           ");

        System.out.println("\n");

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= cp; j++) {
                if (i == 0 || j == 0) {
                    tb[i][j] = 0;
                } else if (wt[i] <= j) {
                    tb[i][j] = max(tb[i - 1][j], tb[i - 1][j - wt[i]] + pft[i]);
                } else {
                    tb[i][j] = tb[i - 1][j];
                }
            }
            System.out.print("At i=" + i + " wt=" + wt[i] + " :    ");
            for (int t = 0; t <= cp; t++) {
                System.out.print(tb[i][t] + "           ");
            }
            System.out.println();
        }

        int[] arr = new int[n];
        int i = n, j = cp;
        System.out.println("\nSelected items are: ");
        System.out.println("Item\t" + "weight\t" + "value");

        while (i > 0 && j > 0) {
            if (tb[i][j] != tb[i - 1][j]) {
                // The item i was selected
                System.out.println(i + "\t " + wt[i] + "\t " + pft[i]);
                j -= wt[i];
                arr[i - 1] = 1;
            }
            i--;
        }

        System.out.println("\nFinal knapsack array is: ");
        System.out.print("Items : ");
        for (i = 0; i < n; i++) {
            System.out.print((i + 1) + "\t");
        }
        System.out.println();
        System.out.print("        ");
        for (i = 0; i < n; i++) {
            System.out.print(arr[i] + "\t");
        }
        System.out.println();

        return tb[n][cp];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of objects : ");
        int qn = scanner.nextInt();

        System.out.print("Enter capacity: ");
        int capacity = scanner.nextInt();

        int[] wt = new int[qn + 1];
        wt[0] = 0;
        System.out.println("Enter weight:");
        for (int i = 1; i <= qn; i++) {
            wt[i] = scanner.nextInt();
        }

        int[] profit = new int[qn + 1];
        profit[0] = 0;
        System.out.println("Enter profits:");
        for (int i = 1; i <= qn; i++) {
            profit[i] = scanner.nextInt();
        }

        System.out.println("obj  wt  profit");
        for (int i = 1; i <= qn; i++) {
            System.out.println(i + "    " + wt[i] + "    " + profit[i]);
        }

        int ans = knapsack(wt, profit, capacity, qn);
        System.out.println("\nMax profit is: " + ans);

        scanner.close();
    }
}