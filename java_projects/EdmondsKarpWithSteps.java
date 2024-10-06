import java.util.*;

public class EdmondsKarpWithSteps {
    
    static class Queue {
        private int maxSize;
        private int front;
        private int rear;
        private int[] queueArray;
    
        public Queue(int size) {
            maxSize = size;
            queueArray = new int[maxSize];
            front = 0;
            rear = -1;
        }
    
        public void puss(int value) {
            if (rear == maxSize - 1) {
                System.out.println("Queue is full. Cannot enqueue.");
                return;
            }
            queueArray[++rear] = value;
        }
    
        public int poll() {
            if (isEmpty()) {
                System.out.println("Queue is empty. Cannot dequeue.");
                return -1; 
            }
            int value = queueArray[front++];
            if (front > rear) {
                front = 0;
                rear = -1;
            }
            return value;
        }
    
        public boolean isEmpty() {
            return rear == -1 || front > rear;
        }
    
        public int size() {
            if (isEmpty()) {
                return 0;
            }
            return rear - front + 1;
        }
    
        public int front() {
            if (isEmpty()) {
                System.out.println("Queue is empty. No front element.");
                return -1; 
            }
            return queueArray[front];
        }
    
        public int rear() {
            if (isEmpty()) {
                System.out.println("Queue is empty. No rear element.");
                return -1; 
            }
            return queueArray[rear];
        }
    }

    static final int INF = Integer.MAX_VALUE; 

    static int edmondsKarpWithSteps(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residualCapacity = new int[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(capacity[i], 0, residualCapacity[i], 0, n);

        int[] parent = new int[n]; 
        int maxFlow = 0;

        int iteration = 1;

        while (true) {
            Arrays.fill(parent, -1);
            parent[source] = source;
             Queue queue = new Queue(n);
            
            queue.puss(source);

            while (!queue.isEmpty()) {
                int u = queue.poll();

                for (int v = 0; v < n; v++) {
                    if (parent[v] == -1 && residualCapacity[u][v] > 0) {
                        parent[v] = u;
                        queue.puss(v);
                    }
                }
            }

            if (parent[sink] == -1)
                break;

            int pathFlow = INF;
            int v = sink;
            while (v != source) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualCapacity[u][v]);
                v = u;
            }

            v = sink;
            while (v != source) {
                int u = parent[v];
                residualCapacity[u][v] -= pathFlow;
                residualCapacity[v][u] += pathFlow;
                v = u;
            }

            maxFlow += pathFlow;
        
            System.out.println("Iteration " + iteration);

             System.out.println("flow in this path is:"+pathFlow);
             
            System.out.println("Augmenting path:");
            v = sink;
            while (v != source) {
                int u = parent[v];
                System.out.print(v+ " <- ");
                v = u;
            }
            System.out.println(source);
            System.out.println("Residual graph:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(residualCapacity[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();

            iteration++;
        }

        return maxFlow;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
         System.out.println("number of vertices");
        int n = scanner.nextInt(); // number of vertices
        System.out.println(" number of edges");
        int m = scanner.nextInt(); // number of edges

        int[][] capacity = new int[n][n];
         System.out.println("enter the vertex and weight between them");
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int c = scanner.nextInt();
            capacity[u][v] += c;
        }
         System.out.println("enter the sourse");
        int source = scanner.nextInt();
        System.out.println("enter the sink");
        int sink = scanner.nextInt();

        int maxFlow = edmondsKarpWithSteps(capacity, source, sink);
        System.out.println("Maximum flow: " + maxFlow);
    }
}