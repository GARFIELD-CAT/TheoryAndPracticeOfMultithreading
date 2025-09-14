package ru.yagunov.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyExchangeRateParserAppApplication {
    public static class TaskService {
        public void startTask(Runnable task) {
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public static class Task implements Runnable{
        @Override
        public void run() {
            Thread currentThread = Thread.currentThread();
            currentThread.setName("ServiceWorker");

            System.out.println(
                "Имя потока: " + currentThread.getName() + ", Id потока: " + currentThread.getId()
            );

            System.out.println("Поток " + currentThread.getName() + " завершил работу!");
        }
    }

	public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeRateParserAppApplication.class, args);
        System.out.println("Главный поток работает!");

        Thread logger = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            currentThread.setName("LoggerThread");

            for (int i = 1; i <= 10; i++) {
                System.out.println(
                    "Имя потока: " + currentThread.getName() + ", Id потока: " + currentThread.getId() + ": " + i
                );

                try {
                    Thread.sleep(1000); // Задержка в 1 секунду
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Поток " + currentThread.getName() + " завершил работу!");
        });

        Runnable task = new Task();

        TaskService service = new TaskService();
        service.startTask(task);
        service.startTask(logger);

        Thread.getAllStackTraces().keySet().forEach(thread -> {
            System.out.println("Active Thread: " + thread.getName());
        });

        System.out.println("Главный поток завершил работу!");
	}
}
