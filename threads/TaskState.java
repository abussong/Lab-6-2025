package threads;

//класс для управления состоянием между потоками
public class TaskState {
    private volatile boolean dataReady = false;
    private volatile boolean processing = false;
    private volatile int tasksGenerated = 0;
    private volatile int tasksProcessed = 0;

    public boolean isDataReady() {return dataReady;}
    public void setDataReady(boolean dataReady) {this.dataReady = dataReady;}
    public boolean isProcessing() {return processing; }
    public void setProcessing(boolean processing) { this.processing = processing;}
    public int getTasksGenerated() {return tasksGenerated;}
    public void incrementTasksGenerated() {tasksGenerated++;}
    public int getTasksProcessed() {return tasksProcessed;}
    public void incrementTasksProcessed() {tasksProcessed++;}
}