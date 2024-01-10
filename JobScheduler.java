import java.util.*;

public class JobScheduler {
    //Creating private List type called jobs.
    private List<Job> jobs;

    //Constructor that will initialize jobs list with a new instance of ArrayList
    public JobScheduler(){ 
        jobs = new ArrayList<>();
        //25 jobs will be created (per project instructions)
        generateJobs(25);
    }
    //generateJobs method is a private helper method within the JobScheduler class. Will populate jobs list with Job objects
    private void generateJobs(int numberOfJobs){
        for (int i=0; i< numberOfJobs; i++){
            jobs.add(new Job());
        }
    }

    //FIFO Scheduling Algorithm
    public void fifo(){
        //Sorts list of jobs in ascending order(arrival time in this case)
        Collections.sort(jobs);
        //Initializing current time to 0; keep track of current time in schedule
        int currentTime = 0;
        //How many jobs have been completed
        int completedJobs = 0;
        //Totalturnaroundtime = total time taken from arrival to completion of job
        double totalTurnaroundTime = 0;
        //For each loop that iterates through each element in the jobs collection
        for (Job job: jobs){
            //If statement checks whether currentTime is less than the job's arrival time (job.arrTime). If job has not arrived by the currentTime,
            //the current time is set to the arrival time of the job
            if(currentTime< job.arrTime){
                currentTime = job.arrTime;
            }
            //Exit time of the job = sum of the currentTime and job's CPU burst time. Essentially when the job will be
            //completed after being processed
            job.exitTime = currentTime + job.cpuBurst;
            //Calculating the turnaround time for the job (exit time - arrival time)
            job.turnaroundTime = job.exitTime - job.arrTime;
            //Increment currentTime by the job's CPU burst time, essentially moving the current time
            currentTime += job.cpuBurst; 
            //Adding the job's turnaround time to the total turnaround time variable
            totalTurnaroundTime += job.turnaroundTime;
            //Increment count of completed jobs by 1
            completedJobs++;
        }
        //Calcuating average turnaround time
        double averageTurnaroundTime = totalTurnaroundTime / completedJobs;
        //Calculates the throughout (how many jobs per unit of time)
        double throughput = (double) completedJobs / currentTime;

        System.out.println("FIFO Scheduling:");
        //Using printf to control decimal space usage
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }

    //SJF Scheduling Algorithm
    public void sjfNonPreemptive() {
        //List of jobs is sorted based on arrival times using a comparator. If two jobs arrive
        //at the same time, they are sorted based on CPU burst times (shorter job will come first)
        jobs.sort(Comparator.comparingInt(Job::getArrTime).thenComparingInt(Job::getCpuBurst));
        //Same documentation as FIFO
        int currentTime = 0;
        int completedJobs = 0;
        double totalTurnaroundTime = 0;
        //Creates a new empty list called readyQueue(to hold jobs that are ready to be processed.)
        List<Job> readyQueue = new ArrayList<>();
        //Loop will run until ALL jobs have been processed
        while (completedJobs < jobs.size()) {
            //Loop through all jobs, and add those jobs that have arrived to the ready queue.
            for (Job job : jobs) {
                if (job.getArrTime() <= currentTime && !readyQueue.contains(job)) {
                    readyQueue.add(job);
                }
            }
            //ReadyQueue will be sorted by CPU burst to make sure the job with the shortest
            //CPU burst is processed next
            readyQueue.sort(Comparator.comparingInt(Job::getCpuBurst));
            //If ReadyQueue is not empty, the job with the shortest CPU burst is removed from the 
            //front of the ReadyQeue
            if (!readyQueue.isEmpty()) {
                Job job = readyQueue.remove(0); 
                if (currentTime < job.getArrTime()) {
                    currentTime = job.getArrTime();
                }
                //Exit time is calculated (current time + job's CPU burst.)
                job.setExitTime(currentTime + job.getCpuBurst());
                //Job's turnaroundtime is calculated (exitTime - ArrTime)
                job.setTurnaroundTime(job.getExitTime() - job.getArrTime());
                //currentTime is moved forward by the job's CPU burst time. Total turnaround time is increased
                //by the job's turnaround time, and the number of completed jobs is incremented.
                currentTime += job.getCpuBurst(); 
                totalTurnaroundTime += job.getTurnaroundTime(); 
                completedJobs++;
            } else {
                // If the ready queue is empty, current time is advanced by one unit (to simulate the passage of time)
                currentTime++;
            }
        }
    
        // Calculate and print the average turnaround time and throughput
        double averageTurnaroundTime = totalTurnaroundTime / jobs.size();
        double throughput = (double) jobs.size() / currentTime;
        System.out.println("SJF Non-Preemptive Scheduling:");
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }
    
