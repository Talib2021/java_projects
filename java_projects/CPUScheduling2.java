import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int priority;
    int remainingTime;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
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

public class CPUScheduling2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for process " + i + ": ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter burst time for process " + i + ": ");
            int burstTime = scanner.nextInt();
            System.out.print("Enter priority for process " + i + ": ");
            int priority = scanner.nextInt();
            processes.add(new Process(i, arrivalTime, burstTime, priority));
        }

        System.out.print("Enter time quantum for Round Robin: ");
        int quantum = scanner.nextInt();

        // Non-preemptive Shortest Job First
        List<Pair<Integer, Integer>> sjfGantt = nonPreemptiveSJF(new ArrayList<>(processes));
        Pair<Double, Double> sjfAverages = calculateAverages(sjfGantt, processes);
        System.out.println("\nNon-preemptive Shortest Job First:");
        printGanttChart(sjfGantt);
        System.out.println("Average Waiting Time: " + sjfAverages.getKey());
        System.out.println("Average Turnaround Time: " + sjfAverages.getValue());

        // Preemptive Priority
        List<Pair<Integer, Integer>> priorityGantt = preemptivePriority(new ArrayList<>(processes));
        Pair<Double, Double> priorityAverages = calculateAverages(priorityGantt, processes);
        System.out.println("\nPreemptive Priority:");
        printGanttChart(priorityGantt);
        System.out.println("Average Waiting Time: " + priorityAverages.getKey());
        System.out.println("Average Turnaround Time: " + priorityAverages.getValue());

        // Round Robin
        List<Pair<Integer, Integer>> rrGantt = roundRobin(new ArrayList<>(processes), quantum);
        Pair<Double, Double> rrAverages = calculateAverages(rrGantt, processes);
        System.out.println("\nRound Robin:");
        printGanttChart(rrGantt);
        System.out.println("Average Waiting Time: " + rrAverages.getKey());
        System.out.println("Average Turnaround Time: " + rrAverages.getValue());

        scanner.close();
    }

    public static List<Pair<Integer, Integer>> nonPreemptiveSJF(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.burstTime));
        int n = processes.size();
        int currentTime = 0;
        int waitingTime = 0;
        int turnaroundTime = 0;
        List<Pair<Integer, Integer>> ganttChart = new ArrayList<>();

        for (Process process : processes) {
            ganttChart.add(new Pair<>(process.pid, currentTime));
            waitingTime += currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            turnaroundTime += currentTime - process.arrivalTime;
        }

        double avgWaitingTime = (double) waitingTime / n;
        double avgTurnaroundTime = (double) turnaroundTime / n;

        return ganttChart;
    }

    public static List<Pair<Integer, Integer>> preemptivePriority(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int n = processes.size();
        int currentTime = 0;
        int waitingTime = 0;
        int turnaroundTime = 0;
        List<Pair<Integer, Integer>> ganttChart = new ArrayList<>();

        while (!processes.isEmpty()) {
            List<Process> eligibleProcesses = new ArrayList<>();
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime) {
                    eligibleProcesses.add(process);
                }
            }

            if (eligibleProcesses.isEmpty()) {
                currentTime++;
                continue;
            }

            eligibleProcesses.sort(Comparator.comparingInt(p -> p.priority));
            Process selectedProcess = eligibleProcesses.get(0);
            ganttChart.add(new Pair<>(selectedProcess.pid, currentTime));
            selectedProcess.remainingTime--;

            if (selectedProcess.remainingTime == 0) {
                processes.remove(selectedProcess);
                waitingTime += currentTime - selectedProcess.arrivalTime - selectedProcess.burstTime;
                turnaroundTime += currentTime - selectedProcess.arrivalTime;
            }

            currentTime++;
        }

        double avgWaitingTime = (double) waitingTime / n;
        double avgTurnaroundTime = (double) turnaroundTime / n;

        return ganttChart;
    }

    public static List<Pair<Integer, Integer>> roundRobin(List<Process> processes, int quantum) {
        int n = processes.size();
        int currentTime = 0;
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        List<Pair<Integer, Integer>> ganttChart = new ArrayList<>();
        Queue<Process> queue = new LinkedList<>();

        for (Process process : processes) {
            queue.offer(process);
        }

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            int startTime = currentTime;
            ganttChart.add(new Pair<>(currentProcess.pid, startTime));
            int remainingTime = currentProcess.remainingTime;

            if (remainingTime <= quantum) {
                currentTime += remainingTime;
                currentProcess.remainingTime = 0;
            } else {
                currentTime += quantum;
                currentProcess.remainingTime -= quantum;
            }

            for (Process process : processes) {
                if (process != currentProcess && process.arrivalTime <= currentTime && process.remainingTime > 0) {
                    queue.offer(process);
                }
            }

            if (currentProcess.remainingTime > 0) {
                queue.offer(currentProcess);
            } else {
                waitingTime[currentProcess.pid] += startTime - currentProcess.arrivalTime;
                turnaroundTime[currentProcess.pid] = currentTime - currentProcess.arrivalTime;
            }
        }

        double avgWaitingTime = Arrays.stream(waitingTime).average().orElse(0);
        double avgTurnaroundTime = Arrays.stream(turnaroundTime).average().orElse(0);

        return ganttChart;
    }

    public static Pair<Double, Double> calculateAverages(List<Pair<Integer, Integer>> ganttChart, List<Process> processes) {
        int n = processes.size();
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];

        for (int i = 0; i < n; i++) {
            int pid = processes.get(i).pid;
            int startTime = -1;

            for (Pair<Integer, Integer> entry : ganttChart) {
                if (entry.getKey() == pid) {
                    if (startTime == -1) {
                        startTime = entry.getValue();
                    }
                    turnaroundTime[i] = entry.getValue() - processes.get(i).arrivalTime + processes.get(i).burstTime;
                }
            }

            waitingTime[i] = turnaroundTime[i] - processes.get(i).burstTime;
        }

        double totalWaitingTime = Arrays.stream(waitingTime).sum();
        double totalTurnaroundTime = Arrays.stream(turnaroundTime).sum();

        double avgWaitingTime = totalWaitingTime / n;
        double avgTurnaroundTime = totalTurnaroundTime / n;

        return new Pair<>(avgWaitingTime, avgTurnaroundTime);
    }

    // public static void printGanttChart(List<Pair<Integer, Integer>> ganttChart) {
    //     int prev = -1;
    //     for (Pair<Integer, Integer> entry : ganttChart) {
    //         int pid = entry.getKey();
    //         int time = entry.getValue();
    //         if (prev != pid) {

    //             System.out.print("| P" + pid + " | ");
    //             prev = pid;
    //         }
    //         System.out.print(time + " ");
    //     }
    //     System.out.println("|");
    // }

    public static void printGanttChart(List<Pair<Integer, Integer>> ganttChart) {
        int prev = -1;
        for (Pair<Integer, Integer> entry : ganttChart) {
            int pid = entry.getKey();
            int time = entry.getValue();
            if (prev != pid) {
                System.out.println("┌─────────┐");
                System.out.println("│  P"+pid+"     │");
                System.out.println("└─────────┘");
                prev = pid;
            }
            System.out.println(  "┌─────────┐");
            System.out.print("│   " + time + "     │");
            System.out.println("\n└─────────┘");
        }
    }
    
}













