
import java.util.*;



class CPUScheduling3 {
    public static void sort(int[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }



    public static int maxx(int[] prio, int k, int[] BT_temp, int np) {     // prio means priority
        int a = -6;             // it show  INTMIN
        int pr_idx = -2;
        int em = 0;             // count no. of processes till time = k where k is current time em is no of process that has gon in queue 
        for (int i = 0; i <= k; i++) {
            if (prio[i] != 0) {
                em++;
                if (prio[i] > a && BT_temp[i] != 0) {
                    // for those index in priority array whose BT is not zero yet and have some priority > 0
                    a = prio[i];
                    pr_idx = i;
                }
            }
        }
        if (pr_idx == -2) {
            // if all the processes are executed i.e. BT_temp[i]=0
            if (em == np)
                return -2;
            else
                return -10;                                 // there are processes available after some time of rest
        }
        return pr_idx;                                      // index of the max priority process available is returned here
    }



    public static void print(int[] arr, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + "        ");
        }
        System.out.println();
    }

    public static void printBT(int[] arr) {
        for (int i = 0; i < 50; i++) {
            if (arr[i] != -1 && arr[i] != 0)
                System.out.print(arr[i] + "        "); // print only those values of BT[] which have some non-value burst time value
        }
        System.out.println();
    }

    public static void printpr(int[] arr) {
        int i = 0;
        while (arr[i] != -1) {
            System.out.print("[" + "P"+arr[i] + "]"  +" "  );
            i++;
        }
        System.out.println();
    }



    public static int minn(int[] BT_temp, int k) {
        int a = 999; // it show INT-MAX
        int minBT_temp_idx = -10;
        for (int i = 0; i <= k; i++) {
            if (BT_temp[i] < a && BT_temp[i] != 0 && BT_temp[i] != (-1)) {
                a = BT_temp[i];
                minBT_temp_idx = i;
            }
        }
        return minBT_temp_idx;
    }

    public static void SJF_Sch(int[] BT, int[] AT, int[] CT, int[] Pr, int np) {
        int[] BT_temp = Arrays.copyOf(BT, 50);                              // temp BT used to decrease BT of a process when its any portion is done
        int TimeNow = 0;                                                                // this will keep track of the time in the Gantt chart
        int k = 0;
        for (int i = 0; i < np; i++) {
          // System.out.println("Iteration " + (i + 1));
            int process_AT = minn(BT_temp, k);                                            // process_AT here is the index of the process in BT[] and its AT also
            // To resolve the case that if CPU sits idle for a moment
            if (process_AT == -10) {                                                       // CPU has no process to execute at this time = k
                int s = k;
                while (BT_temp[s] <= 0) {
                    s++;
                }                                                                         // now BT_temp[s] > 0 and s is the arrival Time of the next Process
                process_AT = s;
                TimeNow = s;
            }
            int c = 0;
            for (int j = 0; j <= process_AT; j++) {
                if (BT[j] > 0) {
                    c++;
                }
            }                                                                            // c is the number/name of the process at index process_AT in BT[]
            c = c - 1;                                                                     // counting processes from P0
           // System.out.println("Process no. that has done is P" + c);
            Pr[i] = c;
            CT[c] = TimeNow + BT_temp[process_AT];
            //System.out.println("CT of this Process is " + CT[c]);
            TimeNow = CT[c];
           // System.out.println("Time passed till now " + TimeNow);
            k = CT[c];
            if (k >= 50) {
                k = 49;
            }
            BT_temp[process_AT] = 0;
           // System.out.println();
        }
    }

    public static void Priority_Sch(int[] BT, int[] AT, int[] Priority, int[] CT, int[] Pr, int np) {
        int[] BT_temp = Arrays.copyOf(BT, 50);                                        // temp BT used to decrease BT of a process when its any portion is done
        int TimeNow = 0;                                                                       // this will keep track of the time in the Gantt chart
        int k = 0;
        int count = 0;                                                                         // no. of iterations or no. of blocks in the Gantt array
        // count is from 0 to CT of the last process done increasing sequentially
        while (true) {
            int process_AT = maxx(Priority, k, BT_temp, np);                               // process_AT here is the index of the process in BT[] and Process's AT also
            if (process_AT == -2)                                                  // break out of loop if all the processes are completely executed i.e. BT_temp[i] = 0
                break;

            // To resolve the case that if CPU sits idle for a moment
            if (process_AT == -10) {                                             // CPU has no process to execute at this time = k
                int s = k;
                while (BT_temp[s] <= 0) {
                    s++;
                }                                                               // now BT_temp[s] > 0 and s is the arrival Time of the next Process
                process_AT = s;
                TimeNow = s;
            }
           // System.out.println();
            int c = 0;
            for (int j = 0; j <= process_AT; j++) {
                if (BT[j] > 0) {
                    c++;
                }
            } // c is the number/name of the process at index process_AT in BT[]
            c = c - 1; // counting processes from P0
            // System.out.println("Iteration " + (count + 1));
            // System.out.println("Process no. that has done is P" + c);
            Pr[count] = c; // adding Process c in the Gantt array
            count++;
            BT_temp[process_AT]--;
            TimeNow++;
            if (BT_temp[process_AT] == 0) {
                CT[c] = TimeNow;
            }
          //  System.out.println("Time passed till now " + TimeNow);
            k = TimeNow;
            if (k >= 50) {
                k = 49;
            }
        }
    }

    public static void RoundRobin_Sch(int[] BT, int[] AT, int TQ, int[] CT, int[] Pr, int np) {
        int[] BT_temp = Arrays.copyOf(BT, 50); // temp BT used to decrease BT of a process when its any portion is done
        int TimeNow = 0; // this will keep track of the time in the Gantt chart
        int k = 0;
        Queue<Integer> q = new LinkedList<>();
        int e = 0; // index to push processes in the queue P0, P1, ...(initial pushes of all processes)

        int pr_exe = 0;  // name of the process being popped from the queue and executing
        int pr_idx = 0; // sequential index in Pr array
        int process_AT;
        q.add(0); // pushed P0 in the queue and it will execute
        TimeNow = AT[0];
        e++;

        while (!q.isEmpty()) {
            pr_exe = q.poll();  // front element in the queue (pr_exe) is popped, and it will execute
            Pr[pr_idx] = pr_exe;   // pr is ganttchart

           // System.out.println("Process no. that has done is P" + pr_exe);
            process_AT = AT[pr_exe];

            if (BT_temp[process_AT] >= TQ) {
                TimeNow += TQ;
                BT_temp[process_AT] -= TQ;
            } else {

                TimeNow += BT_temp[process_AT];
                BT_temp[process_AT] = 0;
            }

           // System.out.println("Time passed till now " + TimeNow);

            while (AT[e] <= TimeNow && e < np) {
                q.add(e);
                e++;
            }

            if (BT_temp[process_AT] > 0) {
                q.add(pr_exe);
            }
            if (BT_temp[process_AT] == 0) {
                CT[pr_exe] = TimeNow;
            }
            pr_idx++;

            // To resolve the case that if CPU sits idle for a moment
            if (q.isEmpty() && e < np) {   // not all np processes are started for their execution
                // CPU has no process to execute at this time = TimeNow        
                int s = TimeNow;
                while (BT_temp[s] <= 0) {
                    s++;
                }// now BT_temp[s] > 0, and s is the arrival Time of the next Process

                TimeNow = s;
                q.add(e);
                e++;
            }
        }
    }

    public static void main(String[] args) {
        int np = 6;
        System.out.print("Enter number of processes : ");
         Scanner scanner = new Scanner(System.in);
         np = scanner.nextInt();

        int TQ = 2;
        System.out.print("\nEnter Time Quantum for Round Robin  : ");
         TQ = scanner.nextInt();

        int[] AT = new int[15];
        int[] BT = new int[50];
        int[] Priority = new int[50];

        System.out.println("\nEnter Arrival Time & Priority & Burst Time (BT) of the processes : ");
        for (int i = 0; i < np; i++) {
            AT[i] = scanner.nextInt();
            Priority[AT[i]] = scanner.nextInt();
            BT[AT[i]] = scanner.nextInt();
        }


        sort(AT, np);
        System.out.println("\nPrinting the given input in formatted form : ");
        System.out.println("  Process-id   AT    Priority       BT ");
        for (int i = 0; i < np; i++) {
            System.out.println("     " + i + "           " + AT[i] + "       " + Priority[AT[i]] + "        " + BT[AT[i]]);
        }

        System.out.println();
        int[] CT = new int[15];
        int[] TAT = new int[15];
        int[] WT = new int[15];
        int[] Process_Gantt = new int[100];
        Arrays.fill(Process_Gantt, -1);

        for (int z = 0; z < 3; z++) {
            if (z == 0) {
                System.out.println("\nApplying SJF (Non-Preemptive) Scheduling : ");
                SJF_Sch(BT, AT, CT, Process_Gantt, np);
            }
            if (z == 1) {
                System.out.println("\n\nApplying Priority (Preemptive) Scheduling : ");
                Priority_Sch(BT, AT, Priority, CT, Process_Gantt, np);
            }
            if (z == 2) {
                System.out.println("\n\nApplying Round-Robin Scheduling : ");
                RoundRobin_Sch(BT, AT, TQ, CT, Process_Gantt, np);
            }
            float avgTAT = 0;
            float avgWT = 0;
            for (int i = 0; i < np; i++) {
                TAT[i] = ((CT[i]) - (AT[i]));
                avgTAT += TAT[i];
            }

            for (int i = 0; i < np; i++) {
                WT[i] = ((TAT[i]) - (BT[AT[i]]));
                avgWT += WT[i];
            }
            float avgt = avgTAT / np;
            float avgw = avgWT / np;

            System.out.print("Printing Processes array :  ");
            for (int i = 0; i < np; i++) {
                System.out.print(i + "        ");
            }

            System.out.println("\nPrinting AT array :         ");
            print(AT, np);
            System.out.print("Printing BT array :         ");
            printBT(BT);
            System.out.print("Printing CT array :         ");
            print(CT, np);
            System.out.print("Printing TAT array :        ");
            print(TAT, np);
            System.out.print("Printing WT array :         ");
            print(WT, np);

            System.out.println("Avg TAT is : " + avgt + "   Avg WT is : " + avgw);
            System.out.println("\nPrinting Processes execution Sequence in Gantt Array :  ");
            printpr(Process_Gantt);

            for (int y = 0; y < 100; y++) {
                if (y < 15) {
                    CT[y] = 0;
                }
                Process_Gantt[y] = -1;
            }
        }
    }
}
