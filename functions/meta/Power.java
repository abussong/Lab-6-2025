package functions.meta;

import functions.Function;

public class Power implements Function {
    private final Function function;
    private double power;

    //геттер и сеттер для удобства работы с классом
    public double getPower() {return power;}
    public void setPower(double power) {this.power = power;}

    public Power (Function function, double power) {
        if (function == null) throw new IllegalArgumentException();
        this.function = function;
        this.power = power;
    }

    public double getLeftDomainBorder() {return function.getLeftDomainBorder();}

    public double getRightDomainBorder() {return function.getRightDomainBorder();}

    public double getFunctionValue(double x) {return Math.pow(function.getFunctionValue(x), power);}
}
