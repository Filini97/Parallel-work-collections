import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> textsForA = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> textsForB = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> textsForC = new ArrayBlockingQueue<>(100);

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


        Thread countA = new Thread(() -> {
            maxCount('a', textsForA);
        });
        countA.start();


        Thread countB= new Thread(() -> {
            maxCount('b', textsForB);
        });
        countB.start();


        Thread countC = new Thread(() -> {
            maxCount('c', textsForC);
        });
        countC.start();
    }

    public static void maxCount(char letter, BlockingQueue<String> queue) {
        int maxCount = 0;
        String currentMax = null;
        for (int i = 0; i < 10_000; i++) {
            int count = 0;
            String target;
            try {
                target = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int j = 0; j < target.length(); j++) {
                if (target.charAt(j) == letter) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                currentMax = target;
            }
        }
        System.out.println("Строка с максимальным вопвторением буквы " + letter + " выглядит так: " + currentMax + "\n" +
                "Количество повторений " + letter + " в этой строке равно: " + maxCount);
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
