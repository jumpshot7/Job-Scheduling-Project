import java.util.*;

public class JobScheduler {
    private List<Job> jobs;

    public JobScheduler(){ 
        jobs = new ArrayList<>();
        generateJobs(25);
    }

    private void generateJobs(int numberOfJobs){
        for (int i=0; i< numberOfJobs; i++){
            jobs.add(new Job());
        }
    }

    public void fifo(){
        Collections.sort(jobs);
        int currentTime = 0;
        int completedJobs = 0;
        double totalTurnaroundTime = 0;

        for (Job job: jobs){
            if(currentTime< job.arrTime){
                currentTime = job.arrTime;
            }
            job.exitTime = currentTime + job.cpuBurst;
            job.turnaroundTime = job.exitTime - job.arrTime;
            currentTime += job.cpuBurst; 
            totalTurnaroundTime += job.turnaroundTime;
            completedJobs++;
        }

        double averageTurnaroundTime = totalTurnaroundTime / completedJobs;
        double throughput = (double) completedJobs / currentTime;

        System.out.println("FIFO Scheduling:");
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }

    public void sjfNonPreemptive() {
        jobs.sort(Comparator.comparingInt(Job::getArrTime).thenComparingInt(Job::getCpuBurst));
        int currentTime = 0;
        int completedJobs = 0;
        double totalTurnaroundTime = 0;
        List<Job> readyQueue = new ArrayList<>();

        while (completedJobs < jobs.size()) {
            for (Job job : jobs) {
                if (job.getArrTime() <= currentTime && !readyQueue.contains(job)) {
                    readyQueue.add(job);
                }
            }
            readyQueue.sort(Comparator.comparingInt(Job::getCpuBurst));
            if (!readyQueue.isEmpty()) {
                Job job = readyQueue.remove(0); 
                if (currentTime < job.getArrTime()) {
                    currentTime = job.getArrTime();
                }
                job.setExitTime(currentTime + job.getCpuBurst());
                job.setTurnaroundTime(job.getExitTime() - job.getArrTime());
                currentTime += job.getCpuBurst(); 
                totalTurnaroundTime += job.getTurnaroundTime(); 
                completedJobs++;
            } else {
                currentTime++;
            }
        }
    
        double averageTurnaroundTime = totalTurnaroundTime / jobs.size();
        double throughput = (double) jobs.size() / currentTime;
        System.out.println("SJF Non-Preemptive Scheduling:");
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }
    
    public void srtf() {
        jobs.sort(Comparator.comparingInt(job -> job.arrTime));
        PriorityQueue<Job> readyQueue = new PriorityQueue<>(
            Comparator.comparingInt(job -> job.remainingTime)
        );
        int currentTime = 0;
        int completedJobs = 0;
        double totalTurnaroundTime = 0;
        Job currentJob = null;
        
        while (completedJobs < jobs.size()) {
            while (!jobs.isEmpty() && jobs.get(0).arrTime <= currentTime) {
                readyQueue.add(jobs.remove(0));
            }

            if (currentJob != null && !readyQueue.isEmpty() &&
                    readyQueue.peek().remainingTime < currentJob.remainingTime) {
                readyQueue.add(currentJob);
                currentJob = null;
            }

            if (currentJob == null && !readyQueue.isEmpty()) {
                currentJob = readyQueue.poll();
            }

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

        double averageTurnaroundTime = totalTurnaroundTime / jobs.size();
        double throughput = (double) jobs.size() / currentTime;
        System.out.println("SRTF Scheduling:");
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }

    public void highestPriorityFirstNonPreemptive() {
        jobs.sort(Comparator.comparingInt(job -> job.arrTime));
        PriorityQueue<Job> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt((Job job) -> job.priority).reversed()
                        .thenComparingInt(job -> job.arrTime)
        );
        int currentTime = 0;
        int completedJobs = 0;
        double totalTurnaroundTime = 0;
        Job currentJob = null;
        while (completedJobs < jobs.size()) {
            while (!jobs.isEmpty() && jobs.get(0).arrTime <= currentTime) {
                priorityQueue.add(jobs.remove(0));
            }

            if (currentJob == null && !priorityQueue.isEmpty()) {
                currentJob = priorityQueue.poll();
            }

            if (currentJob != null) {
                currentTime += currentJob.cpuBurst;
                currentJob.exitTime = currentTime;
                currentJob.turnaroundTime = currentJob.exitTime - currentJob.arrTime;
                totalTurnaroundTime += currentJob.turnaroundTime;
                completedJobs++;
                currentJob = null;
            } else {
                currentTime++;
            }
        }
        double averageTurnaroundTime = totalTurnaroundTime / jobs.size();
        double throughput = (double) jobs.size() / currentTime;
        System.out.println("Highest Priority First Non-Preemptive Scheduling:");
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }

    public void roundRobin(int quantum, int contextSwitchTime) {
        jobs.sort(Comparator.comparingInt(job -> job.arrTime));
        Queue<Job> queue = new LinkedList<>();
        int currentTime = 0;
        int totalContextSwitchTime = 0;
        int nextJobIndex = 0;
        Job lastJob = null;
        while (nextJobIndex < jobs.size() || !queue.isEmpty()) {
            while (nextJobIndex < jobs.size() && jobs.get(nextJobIndex).arrTime <= currentTime) {
                queue.add(jobs.get(nextJobIndex));
                nextJobIndex++;
            }
            if (!queue.isEmpty()) {
                Job job = queue.poll();
                if (contextSwitchTime > 0 && lastJob != null && lastJob != job) {
                    currentTime += contextSwitchTime;
                    totalContextSwitchTime += contextSwitchTime;
                }

                int timeSlice = Math.min(job.remainingTime, quantum);
                currentTime += timeSlice;
                job.remainingTime -= timeSlice;
                if (job.remainingTime > 0) {
                    queue.add(job);
                } else {
                    job.exitTime = currentTime;
                    job.turnaroundTime = job.exitTime - job.arrTime;
                }

                lastJob = job;
            } else {
                currentTime++;
            }
        }

        double averageTurnaroundTime = jobs.stream().mapToDouble(job -> job.turnaroundTime).average().orElse(0);
        double throughput = (double) jobs.size() / (currentTime - totalContextSwitchTime);

        System.out.println("Round Robin Scheduling" + (contextSwitchTime > 0 ? " with Context Switching:" : ":"));
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Throughput: %.2f jobs/unit time\n", throughput);
    }

    public void printJobs() { 
        for (Job job : jobs) {
            System.out.println(job);
        }
    }
 
}