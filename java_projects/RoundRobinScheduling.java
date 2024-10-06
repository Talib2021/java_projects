import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class RoundRobinScheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        System.out.print("Enter the time quantum: ");
        int quantum = scanner.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for process " + i + ": ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter burst time for process " + i + ": ");
            int burstTime = scanner.nextInt();
            processes.add(new Process(i, arrivalTime, burstTime));
        }

        Collections.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        List<Pair<Integer, Integer>> ganttChart = roundRobin(processes, quantum);

        System.out.println("\nRound Robin Gantt Chart:");
        printGanttChart(ganttChart);

        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        int currentTime = 0;
        for (int i = 0; i < n; i++) {
            int pid = processes.get(i).pid;
            int startTime = Math.max(processes.get(i).arrivalTime, currentTime);
            int totalExecutionTime = 0;

            for (Pair<Integer, Integer> entry : ganttChart) {
                if (entry.getKey() == pid) {
                    if (startTime == -1) {
                        startTime = entry.getValue();
                    }
                    totalExecutionTime += entry.getValue() - startTime;
                }
            }

            waitingTime[i] = totalExecutionTime - processes.get(i).burstTime;
            totalWaitingTime += waitingTime[i];
            turnaroundTime[i] = totalExecutionTime;
            totalTurnaroundTime += turnaroundTime[i];
        }

        double avgWaitingTime = totalWaitingTime / n;
        double avgTurnaroundTime = totalTurnaroundTime / n;

        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);

        scanner.close();
    }

    public static List<Pair<Integer, Integer>> roundRobin(List<Process> processes, int quantum) {
        int n = processes.size();
        int currentTime = 0;
        List<Pair<Integer, Integer>> ganttChart = new ArrayList<>();
        Queue<Process> queue = new LinkedList<>();
        int index = 0;

        while (index < n || !queue.isEmpty()) {
            if (!queue.isEmpty()) {
                Process currentProcess = queue.poll();
                int executionTime = Math.min(quantum, currentProcess.remainingTime);

                ganttChart.add(new Pair<>(currentProcess.pid, currentTime));

                currentProcess.remainingTime -= executionTime;
                currentTime += executionTime;

                if (currentProcess.remainingTime > 0) {
                    queue.offer(currentProcess);
                }
            } else {
                // No processes in the queue, find the next process to enqueue
                Process nextProcess = processes.get(index);
                if (nextProcess.arrivalTime <= currentTime) {
                    queue.offer(nextProcess);
                    index++;
                } else {
                    // If no process is available to run, just increment the time
                    ganttChart.add(new Pair<>(-1, currentTime));
                    currentTime++;
                }
            }
        }

        return ganttChart;
    }

    public static void printGanttChart(List<Pair<Integer, Integer>> ganttChart) {
        int prev = -1;
        for (Pair<Integer, Integer> entry : ganttChart) {
            int pid = entry.getKey();
            int time = entry.getValue();
            if (prev != pid) {
                System.out.print("| P" + pid + " | ");
                prev = pid;
            }
            System.out.print(time + " ");
        }
        System.out.println("|");
    }
}

class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
