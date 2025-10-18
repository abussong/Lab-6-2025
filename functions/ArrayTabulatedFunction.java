package functions;

import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {

    private FunctionPoint[] points;

    //создаёт объект табулированной функции по заданным левой и правой границе области определения, а также количеству точек для табулирования
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {

        if (leftX>=rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой!");
        if (pointsCount < 2) throw new IllegalArgumentException("Точек должно быть минимум две!");

        this.points = new FunctionPoint[pointsCount];
        //вычисляем шаг между точками и заполняем массив точек
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i * step, 0);
        }

    }

    //создаёт объект табулированной функции по заданным левой и правой границе области определения, а также значениям функции
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {

        if (leftX>=rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой!");
        if (values.length < 2) throw new IllegalArgumentException("Точек должно быть минимум две!");

        this.points = new FunctionPoint[values.length];
        //вычисляем шаг между точками и заполняем массив точек
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            points[i] = new FunctionPoint(leftX + i * step, values[i]);
        }
    }

    //создаёт объект табулированной функции по массиву точек
    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2) throw new IllegalArgumentException("Точек должно быть минимум две!");

        //проверка упорядоченности точек по X
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по значению X!");
            }
        }

        //защитное копирование для обеспечения инкапсуляции
        this.points = new FunctionPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    public ArrayTabulatedFunction() {}

    //возвращает левую границу области определения функции
    public double getLeftDomainBorder(){
        return points[0].getX();
    }

    //возвращает правую границу области определения функции
    public double getRightDomainBorder(){
        return points[points.length-1].getX();
    }

    //вычисляет значение функции в заданной точке x с помощью линейной интерполяции
    //если x вне области определения, то возвращаем Double.NaN (Not-a-Number)
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) return Double.NaN;

        for (int i = 0; i< points.length-1; i++) {
            if (points[i].getX() <= x && x <= points[i + 1].getX()) {
                double x1 = points[i].getX();
                double x2 = points[i + 1].getX();
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }

    //возвращает количество точек табулирования
    public int getPointsCount(){return points.length;}

    //возвращает копию точки по указанному индексу
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > points.length-1) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        return new FunctionPoint(points[index]);
    }

    //устанавливает точку по указанному индексу (с проверкой порядка по X)
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index > points.length-1) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        double newX = point.getX();
        if (index > 0 && newX<=points[index-1].getX()) throw new InappropriateFunctionPointException("Значение X должно быть больше предыдущей точки!");
        if (index < points.length-1 && newX >= points[index + 1].getX()) throw new InappropriateFunctionPointException("Значение X должно быть меньше следующей точки!");
        points[index] = new FunctionPoint(point);
    }

    //возвращает координату X точки по указанному индексу
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > points.length-1) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        return points[index].getX();
    }

    //устанавливает координату X точки по указанному индексу (с проверкой порядка)
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index > points.length-1) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        if (index > 0 && x <= points[index - 1].getX()) throw new InappropriateFunctionPointException("Значение X должно быть больше предыдущей точки!");
        if (index<points.length-1 && x >= points[index + 1].getX()) throw new InappropriateFunctionPointException("Значение X должно быть меньше следующей точки!");
        points[index].setX(x);
    }

    //возвращает координату Y точки по указанному индексу
    public double getPointY(int index)throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > points.length-1) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        return points[index].getY();
    }

    //устанавливает координату Y точки по указанному индексу
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > points.length-1) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        //if (index < points.length && index >= 0)
        points[index].setY(y);
    }

    //удаляет точку по указанному индексу
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index > points.length-1) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        if (points.length-1 < 3) throw new IllegalStateException("Невозможно удалить точку! Точек должно быть минимум две!");
        FunctionPoint[] newPoints = new FunctionPoint[points.length-1];
        System.arraycopy(points, 0, newPoints, 0, index);
        System.arraycopy(points, index+1, newPoints, index, newPoints.length-index);
        points  = newPoints;
    }

    //добавляет новую точку в массив (с сохранением порядка по X)
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        //находим позицию для вставки (точки должны быть отсортированы по X)
        int insertIndex = 0;
        while (insertIndex < points.length && points[insertIndex].getX() < point.getX()) insertIndex++;

        //если такая точка уже существует - ничего не добавляем и выбрасываем сообщение об ошибке
        if (insertIndex < points.length && points[insertIndex].getX() == point.getX()) throw new InappropriateFunctionPointException("Точка со значением X = " + point.getX() + " уже существует!");

        FunctionPoint[] newPoints = new FunctionPoint[points.length+1];
        System.arraycopy(points, 0, newPoints, 0, insertIndex);
        newPoints[insertIndex] = new FunctionPoint(point);
        System.arraycopy(points, insertIndex, newPoints, insertIndex + 1, points.length - insertIndex);
        points = newPoints;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        //сохраняем необходимые данные
        out.writeInt(points.length);
        for (FunctionPoint point : points) {
            out.writeDouble(point.getX());
            out.writeDouble(point.getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        //восстанавливаем данные в том же порядке
        int pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    //возвращает текстовое описание функции
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < points.length; i++) {
            sb.append(points[i].toString());//используем toString точки
            if (i < points.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    //сравнивает текущую функцию с другим объектом
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TabulatedFunction)) return false;
        TabulatedFunction that = (TabulatedFunction) o;
        //быстрая проверка для ArrayTabulatedFunction
        if (o instanceof ArrayTabulatedFunction ) {
            ArrayTabulatedFunction other = (ArrayTabulatedFunction) o;
            if (this.points.length != other.points.length) return false;

            //прямое сравнение массивов точек
            for (int i = 0; i < points.length; i++) {
                if (!this.points[i].equals(other.points[i])) {return false;}
            }
            return true;
        }
        //общая проверка для любой TabulatedFunction

        if (this.getPointsCount() != that.getPointsCount()) return false;

        for (int i = 0; i < this.getPointsCount(); i++) {
            try {
                if (!this.getPoint(i).equals(that.getPoint(i))) {return false;}
            } catch (FunctionPointIndexOutOfBoundsException e) {return false;}
        }
        return true;
    }

    //реализация хэш-кода с использованием XOR
    @Override
    public int hashCode() {
        int hash = getPointsCount();//начинаем с количества точек
        for (FunctionPoint point : this.points) {
            hash ^= point.hashCode();//комбинируем с хэш-кодами точек
        }
        return hash;
    }

    //возвращает копию текущей точки функции
    @Override
    public Object clone() {
        try {return new ArrayTabulatedFunction(this.points);} catch (Exception e) {return null;}
    }
    //создаем новый объект с помощью конструктора массива точек
    //конструктор сам создаст глубокие копии точек
}
