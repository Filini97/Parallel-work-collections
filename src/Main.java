import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> textsForA = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> textsForB = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> textsForC = new ArrayBlockingQueue<>(100);

        // наполняем очареди текстом
        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = generateText("abc", 100_000);
                    textsForA.put(text);
                    textsForB.put(text);
                    textsForC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        // ищем строку с наибольшим кол-вом символов "а"
        Thread countA = new Thread(() -> {
            int maxCountA = 0; //максимальное кол-во повторений
            String currentMaxA = null; // строка с максимальным кол-вом повторений
            for (int i = 0; i < 10_000; i++) { // ограничиваем кол-во повтроений вол-вом генераций строк
                int count = 0; // счётчик
                String target;
                try {
                    target = textsForA.take(); // берем строку из очереди
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; j < target.length(); j++) {
                    if (target.charAt(j) == 'a') { //считаем кол-во букв "а" во взятой строке
                        count++;
                    }
                }
                if (count > maxCountA) { // сравниваем счётчик строки с максимальным найдённым ранее значением
                    maxCountA = count;
                    currentMaxA = target;
                }
            }
            System.out.println("Строка с максимальным вопвторением буквы 'a' выглядит так: " + currentMaxA + "\n" +
                    "Количество повторений 'a' в этой строке равно: " + maxCountA);
        });
        countA.start();


        Thread countB= new Thread(() -> {
            int maxCountB = 0;
            String currentMaxB = null;
            for (int i = 0; i < 10_000; i++) {
                int count = 0;
                String target;
                try {
                    target = textsForB.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; j < target.length(); j++) {
                    if (target.charAt(j) == 'b') {
                        count++;
                    }
                }
                if (count > maxCountB) {
                    maxCountB = count;
                    currentMaxB = target;
                }
            }
            System.out.println("Строка с максимальным вопвторением буквы 'b' выглядит так: " + currentMaxB + "\n" +
                    "Количество повторений 'b' в этой строке равно: " + maxCountB);
        });
        countB.start();


        Thread countC = new Thread(() -> {
            int maxCountC = 0;
            String currentMaxC = null;
            for (int i = 0; i < 10_000; i++) {
                int count = 0;
                String target;
                try {
                    target = textsForC.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; j < target.length(); j++) {
                    if (target.charAt(j) == 'c') {
                        count++;
                    }
                }
                if (count > maxCountC) {
                    maxCountC = count;
                    currentMaxC = target;
                }
            }
            System.out.println("Строка с максимальным вопвторением буквы 'c' выглядит так: " + currentMaxC + "\n" +
                    "Количество повторений 'c' в этой строке равно: " + maxCountC);
        });
        countC.start();
    }



    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}
