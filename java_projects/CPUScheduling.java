//import javafx.util.Pair;

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


public class CPUScheduling {
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
        System.out.println("\nNon-preemptive Shortest Job First:");
        printGanttChart(sjfGantt);
       

        // Preemptive Priority
        List<Pair<Integer, Integer>> priorityGantt = preemptivePriority(new ArrayList<>(processes));
       // Pair<Double, Double> priorityAverages = calculateAverages(priorityGantt, processes);
        System.out.println("\nPreemptive Priority:");
        printGanttChart(priorityGantt);
        // System.out.println("Average Waiting Time: " + priorityAverages.getKey());
        // System.out.println("Average Turnaround Time: " + priorityAverages.getValue());


        // Round Robin
       // List<Pair<Integer, Integer>> rrGantt = roundRobin(new ArrayList<>(processes), quantum);
       // System.out.println("\nRound Robin:");
      //  printGanttChart(rrGantt);
       
        scanner.close();
    }

      
    public static List<Pair<Integer, Integer>> nonPreemptiveSJF(List<Process> processes) {
        List<Process> sortedProcesses = new ArrayList<>(processes);
        sortedProcesses.sort(Comparator.comparingInt(process -> process.burstTime));//.thenComparingInt(process -> process.arrivalTime));
    
        int n = sortedProcesses.size();
        int currentTime = 0;
        int waitingTime = 0;
        int turnaroundTime = 0;
        List<Pair<Integer, Integer>> ganttChart = new ArrayList<>();
        boolean[] completed = new boolean[n];
    
        while (true) {
            int shortestJobIndex = -1;
            int shortestBurstTime = Integer.MAX_VALUE;
    
            for (int i = 0; i < n; i++) {
                if (!completed[i] && sortedProcesses.get(i).arrivalTime <= currentTime) {
                    if (sortedProcesses.get(i).burstTime < shortestBurstTime) {
                        shortestJobIndex = i;
                        shortestBurstTime = sortedProcesses.get(i).burstTime;
                    }
                }
            }
    
            if (shortestJobIndex == -1) {
                // No eligible job found, increment the current time.
                currentTime++;
            } else {
                Process shortestJob = sortedProcesses.get(shortestJobIndex);
                ganttChart.add(new Pair<>(shortestJob.pid, currentTime));
                waitingTime += currentTime - shortestJob.arrivalTime;
                currentTime += shortestJob.burstTime;
                turnaroundTime += currentTime - shortestJob.arrivalTime;
                completed[shortestJobIndex] = true;
            }
    
            boolean allCompleted = true;
            for (int i = 0; i < n; i++) {
                if (!completed[i]) {
                    allCompleted = false;
                    break;
                }
            }
    
            if (allCompleted) {
                break;
            }
        }
           System.out.println("\n");
        double avgWaitingTime = (double) waitingTime / n;
        double avgTurnaroundTime = (double) turnaroundTime / n;
    
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    
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
             System.out.println("\n");

        double avgWaitingTime = (double) waitingTime / n;
        double avgTurnaroundTime = (double) turnaroundTime / n;
          System.out.println("Average Waiting Time: " +avgWaitingTime );
         System.out.println("Average Turnaround Time: " +  avgTurnaroundTime);

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

