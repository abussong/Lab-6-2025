package functions;

import java.io.*;

public class FunctionPoint implements Externalizable  {

    //координаты точки
    private double x;
    private double y;

    //создаёт объект точки с заданными координатами
    public FunctionPoint(double x, double y){
        this.x=x;
        this.y=y;
    }

    //создаёт объект точки с теми же координатами, что у указанной точки
    public FunctionPoint(FunctionPoint point){
        this.x=point.x;
        this.y=point.y;
    }

    //создаёт точку с координатами (0; 0)
    public FunctionPoint() {
        this.x=0.0;
        this.y=0.0;
    }

    //геттеры и сеттеры
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.readDouble();
        y = in.readDouble();
    }

    //возвращает текстовое описание точки в формате (x; y)
    @Override
    public String toString() {
        return "(" + this.x + "; " + this.y + ")";
    }

    //сравнивает текущую точку с другим объектом
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // один и тот же объект
        if (o == null || !(o instanceof FunctionPoint other)) return false; // разные классы или null

        //сравниваем координаты с учетом точности double
        return Double.compare(this.x, other.x) == 0 && Double.compare(this.y, other.y) == 0;
    }

    //реализация хэш-кода с использованием XOR
    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y);
    }

    //возвращает копию текущей точки
    @Override
    public Object clone() {
        try {return new FunctionPoint(this);} catch (Exception e) {return null;}
    }
}
