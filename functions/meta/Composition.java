package functions.meta;

import functions.Function;

public class Composition implements Function {
    private final Function outerFunction;
    private final Function innerFunction;

    public Composition(Function outerFunction, Function innerFunction) {
        if (outerFunction == null || innerFunction == null) throw new IllegalArgumentException();
        this.outerFunction = outerFunction;
        this.innerFunction = innerFunction;
    }

    public double getLeftDomainBorder() {return innerFunction.getLeftDomainBorder();}

    public double getRightDomainBorder() {return outerFunction.getRightDomainBorder();}

    public double getFunctionValue(double x) {return outerFunction.getFunctionValue(innerFunction.getFunctionValue(x));}
}
