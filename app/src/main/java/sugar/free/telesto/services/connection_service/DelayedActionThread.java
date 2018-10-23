package sugar.free.telesto.services.connection_service;

public class DelayedActionThread extends Thread {

    private long duration;
    private Runnable runnable;

    private DelayedActionThread(long duration, Runnable runnable) {
        this.duration = duration;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(duration);
            runnable.run();
        } catch (InterruptedException e) {
        }
    }

    public static DelayedActionThread runDelayed(long duration, Runnable runnable) {
        DelayedActionThread delayedActionThread = new DelayedActionThread(duration, runnable);
        delayedActionThread.start();
        return delayedActionThread;
    }
}