    //SRTF Scheduling Algorithm
    public void srtf() {
        //Sorts list of jobs in ascending order by arrival time
         jobs.sort(Comparator.comparingInt(job -> job.arrTime));
        //Creating a PriortiyQueue to behave as the ready queue. PriorityQueue will order  jobs
        //based on their remaining time. PriorityQueue will automatically order jobs that have
        //a shorter remaining time.
        PriorityQueue<Job> readyQueue = new PriorityQueue<>(
            Comparator.comparingInt(job -> job.remainingTime)
        );
        //Same as FIFO and SJF
        int currentTime = 0;
        int completedJobs = 0;
        double totalTurnaroundTime = 0;
        Job currentJob = null;
        
        while (completedJobs < jobs.size()) {
            // Add all jobs that have arrived by the current time to the ready queue
            while (!jobs.isEmpty() && jobs.get(0).arrTime <= currentTime) {
                readyQueue.add(jobs.remove(0));
            }


            //Checks if preemption is necessary. If the current job is not null, and theres a job in the readyQueue,
            //with a shorter remaining time, the current job is added back into the readyQueue.
            if (currentJob != null && !readyQueue.isEmpty() &&
                    readyQueue.peek().remainingTime < currentJob.remainingTime) {
                readyQueue.add(currentJob);
                currentJob = null;
            }

            //If no job is available (CPU is idle,) and the ready queue is not empty, the next job with shortest time
            //is taken from the ready queue
            if (currentJob == null && !readyQueue.isEmpty()) {
                currentJob = readyQueue.poll();
            }

            //Increment time, and if there is a current job, the remaining time of the job is decremeneted. If job is compelted,
            //(remainingTime == 0,) exitTime and turnaroundTime are calculated. totalTurnaroundTime and completedJobs are updated.
            if (currentJob != null) {
                currentJob.remainingTime--;
                if (currentJob.remainingTime == 0) {
                    currentJob.exitTime = currentTime + 1;
                    currentJob.turnaroundTime = currentJob.exitTime - currentJob.arrTime;
                    totalTurnaroundTime += currentJob.turnaroundTime;
                    completedJobs++;
                    currentJob = null;
                }
            }
            currentTime++;
        }
        //Average turnaround time and throughput
        double averageTurnaroundTime = totalTurnaroundTime / jobs.size();
        double throughput = (double) jobs.size() / currentTime;
        //Using printf for decimal values
        System.out.println("SRTF Scheduling:");
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }

