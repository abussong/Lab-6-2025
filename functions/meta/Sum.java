package functions.meta;

import functions.Function;

public class Sum implements Function {
    private final Function firstFunction;
    private final Function secondFunction;

    public Sum(Function firstFunction, Function secondFunction) {
        if (firstFunction == null || secondFunction == null) throw new IllegalArgumentException();
        if (secondFunction.getRightDomainBorder()<firstFunction.getLeftDomainBorder() || firstFunction.getRightDomainBorder()< secondFunction.getLeftDomainBorder()) throw new IllegalArgumentException("Области определения функций не пересекаются!");
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
    }

    public double getLeftDomainBorder() {return Math.max(firstFunction.getLeftDomainBorder(), secondFunction.getLeftDomainBorder());}

    public double getRightDomainBorder() {return Math.min(firstFunction.getRightDomainBorder(), secondFunction.getRightDomainBorder());}

    public double getFunctionValue(double x) {
        //проверяем, что x принадлежит пересечению областей определения
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {return Double.NaN;}
        return firstFunction.getFunctionValue(x) + secondFunction.getFunctionValue(x);
    }
}