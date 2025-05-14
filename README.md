# CPU Scheduling Algorithms Simulation

This Java program simulates various CPU scheduling algorithms to analyze their performance. The program implements the following scheduling algorithms:

- **First-In-First-Out (FIFO)**
- **Shortest Job First (SJF) - Non-Preemptive**
- **Shortest Remaining Job First (SRJF) - Preemptive**
- **Highest Priority Algorithm**
- **Round-Robin (RR) with and without Context Switch**

## Features

### Scheduling Algorithms

1. **First-In-First-Out (FIFO)**
    - Jobs are executed in the order they arrive.
    
2. **Shortest Job First (SJF)**
    - Non-preemptive algorithm where the job with the shortest execution time is selected next.
    
3. **Shortest Remaining Job First (SRJF)**
    - Preemptive algorithm where the job with the shortest remaining execution time is selected next.
    
4. **Highest Priority Algorithm**
    - Jobs are selected based on priority, with the highest priority job executed first.
    
5. **Round-Robin (RR)**
    - Jobs are executed in a cyclic order, each receiving a fixed time slice. The implementation includes versions with and without context switching overhead.

### Performance Analysis

The program analyzes each algorithm by collecting key performance metrics:

- **Turnaround Time**: The total time taken from job arrival to completion.
- **Throughput**: The number of jobs completed within a fixed length of time.

### Output

- **Generated Jobs**: 25 jobs are randomly generated and stored as job objects.
- **Simulation Results**: The program simulates each scheduling algorithm and displays the results.
- **Results Table**: A table is generated showing the values for each algorithm, including average turnaround time and throughput.

## Program Workflow

1. **Job Generation**: 
    - 25 jobs are randomly generated, each represented as a job object with attributes such as arrival time, burst time, and priority.
    
2. **Algorithm Simulation**:
    - Each scheduling algorithm is simulated with the generated jobs.
    - Turnaround time and throughput are collected for analysis.

3. **Results Display**:
    - A table is displayed showing the performance metrics for each algorithm.
    - The average turnaround time for each algorithm is calculated and displayed.