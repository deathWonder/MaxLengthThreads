import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {


        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(4);
        List<Future<Integer>> futures = new ArrayList<>();

        long startTs = System.currentTimeMillis(); // start time

        for (String text : texts) {
            Future<Integer> task = threadPool.submit(new MyCallable(text));
            futures.add(task);
        }
        int max = 0;
        for (Future<Integer> future : futures) {
            int temp = future.get();
            if (max < temp) {
                max = temp;
            }
        }

        long endTs = System.currentTimeMillis(); // end time

        threadPool.shutdown();
        System.out.println(max);

        System.out.println("Time: " + (endTs - startTs) + "ms");

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