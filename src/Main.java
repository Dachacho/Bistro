public class Main {
    public static void main(String[] args) {
        Bistro b = new Bistro(2);

        Thread g1 = new Thread(()-> b.dine(10));
        Thread g2 = new Thread(()-> b.dine(20));
        Thread g3 = new Thread(()-> b.dine(30));
        Thread g4 = new Thread(()-> b.dine(40));

        g1.start();
        g2.start();
        g3.start();
        g4.start();

        try {
            g1.join();
            g2.join();
            g3.join();
            g4.join();
        }catch(InterruptedException e){
            b.shutdown();
        }
    }
}