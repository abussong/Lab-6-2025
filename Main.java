import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;
import java.util.concurrent.Semaphore;

import threads.*;

public class Main {
    public static void main(String[] args) {

        //создаем экспоненциальную функцию
        Exp expFunction = new Exp();

        //теоретическое значение интеграла exp(x) от 0 до 1 равно (e - 1) ≈ 1.718281828459045
        double theoreticalValue = Math.E - 1;
        System.out.println("Теоретическое значение интеграла: "+ (Math.E - 1));

        //подбираем шаг дискретизации для точности до 7 знака
        double[] steps = {1.0, 0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001};
        System.out.println();
        System.out.println("----------Поиск подходящего шага дискретизации----------");

        for (double step : steps) {
            double calculatedValue = Functions.integrate(expFunction, 0, 1, step);
            double error = Math.abs(calculatedValue - theoreticalValue);

            System.out.printf("Шаг: %.8f | Вычислено: %.15f | Погрешность: %.10f%n", step, calculatedValue, error);

            //проверяем, достигнута ли требуемая точность
            if (error < 1e-6) {
                System.out.printf("Требуемая точность достигнута при шаге: "+ step);
                break;
            }
        }

        System.out.println();
        System.out.println();
        nonThread();
        simpleThreads();
        complicatedThreads();
    }

    public static void nonThread() {
        System.out.println("--------------Последовательная версия--------------");

        //создаём объект задания(?) и устанавливаем количество заданий
        Task task = new Task();
        task.setTasksCount(120);

        for (int i = 0; i < task.getTasksCount(); i++) {
            //генерируем случайные параметры
            double base = 1 + Math.random() * 9;//основание логарифма от 1 до 10
            double left = Math.random() * 100;//левая граница от 0 до 100
            double right = 100 + Math.random() * 100;//правая граница от 100 до 200
            double step = Math.random();//шаг от 0 до 1

            //создаём функцию логарифма с заданным основанием и выводим изначальные параметры
            Function logFunction = new Log(base);
            System.out.printf("Source %.4f %.4f %.4f%n", left, right, step);

            //вычисляем интеграл и выводим изначальные параметры с результатом
            try {
                double result = Functions.integrate(logFunction, left, right, step);
                System.out.printf("Result %.4f %.4f %.4f %.6f%n", left, right, step, result);
            } catch (Exception e) {System.out.println("Error: " + e.getMessage());}
        }
        System.out.println();
    }

    public static void simpleThreads() {
        System.out.println("-----------Простая многопоточная версия-----------");

        //создаём общие объекты (задания и состояния) и устанавливаем количество заданий
        Task task = new Task(110);
        TaskState state = new TaskState();

        //создаём потоки с общим состоянием
        Thread generator = new Thread(new SimpleGenerator(task, state));
        Thread integrator = new Thread(new SimpleIntegrator(task, state));

        //запускаем потоки
        generator.start();
        integrator.start();

        try {
            //ждём завершения потоков
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {System.out.println("Главный поток был прерван!");}
        System.out.println();
    }

    public static void complicatedThreads() {
        System.out.println("----------Усложнённая многопоточная версия----------");

        //создаём общие объекты (задания и семафора) и устанавливаем количество заданий
        Task task = new Task(130);
        Semaphore semaphore = new Semaphore(1, true);

        //создаем потоки
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);

        //запускаем потоки
        generator.start();
        integrator.start();

        try {
            Thread.sleep(50);

            //прерываем потоки
            generator.interrupt();
            integrator.interrupt();

            //ждем завершения потоков
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {System.out.println("Главный поток был прерван!");}
        System.out.println();
    }
}