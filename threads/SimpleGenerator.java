package threads;
import functions.basic.Log;
public class SimpleGenerator implements Runnable {
    private Task task;
    private TaskState state;

    public SimpleGenerator(Task task, TaskState state) {
        this.task = task;
        this.state = state;
    }

   @Override
   public void run() {
       for (int i = 0; i < task.getTasksCount(); i++) {
           //ждём завершения обработки предыдущего задания
           while (state.isProcessing()) {
               Thread.yield();
           }

           //генерируем случайные параметры
           double base = 1 + Math.random() * 9;
           double left = Math.random() * 100;
           double right = 100 + Math.random() * 100;
           double step = Math.random();

           //устанавливаем параметры в задание
           task.setFunction(new Log(base));
           task.setLeftX(left);
           task.setRightX(right);
           task.setDiscretizationStep(step);

           System.out.printf("Source %.4f %.4f %.4f%n", left, right, step);

           //устанавливаем состояние
           state.setDataReady(true);
           state.setProcessing(true);
           state.incrementTasksGenerated();
       }
   }

}
