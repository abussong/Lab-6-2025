package threads;
import functions.Functions;
public class SimpleIntegrator implements Runnable {
    private Task task;
    private TaskState state;

    public SimpleIntegrator(Task task, TaskState state) {
        this.task = task;
        this.state = state;
    }

   @Override
   public void run() {
       try {
           while (state.getTasksProcessed() < task.getTasksCount()) {
               //активное ожидание данных
               if (state.isDataReady() && state.isProcessing()) {
                   //получение и обработка данных
                   double left = task.getLeftX();
                   double right = task.getRightX();
                   double step = task.getDiscretizationStep();

                   double result = Functions.integrate(task.getFunction(), left, right, step);

                   System.out.printf("Result %.4f %.4f %.4f %.6f%n", left, right, step, result);

                   //сбрасываем состояние
                   state.setDataReady(false);
                   state.setProcessing(false);
                   state.incrementTasksProcessed();
               } else {
                   //если данные не готовы - ждём
                   Thread.yield();

               }
           }
       } catch (Exception e) {
           System.out.println("Integration error: " + e.getMessage());
       }
   }
}
