package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base;
    public Log(double base) {this.base = base;}

    //геттер и сеттер для удобства работы с классом
    public double getBase() {return this.base;}
    public void setBase(double base){ this.base = base;}

    public double getLeftDomainBorder() {return 0;}

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x <= 0) return Double.NaN;
        return Math.log(x) / Math.log(base);
    }
}