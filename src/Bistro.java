public class Bistro {
    private int seats;
    private Thread order;
    private Thread meal;
    private final Thread waiter;
    public Bistro(int n){
        this.seats = n;
        waiter = new Thread(new Runnable() {
            @Override
            public void run() {
                serve();
            }
        });
        waiter.start();
    }

    public synchronized void dine(int price) {
        while(seats <= 0){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        seats--;

        while(order != null){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        order = Thread.currentThread();
        System.out.println("Guest " + Thread.currentThread().getName() + " orders for " + price + " Lari");

        notifyAll();

        while(meal == null){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        meal = null;
        System.out.println("Guest " + Thread.currentThread().getName() + " eats...");
        seats++;

        notifyAll();
    }
    public synchronized void serve(){
        while(true) {
            try {
                while (order == null) {
                    wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Thread recordedOrder = order;
            order = null;

            notifyAll();

            try {
                while (meal != null) {
                    wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            meal = recordedOrder;
            System.out.println("Enjoy!");

            notifyAll();
        }
    }

    public void shutdown(){
        if(order != null)
            order.interrupt();
        waiter.interrupt();
    }
}
