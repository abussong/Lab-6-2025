package functions;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    private FunctionNode head;
    private int pointsCount;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;

    public LinkedListTabulatedFunction() {
    }

    //инициализация пустого списка - голова указывает сама на себя
    private void initializeList() {
        head = new FunctionNode(); // голова ссылается сама на себя
        head.setPrev(head);
        head.setNext(head);
        pointsCount = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;
    }

    //создаёт объект табулированной функции по заданным левой и правой границе области определения, а также количеству точек для табулирования
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX>=rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой!");
        if (pointsCount < 2) throw new IllegalArgumentException("Точек должно быть минимум две!");

        this.pointsCount = pointsCount;

        initializeList();
        //вычисляем шаг между точками и заполняем массив точек
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail().setPoint(new FunctionPoint(leftX + i * step, 0));
        }

        this.lastAccessedNode = head.getNext();
        this.lastAccessedIndex = 0;
    }

    //создаёт объект табулированной функции по заданным левой и правой границе области определения, а также значениям функции
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX>=rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой!");
        if (values.length < 2) throw new IllegalArgumentException("Точек должно быть минимум две!");

        this.pointsCount = values.length;

        initializeList();

        //вычисляем шаг между точками и заполняем массив точек
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            addNodeToTail().setPoint(new FunctionPoint(leftX + i * step, values[i]));
        }

        this.lastAccessedNode = head.getNext();
        this.lastAccessedIndex = 0;
    }

    //создаёт объект табулированной функции по массиву точек
    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2) throw new IllegalArgumentException("Точек должно быть минимум две!");

        //проверка упорядоченности точек по X
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по значению X");
            }
        }

        this.pointsCount = points.length;
        initializeList();

        //создаем узлы из массива точек
        for (int i = 0; i < points.length; i++) {
            addNodeToTail().setPoint(new FunctionPoint(points[i]));
        }

        this.lastAccessedNode = head.getNext();
        this.lastAccessedIndex = 0;
    }

    //возвращает левую границу области определения функции
    public double getLeftDomainBorder() {
        if (pointsCount == 0) throw new IllegalStateException("Функция пуста!");
        return head.getNext().getPoint().getX();
    }

    //возвращает правую границу области определения функции
    public double getRightDomainBorder() {
        if (pointsCount == 0) throw new IllegalStateException("Функция пуста!");
        return head.getPrev().getPoint().getX();
    }

    //вычисляет значение функции в заданной точке x с помощью линейной интерполяции
    //если x вне области определения, то возвращаем Double.NaN (Not-a-Number)
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) return Double.NaN;

        // Поиск интервала, содержащего x
        FunctionNode current = head.getNext();
        //реализуем через while, т.к. список циклический
        while (current != head && current.getNext() != head) {
            double x1 = current.getPoint().getX();
            double x2 = current.getNext().getPoint().getX();
            if (x1 <= x && x <= x2) {
                double y1 = current.getPoint().getY();
                double y2 = current.getNext().getPoint().getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.getNext();
        }

        return Double.NaN;
    }

    //возвращает количество точек табулирования
    public int getPointsCount() {return this.pointsCount;}

    //возвращает копию точки по указанному индексу
    public FunctionPoint getPoint(int index) {return new FunctionPoint(getNodeByIndex(index).getPoint());}

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index > pointsCount-1) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        double newX = point.getX();

        //проверка порядка
        if (index > 0 && newX <= getNodeByIndex(index - 1).getPoint().getX()) throw new InappropriateFunctionPointException("Значение X должно быть больше предыдущей точки!");
        if (index < pointsCount - 1 && newX >= getNodeByIndex(index + 1).getPoint().getX()) throw new InappropriateFunctionPointException("Значение X должно быть меньше следующей точки!");

        getNodeByIndex(index).setPoint(new FunctionPoint(point));
    }

    //возвращает координату X точки по указанному индексу
    public double getPointX(int index) {
        return getNodeByIndex(index).getPoint().getX();
    }

    //устанавливает координату X точки по указанному индексу (с проверкой порядка)
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        //проверка порядка
        if (index > 0 && x <= getNodeByIndex(index - 1).getPoint().getX()) throw new InappropriateFunctionPointException("Значение X должно быть больше предыдущей точки!");
        if (index < pointsCount - 1 && x >= getNodeByIndex(index + 1).getPoint().getX()) throw new InappropriateFunctionPointException("Значение X должно быть меньше следующей точки!");

        FunctionPoint oldPoint = node.getPoint();
        node.setPoint(new FunctionPoint(x, oldPoint.getY()));
    }

    //возвращает координату Y точки по указанному индексу
    public double getPointY(int index) {return getNodeByIndex(index).getPoint().getY();}

    //устанавливает координату Y точки по указанному индексу
    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        FunctionPoint oldPoint = node.getPoint();
        node.setPoint(new FunctionPoint(oldPoint.getX(), y));
    }

    //удаляет точку по указанному индексу
    public void deletePoint(int index) {
        if (pointsCount < 3) throw new IllegalStateException("Невозможно удалить точку! Точек должно быть минимум две!");
        deleteNodeByIndex(index);
    }

    //добавляет новую точку в массив (с сохранением порядка по X)
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        //поиск позиции для вставки
        int insertIndex = 0;
        FunctionNode current = head.getNext();

        while (current != head && current.getPoint().getX() < point.getX()) {
            insertIndex++;
            current = current.getNext();
        }

        //проверка на дубликат точки
        if (current != head && current.getPoint().getX() == point.getX()) throw new InappropriateFunctionPointException("Точка со значением X = " + point.getX() + " уже существует!");

        //вставка узла
        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.setPoint(new FunctionPoint(point));
    }

    //функция для добавления узла в конец списка
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode();
        FunctionNode tail = head.getPrev();

        //связываем новый узел и обновляем связи соседей
        newNode.setPrev(tail);
        newNode.setNext(head);
        tail.setNext(newNode);
        head.setPrev(newNode);

        pointsCount++;
        lastAccessedIndex = pointsCount - 1;
        lastAccessedNode = newNode;

        return newNode;
    }

    //функция для получения узла по индексу
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");

        //для оптимимзации начинаем с последнего доступного узла
        FunctionNode current;
        int startIndex;

        if (lastAccessedIndex != -1 && Math.abs(index - lastAccessedIndex) < Math.min(index, pointsCount - index)) {
            current = lastAccessedNode;
            startIndex = lastAccessedIndex;
        } else if (index < pointsCount / 2) {
            current = head.getNext();
            startIndex = 0;
        } else {
            current = head.getPrev();
            startIndex = pointsCount - 1;
        }

        //продвигаемся по списку вперед или назад
        if (index > startIndex) {
            for (int i = startIndex; i < index; i++) {
                current = current.getNext();
            }
        } else if (index < startIndex) {
            for (int i = startIndex; i > index; i--) {
                current = current.getPrev();
            }
        }

        //сохраняем данные для следующего доступа
        lastAccessedNode = current;
        lastAccessedIndex = index;

        return current;
    }

    //функция для добавления узла по указанному индексу
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");
        if (index == pointsCount) {return addNodeToTail();}

        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.getPrev();
        FunctionNode newNode = new FunctionNode();

        //связываем новый узел и обновляем связи соседей
        newNode.setPrev(prevNode);
        newNode.setNext(nextNode);
        prevNode.setNext(newNode);
        nextNode.setPrev(newNode);

        pointsCount++;
        lastAccessedIndex = index;

        return newNode;
    }

    //функция для удаления узла по индексу
    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) throw new FunctionPointIndexOutOfBoundsException("Недопустимое значение индекса!");

        FunctionNode deletedNode = getNodeByIndex(index);
        FunctionNode prevNode = deletedNode.getPrev();
        FunctionNode nextNode = deletedNode.getNext();

        //обновляем связи соседей и изолируем удаляемый узел
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
        deletedNode.setPrev(null);
        deletedNode.setNext(null);

        pointsCount--;
        lastAccessedIndex = index-1;
        //устанавливаем как последний использованный индекс предыдущий элемент

        return deletedNode;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);

        //сохраняем все точки
        FunctionNode current = head.getNext();
        while (current != head) {
            out.writeDouble(current.getPoint().getX());
            out.writeDouble(current.getPoint().getY());
            current = current.getNext();
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int count = in.readInt();

        //инициализируем список
        initializeList();

        //восстанавливаем точки
        for (int i = 0; i < count; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail().setPoint(new FunctionPoint(x, y));
        }

        //инициализируем кэш
        lastAccessedNode = head.getNext();
        lastAccessedIndex = 0;
    }

    //возвращает текстовое описание функции
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        FunctionNode current = head.getNext();
        while (current != head) {
            sb.append(current.getPoint().toString());
            if (current.getNext() != head) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TabulatedFunction)) return false;

        //быстрая проверка для LinkedListTabulatedFunction
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction other = (LinkedListTabulatedFunction) o;
            if (this.pointsCount != other.pointsCount) return false;

            //прямое сравнение узлов списка
            FunctionNode thisCurrent = this.head.getNext();
            FunctionNode otherCurrent = other.head.getNext();

            while (thisCurrent != this.head && otherCurrent != other.head) {
                if (!thisCurrent.getPoint().equals(otherCurrent.getPoint())) {return false;}
                thisCurrent = thisCurrent.getNext();
                otherCurrent = otherCurrent.getNext();
            }
            return true;
        }

        //общая проверка для любой TabulatedFunction
        TabulatedFunction other = (TabulatedFunction) o;
        if (this.getPointsCount() != other.getPointsCount()) return false;

        for (int i = 0; i < this.getPointsCount(); i++) {
            try {
                if (!this.getPoint(i).equals(other.getPoint(i))) {return false;}
            } catch (FunctionPointIndexOutOfBoundsException e) {return false;}
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = pointsCount;//начинаем с количества точек

        FunctionNode current = head.getNext();
        while (current != head) {
            hash ^= current.getPoint().hashCode();//комбинируем с хэш-кодами точек
            current = current.getNext();
        }

        return hash;
    }

    @Override
    public Object clone() {
            //создаем массив точек из текущего списка
            FunctionPoint[] pointsArray = new FunctionPoint[pointsCount];
            FunctionNode current = head.getNext();

            int index = 0;
            while (current != head) {
                pointsArray[index] = current.getPoint();
                current = current.getNext();
                index++;
            }
            //создаем новый объект с помощью конструктора массива точек
            //конструктор сам создаст глубокие копии точек
            return new LinkedListTabulatedFunction(pointsArray);
    }

    //внутренний класс узла списка
    private static class FunctionNode implements Externalizable {
        private FunctionPoint point;
        private FunctionNode next;
        private FunctionNode prev;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }
        public FunctionNode() {
            this.point = null;
            this.prev = this;
            this.next = this;
        }
        public FunctionPoint getPoint(){return this.point;}
        public void setPoint(FunctionPoint point){this.point=point;}

        public FunctionNode getNext() {return next;}
        public void setNext(FunctionNode next) {this.next = next;}
        public FunctionNode getPrev() {return prev;}
        public void setPrev(FunctionNode prev) {this.prev = prev;}

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(point);
            //не сериализуем ссылки next и prev - они будут восстановлены при восстановлении списка
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            point = (FunctionPoint) in.readObject();
            //ссылки next и prev будут установлены при добавлении в список
        }
    }
}




