package functions;

import java.io.*;
public class TabulatedFunctions {
    private TabulatedFunctions() {throw new AssertionError("Нельзя создавать экземпляры утилитного класса");}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        //проверяем возможные ошибки в параметрах и корректность границ табулирования
        if (leftX>=rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой!");
        if (pointsCount < 2) throw new IllegalArgumentException("Точек должно быть минимум две!");
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {throw new IllegalArgumentException("Границы табулирования выходят за область определения функции!");}

        //создаем табулированную функцию, используя ArrayTabulatedFunction
        ArrayTabulatedFunction result = new ArrayTabulatedFunction(leftX, rightX, pointsCount);

        //заполняем значениями исходной функции
        for (int i = 0; i < pointsCount; i++) {
            double x = result.getPointX(i);
            double y = function.getFunctionValue(x);
            result.setPointY(i, y);
        }

        return result;
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
            //записываем количество точек
            dos.writeInt(function.getPointsCount());

            //записываем координаты всех точек
            for (int i = 0; i < function.getPointsCount(); i++) {
                dos.writeDouble(function.getPointX(i));
                dos.writeDouble(function.getPointY(i));
            }
        dos.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
            //читаем количество точек
            int pointsCount = dis.readInt();

            //читаем координаты точек
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
            }

            //создаем табулированную функцию (реализация через ArrayTabulatedFunction как в методе табулирования)
            return new ArrayTabulatedFunction(points);

    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter bw = new BufferedWriter(out);
        bw.write(Integer.toString(function.getPointsCount()));
        bw.newLine();

        //записываем координаты всех точек через пробел
        for (int i = 0; i < function.getPointsCount(); i++) {
            bw.write("("+function.getPointX(i) + ", " + function.getPointY(i) + ")");
            bw.newLine();
        }
        bw.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);

            //настройка токенизатора для чтения чисел
            st.resetSyntax();

            st.wordChars('0', '9');
            st.wordChars('.', '.');
            st.wordChars('-', '-');
            st.wordChars('E', 'E');
            st.wordChars('e', 'e');

            st.whitespaceChars(' ', ' ');
            st.whitespaceChars('(','(');
            st.whitespaceChars(')',')');
            st.whitespaceChars(',',',');
            st.whitespaceChars('\t', '\t');
            st.whitespaceChars('\n', '\n');
            st.whitespaceChars('\r', '\r');

            //читаем количество точек
            if (st.nextToken() != StreamTokenizer.TT_WORD) {throw new RuntimeException("Ожидалось количество точек!");}
            int pointsCount = Integer.parseInt(st.sval);

            //читаем координаты точек
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                //читаем X
                if (st.nextToken() != StreamTokenizer.TT_WORD) {throw new RuntimeException("Ожидалась координата X!");}
                double x = Double.parseDouble(st.sval);

                //читаем Y
                if (st.nextToken() != StreamTokenizer.TT_WORD) {throw new RuntimeException("Ожидалась координата Y!");}
                double y = Double.parseDouble(st.sval);

                points[i] = new FunctionPoint(x, y);
            }

            return new ArrayTabulatedFunction(points);
    }
}
