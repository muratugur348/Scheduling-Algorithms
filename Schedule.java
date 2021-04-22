import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Task {
    String taskname;
    int arrivalTime, priority,burstTime,timeOfExec,waitedTime,waitedTimeForTurnaround,burstLeft,finishedCpuTime;

    public Task(String taskname, int arrivalTime, int priority, int burstTime) {
        this.taskname = taskname;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.burstTime = burstTime;
        this.timeOfExec= -1;
        this.waitedTime=0;
        this.waitedTimeForTurnaround=0;
        this.burstLeft=this.burstTime;
        this.finishedCpuTime=0;
    }
}

public class Schedule {
    public static int cpuTime = 0;
    public static void main(String[] args) throws IOException {
        ArrayList<Task> tasks= new ArrayList<Task>();
        ArrayList<String> rows = new ArrayList<String>();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[1]));
            while((line = reader.readLine()) != null){
                rows.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i =0;i<rows.size();i++)
        {
            String[] arrOfStr = rows.get(i).split(", ");
            tasks.add(new Task(arrOfStr[0],Integer.parseInt(arrOfStr[1]),Integer.parseInt(arrOfStr[2]),Integer.parseInt(arrOfStr[3])));
        }

        switch (args[0])
        {
            case "fcfs":
                fcfs(tasks);
                break;
            case "sjf":
                sjf(tasks);
                break;
            case "pri":
                pri(tasks);
                break;
            case "rr":
                rr(tasks);
                break;
            case "prirr":
                prirr(tasks);
                break;

        }
    }
    public static void fcfs(ArrayList<Task> tasks) throws IOException {
        tasks = sortByArrivalTime(tasks);
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("output.txt");
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        bw.write("First Come First Serve Scheduling\n");


        for (Task task : tasks) {

            if(!(cpuTime<task.arrivalTime))
            {
                task.timeOfExec =cpuTime;

            }
            else {
                cpuTime+=task.arrivalTime;

            }
            bw.write("Will run Name = " + task.taskname + "\n");
            bw.write("Priority:" + task.priority + "\n");
            bw.write("Burst:" + task.burstTime + "\n");
            bw.write("Task " + task.taskname + " finished" + "\n\n");
            cpuTime+=task.burstTime;
        }
        int waitTime=0, turnAroundTime=0;
        for (Task task : tasks)
        {
            waitTime += task.timeOfExec - task.arrivalTime;
            turnAroundTime += (task.timeOfExec+task.burstTime)-task.arrivalTime;
        }
        bw.write("Average Waiting Time: "+ (float)waitTime/tasks.size() +"\n");
        bw.write("Average Turnaround Time: "+(float)turnAroundTime/tasks.size());



        bw.close();
    }
    public static void sjf(ArrayList<Task> tasks) throws IOException {
        ArrayList<Task> finishedTasks = new ArrayList<Task>();
        tasks = sortByBurst(tasks);
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("output.txt");
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        bw.write("Shortest Job First Serve Scheduling\n");
        while (tasks.size()!=0)
        {
            for(Task task:tasks)
            {
                if(cpuTime>= task.arrivalTime)
                {
                    task.timeOfExec=cpuTime;
                    bw.write("Will run Name = " + task.taskname + "\n");
                    bw.write("Priority:" + task.priority + "\n");
                    bw.write("Burst:" + task.burstTime + "\n");
                    cpuTime+= task.burstTime;
                    bw.write("Task " + task.taskname + " finished" + "\n\n");
                    finishedTasks.add(task);
                    tasks.remove(task);
                    break;
                }
            }
            cpuTime++;
        }
        int waitTime=0, turnAroundTime=0;
        for (Task task : finishedTasks)
        {

            waitTime += task.timeOfExec - task.arrivalTime;
            turnAroundTime += (task.timeOfExec+task.burstTime)-task.arrivalTime;
        }
        bw.write("Average Waiting Time: "+ (float)waitTime/finishedTasks.size() +"\n");
        bw.write("Average Turnaround Time: "+(float)turnAroundTime/finishedTasks.size());
        bw.close();

    }
    public static void pri(ArrayList<Task> tasks) throws IOException {
        tasks= sortByPriority(tasks);
        ArrayList<Task> finishedTasks = new ArrayList<Task>();
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("output.txt");
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        bw.write("Highest Priority Job First Serve Scheduling\n");
        while (tasks.size()!=0)
        {
            for(Task task :tasks)
            {
                if(cpuTime>=task.arrivalTime)
                {
                    bw.write("Will run Name = " + task.taskname + "\n");
                    bw.write("Priority:" + task.priority + "\n");
                    bw.write("Burst:" + task.burstTime + "\n");
                    task.waitedTime=cpuTime-task.arrivalTime;
                    cpuTime+= task.burstTime;
                    task.waitedTimeForTurnaround=cpuTime-task.arrivalTime;
                    bw.write("Task " + task.taskname + " finished" + "\n\n");
                    finishedTasks.add(task);
                    tasks.remove(task);
                    break;
                }
            }
            cpuTime++;
        }
        int waitTime=0, turnAroundTime=0;
        for (Task task : finishedTasks)
        {

            waitTime += task.waitedTime;
            turnAroundTime += task.waitedTimeForTurnaround;
        }
        bw.write("Average Waiting Time: "+(float) waitTime/finishedTasks.size() +"\n");
        bw.write("Average Turnaround Time: "+(float)turnAroundTime/finishedTasks.size());
        bw.close();

    }
    public static void rr(ArrayList<Task> tasks) throws IOException {
        tasks=sortByArrivalTime(tasks);
        ArrayList<Task> finishedTasks = new ArrayList<Task>();
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("output.txt");
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        bw.write("Round Robin CPU Scheduling\n");
        while (tasks.size()!=0) {

            for (int i =0;i< tasks.size();i++) {
                if(cpuTime>=tasks.get(i).arrivalTime)
                {
                    if(tasks.get(i).burstLeft>10)
                    {
                        bw.write("Will run Name = " + tasks.get(i).taskname + "\n");
                        bw.write("Priority:" + tasks.get(i).priority + "\n");
                        tasks.get(i).burstLeft-=10;
                        bw.write("Burst:" + tasks.get(i).burstLeft + "\n\n");
                        cpuTime+=10;
                    }
                    else if(tasks.get(i).burstLeft<=10)
                    {
                        bw.write("Will run Name = " + tasks.get(i).taskname + "\n");
                        bw.write("Priority:" + tasks.get(i).priority + "\n");
                        cpuTime+=tasks.get(i).burstLeft;
                        tasks.get(i).burstLeft=0;
                        tasks.get(i).finishedCpuTime=cpuTime;
                        bw.write("Burst:" + tasks.get(i).burstLeft + "\n");
                        bw.write("Task " + tasks.get(i).taskname + " finished" + "\n\n");
                        finishedTasks.add(tasks.get(i));
                        tasks.remove(tasks.get(i));
                        i--;
                    }

                }
            }
            cpuTime++;
        }
        int waitTime=0, turnAroundTime=0;
        for (Task task : finishedTasks)
        {
            waitTime += task.finishedCpuTime-task.arrivalTime-task.burstTime;
            turnAroundTime += task.finishedCpuTime-task.arrivalTime;
        }
        bw.write("Average Waiting Time: "+(float) waitTime/ finishedTasks.size() +"\n");
        bw.write("Average Turnaround Time: "+(float)turnAroundTime/finishedTasks.size());
        bw.close();
    }
    public static void prirr(ArrayList<Task> tasks) throws IOException {
        tasks= sortByPriority(tasks);
        int taskSize = tasks.size();
        ArrayList<Task> finishedTasks = new ArrayList<Task>();
        ArrayList<Task> readyTasks = new ArrayList<Task>();
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("output.txt");
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        bw.write("Priority with Round Robin Scheduling\n");
        while (tasks.size()!=0) {
            loop: for (int i =0;i< tasks.size();i++) {
                if(cpuTime>=tasks.get(i).arrivalTime)
                {
                    Task temp = tasks.get(i);
                    readyTasks.add(temp);
                    for(int j =0;j<tasks.size();j++)
                    {
                         if (tasks.size()==1)
                          {
                        tasks.remove(tasks.get(i));
                        break;
                         }
                        else if(tasks.get(j)==temp)
                        {

                        }
                        else if(tasks.get(j).priority==temp.priority && tasks.get(j).arrivalTime<=cpuTime)
                        {
                            readyTasks.add(tasks.get(j));
                            tasks.remove(tasks.get(j));
                            j--;
                        }

                        else{

                            tasks.remove(tasks.get(i));

                            break;
                        }
                    }
                    while(readyTasks.size()!=0)
                    {

                        if(readyTasks.size()==1)
                        {
                            readyTasks.get(0).timeOfExec=cpuTime;
                            bw.write("Will run Name = " + readyTasks.get(0).taskname + "\n");
                            bw.write("Priority:" + readyTasks.get(0).priority + "\n");
                            bw.write("Total Burst:" + readyTasks.get(0).burstTime + "\n");
                            bw.write("Burst Left: 0 \n");
                            cpuTime+= readyTasks.get(0).burstLeft;
                            readyTasks.get(0).finishedCpuTime=cpuTime;
                            bw.write("Task " + readyTasks.get(0).taskname + " finished" + "\n\n");
                            finishedTasks.add(readyTasks.get(0));
                            readyTasks.remove(readyTasks.get(0));
                            break loop;

                        }
                        else if(readyTasks.size()>1)
                        {
                            for (int j =0;j< readyTasks.size();j++) {
                                    if(readyTasks.get(j).burstLeft>10)
                                    {
                                        bw.write("Will run Name = " + readyTasks.get(j).taskname + "\n");
                                        bw.write("Priority:" + readyTasks.get(j).priority + "\n");
                                        readyTasks.get(j).burstLeft-=10;
                                        bw.write("Total Burst:" + readyTasks.get(j).burstTime + "\n");
                                        bw.write("Burst Left:" + readyTasks.get(j).burstLeft + "\n\n");
                                        cpuTime+=10;

                                    }
                                    else if(readyTasks.get(j).burstLeft<=10)
                                    {

                                        bw.write("Will run Name = " + readyTasks.get(j).taskname + "\n");
                                        bw.write("Priority:" + readyTasks.get(j).priority + "\n");
                                        cpuTime+=readyTasks.get(j).burstLeft;
                                        readyTasks.get(j).burstLeft=0;
                                        readyTasks.get(j).finishedCpuTime=cpuTime;
                                        bw.write("Total Burst:" + readyTasks.get(j).burstTime + "\n");
                                        bw.write("Burst Left:" + readyTasks.get(j).burstLeft + "\n");
                                        bw.write("Task " + readyTasks.get(j).taskname + " finished" + "\n\n");
                                        finishedTasks.add(readyTasks.get(j));
                                        readyTasks.remove(readyTasks.get(j));
                                        j--;
                                    }


                            }


                        }
                    }


                }
            }
            boolean arrivelIsBigger=false;
            for(int i = 0;i<tasks.size();i++)
            {
                if(tasks.get(i).arrivalTime<=cpuTime)
                {
                    arrivelIsBigger=true;
                }
            }
            if(!arrivelIsBigger)
            cpuTime++;
        }
        int waitTime=0, turnAroundTime=0;
        for (Task task : finishedTasks)
        {
            waitTime += task.finishedCpuTime-task.arrivalTime-task.burstTime;
            turnAroundTime += task.finishedCpuTime-task.arrivalTime;
        }
        bw.write("Average Waiting Time: "+(float) waitTime/taskSize +"\n");
        bw.write("Average Turnaround Time: "+(float)turnAroundTime/taskSize);
        bw.close();
    }
    public  static ArrayList<Task> sortByArrivalTime(ArrayList<Task> tasks)
    {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task z1, Task z2) {
                if (z1.arrivalTime > z2.arrivalTime)
                    return 1;
                if (z1.arrivalTime < z2.arrivalTime)
                    return -1;
                return 0;
            }
        });
        return tasks;
    }
    public  static ArrayList<Task> sortByBurst(ArrayList<Task> tasks)
    {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task z1, Task z2) {
                if (z1.burstTime > z2.burstTime)
                    return 1;
                if (z1.burstTime< z2.burstTime)
                    return -1;
                return 0;
            }
        });
        return tasks;
    }
    public  static ArrayList<Task> sortByPriority(ArrayList<Task> tasks)
    {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task z1, Task z2) {
                if (z1.priority < z2.priority)
                    return 1;
                if (z1.priority > z2.priority)
                    return -1;
                return 0;
            }
        });
        return tasks;
    }
}
