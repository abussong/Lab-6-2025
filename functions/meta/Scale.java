package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function function;
    private double scaleX;
    private double scaleY;

    //геттеры и сеттеры для удобства возможной дальнейшей работы с полями класса
    public double getScaleX() { return this.scaleX; }
    public double getScaleY() { return this.scaleY; }
    public void setScaleX(double scaleX) { this.scaleX = scaleX; }
    public void setScaleY(double scaleY) { this.scaleY = scaleY; }

    public Scale(Function function, double scaleX, double scaleY) {
        if (function == null) throw new IllegalArgumentException();
        this.function = function;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getLeftDomainBorder() {
        if (this.scaleX < 0) return function.getRightDomainBorder() * scaleX;
        if (this.scaleX >= 0) return function.getLeftDomainBorder() * scaleX;
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        if (this.scaleX < 0) return function.getLeftDomainBorder() * scaleX;
        if (this.scaleX >= 0) return function.getRightDomainBorder() * scaleX;
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        //масштабируем аргумент и результат
        return function.getFunctionValue(x * scaleX) * scaleY;
    }
}