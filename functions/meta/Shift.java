package functions.meta;

import functions.Function;

public class Shift implements Function {
    private final Function function;
    private double shiftX;
    private double shiftY;

    //геттеры и сеттеры для удобства возможной дальнейшей работы с полями класса
    public double getShiftX() { return this.shiftX; }
    public double getShiftY() { return this.shiftY; }
    public void setShiftX(double shiftX) { this.shiftX = shiftX; }
    public void setShiftY(double shiftY) { this.shiftY = shiftY; }

    public Shift(Function function, double shiftX, double shiftY) {
        if (function == null) throw new IllegalArgumentException();
        this.function = function;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder() {return function.getLeftDomainBorder() + shiftX;}

    public double getRightDomainBorder() {return function.getRightDomainBorder() + shiftX;}

    public double getFunctionValue(double x) {
        //сдвигаем по осям аргумент и результат
        return function.getFunctionValue(x + shiftX) + shiftY;
    }
}
