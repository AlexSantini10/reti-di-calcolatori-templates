package tests;

public class TestShutdown2 {
	
	public static void main(String[] args) {
        // Test dello shutdown hook

        System.out.println("Inizio programma");

        // Attendo un input da tastiera per terminare il programma
        try {
            System.in.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown hook eseguito");
            }
        });
    }
	
}
