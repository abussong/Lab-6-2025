package threads;
import functions.Function;
public class Task
{
    private Function function;//интегрируемая функция
    private double leftX;//левая граница интегрирования
    private double rightX;//правая граница интегрирования
    private double discretizationStep;//шаг дискретизации
    private int tasksCount;//количество заданий для выполнения

    //конструктор по умолчанию
    public Task() {}

    //устанавливаем количество заданий
    public Task(int tasksCount) {this.tasksCount = tasksCount;}

    public Function getFunction() {return function;}
    public void setFunction(Function function) {this.function = function;}
    public double getLeftX() {return leftX;}
    public void setLeftX(double leftX) {this.leftX = leftX;}
    public double getRightX() {return rightX;}
    public void setRightX(double rightX) {this.rightX = rightX;}
    public double getDiscretizationStep() {return discretizationStep;}
    public void setDiscretizationStep(double discretizationStep) {this.discretizationStep = discretizationStep;}
    public int getTasksCount() {return tasksCount;}
    public void setTasksCount(int tasksCount) {this.tasksCount = tasksCount;}
}
