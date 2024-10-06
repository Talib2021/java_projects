import java.util.Arrays;
import java.util.Scanner;

public class DiskScheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: Initial head position
        System.out.print("Enter the initial head position: ");
        int initialHead = scanner.nextInt();

        // Input: Cylinder sequence
        System.out.print("Enter the number of cylinders: ");
        int numCylinders = scanner.nextInt();
        System.out.print("Enter the cylinder sequence (space-separated): ");
        int[] cylinderSequence = new int[numCylinders];
        for (int i = 0; i < numCylinders; i++) {
            cylinderSequence[i] = scanner.nextInt();
        }

        // Calculate and display FCFS head movement
        int fcfsHeadMovement = calculateFCFS(initialHead, cylinderSequence);
        System.out.println("FCFS Head Movement: " + fcfsHeadMovement + " units");

        // Calculate and display SCAN head movement
        System.out.print("Enter the initial direction of SCAN (up or down): ");
        String scanDirection = scanner.next().toLowerCase();
        int scanHeadMovement = calculateSCAN(initialHead, cylinderSequence, scanDirection);
        System.out.println("SCAN Head Movement: " + scanHeadMovement + " units");

        scanner.close();
    }

    // FCFS Algorithm
    private static int calculateFCFS(int initialHead, int[] cylinderSequence) {
        int headMovement = 0;
        for (int cylinder : cylinderSequence) {
            headMovement += Math.abs(initialHead - cylinder);
            System.out.print( initialHead+" =>");
            initialHead = cylinder;
        }
        return headMovement;
    }

    // SCAN Algorithm with Reversal of Direction
    private static int calculateSCAN(int initialHead, int[] cylinderSequence, String direction) {
        int headMovement = 0;

        // Sort the cylinder sequence
        Arrays.sort(cylinderSequence);

        // Find the index where the initial head is located
        int initialHeadIndex = Arrays.binarySearch(cylinderSequence, initialHead);

        // If the initial head is not in the sequence, find the position to insert it
        if (initialHeadIndex < 0) {
            initialHeadIndex = -initialHeadIndex - 1;
        }

        if (direction.equals("up")) {
            // Move in the up direction
            for (int i = initialHeadIndex; i < cylinderSequence.length; i++) {
                headMovement += Math.abs(initialHead - cylinderSequence[i]);
                System.out.print( initialHead+" =>");
                initialHead = cylinderSequence[i];
            }
            // Move to the end
            headMovement += Math.abs(initialHead - cylinderSequence[cylinderSequence.length - 1]);
            // System.out.print( initialHead);

            // Reverse direction
            // Move to the beginning in the opposite direction
           // headMovement += Math.abs(initialHead - cylinderSequence[0]);
            for (int i = initialHeadIndex-1;i>=0; i--) {
                headMovement += Math.abs(initialHead - cylinderSequence[i]);
                System.out.print( initialHead+" =>");
                initialHead = cylinderSequence[i];
            }
             System.out.print( initialHead);

        } else if (direction.equals("down")) {
            // Move in the down direction
            for (int i = initialHeadIndex-1; i >= 0; i--) {
                headMovement += Math.abs(initialHead - cylinderSequence[i]);
                System.out.print( initialHead+" =>");
                initialHead = cylinderSequence[i];
            }
            // Move to the beginning
            headMovement += Math.abs(initialHead - cylinderSequence[0]);
            // System.out.print( initialHead);

            // Reverse direction
            // Move to the end in the opposite direction
           // headMovement += Math.abs(initialHead - cylinderSequence[cylinderSequence.length - 1]);
            for (int i = initialHeadIndex; i <cylinderSequence.length; i++) {
                headMovement += Math.abs(initialHead - cylinderSequence[i]);
                System.out.print( initialHead+" =>");
                initialHead = cylinderSequence[i];
            }
             System.out.print( initialHead);
        }

        return headMovement;
    }
}
