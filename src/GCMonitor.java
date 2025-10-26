import java.util.ArrayList;
import java.util.List;


public class GCMonitor {
    public static void runGcDemo() {
        System.out.println("\n[GCMonitor] Starting GC demo (background).");
        try {
            for (int run = 0; run < 3; run++) {
                List<String> temps = new ArrayList<>();
                for (int i = 0; i < 200_000; i++) {
                    String s = new StringBuilder().append("temp").append(i).toString();
                    temps.add(s);
                }
                // drop reference -> allow GC
                temps = null;
                System.out.println("[GCMonitor] Created many temporary strings, requesting GC...");
                System.gc(); // request GC
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        }
        System.out.println("[GCMonitor] GC demo finished.");
    }
}
