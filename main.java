 public class main {
 public static void main(String[] args) {

        // Creating an instance of JobScheduler
        JobScheduler scheduler = new JobScheduler(); 

        //int quantum = 5; //uncomment this line, and line 15 if you choose to run Round Robin with context switch
        //int contextSwitchTime = 0; //uncomment this line, and line 16 if you choose to run Round Robin without context switch

        scheduler.printJobs(); // Calling printJobs on the instance
        scheduler.fifo(); // Running FIFO algorithm
        scheduler.sjfNonPreemptive(); // Running SJF algorithm
        scheduler.srtf(); // Running SRTF algorithm
        scheduler.highestPriorityFirstNonPreemptive(); // Running Highest Priority algorithm
        //scheduler.roundRobin(quantum, contextSwitchTime); 
        //scheduler.roundRobin(quantum, 0); 
    }
 }