    //HighestPriority Scheduling Algorithm
    public void highestPriorityFirstNonPreemptive() {
        //Sort jobs by arrival time in ascending order
        jobs.sort(Comparator.comparingInt(job -> job.arrTime));
        //PriorityQueue is created, that orders jobs by priority. 
        //The highest priority uses .reversed() since a min-heap has the value with the smallest value at the head of the queue.
        PriorityQueue<Job> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt((Job job) -> job.priority).reversed()
                        .thenComparingInt(job -> job.arrTime)
        );
        //Same as other scheudling algorithms
        int currentTime = 0;
        int completedJobs = 0;
        double totalTurnaroundTime = 0;
        Job currentJob = null;
        //Very similar to other scheduling algorithms
        while (completedJobs < jobs.size()) {
            // Add all jobs that have arrived by the current time to the priority queue
            while (!jobs.isEmpty() && jobs.get(0).arrTime <= currentTime) {
                priorityQueue.add(jobs.remove(0));
            }

            // Select the highest priority job if the CPU is idle
            if (currentJob == null && !priorityQueue.isEmpty()) {
                currentJob = priorityQueue.poll();
            }

            //Again, similar to SRTF
            if (currentJob != null) {
                // Run the job in one go, as it's non-preemptive (unlike SRTF)
                currentTime += currentJob.cpuBurst;
                currentJob.exitTime = currentTime;
                currentJob.turnaroundTime = currentJob.exitTime - currentJob.arrTime;
                totalTurnaroundTime += currentJob.turnaroundTime;
                completedJobs++;
                currentJob = null; // Job completed
            } else {
                // If no jobs are ready to run, increment the current time
                currentTime++;
            }
        }
            //Same as other algorithms
        double averageTurnaroundTime = totalTurnaroundTime / jobs.size();
        double throughput = (double) jobs.size() / currentTime;
        System.out.println("Highest Priority First Non-Preemptive Scheduling:");
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }

    //roundRobin method takes quantum (maximum time a job gets to run before it has to stop)
    //and contextSwitchTime (time it takes to switch from one running job to another)
    public void roundRobin(int quantum, int contextSwitchTime) {
        //Sorting all jobs in order of arrival time to deal with first to last.
        jobs.sort(Comparator.comparingInt(job -> job.arrTime));
        //Creating queue to keep track of jobs
        Queue<Job> queue = new LinkedList<>();
        //Track the current time
        int currentTime = 0;
        //Track the total time for context switches
        int totalContextSwitchTime = 0;
        //Keep track of the index of the next job to add to the queue
        int nextJobIndex = 0;
        //Store the last job to check if a context switch is needed
        Job lastJob = null;
        //Keep looping as long as there are jobs that havent beeen looked at,
        //or jobs waiting in queue.
        while (nextJobIndex < jobs.size() || !queue.isEmpty()) {
            //As time increases, keep adding jobs to queue
            while (nextJobIndex < jobs.size() && jobs.get(nextJobIndex).arrTime <= currentTime) {
                queue.add(jobs.get(nextJobIndex));
                nextJobIndex++;
            }
            //If job is in queue, work on next on
            if (!queue.isEmpty()) {
                Job job = queue.poll();
                //If we need to take time to switch from one job to another (and we're not switching back to the same job we just did),
                //we add that switch time to the clock and keep track of how much total time we've spent switching.
                if (contextSwitchTime > 0 && lastJob != null && lastJob != job) {
                    currentTime += contextSwitchTime;
                    totalContextSwitchTime += contextSwitchTime;
                }

                //We let the job run for either its remaining time or the quantum, whichever is less.
                //We update the clock and reduce the job's remaining time by that amount.
                int timeSlice = Math.min(job.remainingTime, quantum);
                currentTime += timeSlice;
                job.remainingTime -= timeSlice;
                //If the job isn't finished, it goes back in the queue. If it is finished, we note the time it finished and 
                //calculate how long it took from when it arrived (turnaroundTime)
                if (job.remainingTime > 0) {
                    queue.add(job);
                } else {
                    job.exitTime = currentTime;
                    job.turnaroundTime = job.exitTime - job.arrTime;
                }

                lastJob = job;
                // If no jobs are ready, increment the current time 
            } else {
                currentTime++;
            }
        }

        //After all jobs are done, we calculate the average time each job took (averageTurnaroundTime) and how many
        //jobs we got through per unit of time, not counting the time we spent switching (throughput).
        double averageTurnaroundTime = jobs.stream().mapToDouble(job -> job.turnaroundTime).average().orElse(0);
        double throughput = (double) jobs.size() / (currentTime - totalContextSwitchTime);

        System.out.println("Round Robin Scheduling" + (contextSwitchTime > 0 ? " with Context Switching:" : ":"));
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }

    //Print Jobs method to display the jobs before scheduling
    public void printJobs() { 
        for (Job job : jobs) {
            System.out.println(job);
        }
    }
 
}