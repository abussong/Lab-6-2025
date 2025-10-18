package functions;
import functions.meta.*;
public class Functions {
    private Functions() {throw new AssertionError("Нельзя создавать экземпляры утилитного класса");}
    public static Function shift(Function f, double shiftX, double shiftY) {return new Shift(f, shiftX, shiftY);}
    public static Function scale(Function f, double scaleX, double scaleY) {return new Scale(f, scaleX, scaleY);}
    public static Function power(Function f, double power) {return new Power(f, power);}
    public static Function sum(Function f1, Function f2) {return new Sum(f1, f2);}
    public static Function mult(Function f1, Function f2) {return new Mult(f1, f2);}
    public static Function composition(Function f1, Function f2) {return new Composition(f1, f2);}

    public static double integrate(Function f, double leftX, double rightX, double step){
        if (leftX>=rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой!");
        if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder()) {throw new IllegalArgumentException();}
        if (step <= 0) {throw new IllegalArgumentException("Шаг дискретизации должен быть положительным!");}
        double integral = 0.0;
        double currentX = leftX;

        //проходим по всем участкам
        while (currentX < rightX) {
            double nextX = Math.min(currentX + step, rightX);

            //вычисляем значения функции на границах участка
            double fCurrent = f.getFunctionValue(currentX);
            double fNext = f.getFunctionValue(nextX);

            //добавляем площадь трапеции для текущего участка
            integral += (fCurrent + fNext) * (nextX - currentX) / 2.0;

            currentX = nextX;
        }
        return integral;
    }
}