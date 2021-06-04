package threads;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import MainFunction.MainFunction;

public class WritingThread implements Runnable {

	PrintWriter writer;
    BlockingQueue<String> blockingQueueWrite;
    boolean isProcessing;

    public WritingThread(PrintWriter printWriter, BlockingQueue<String> blockingQueueWrite, boolean isProcessing) {
        this.blockingQueueWrite = blockingQueueWrite;
        this.writer = printWriter;
        this.isProcessing = isProcessing;
    }

    public void run() {
        while (MainFunction.processThread.isAlive() || !blockingQueueWrite.isEmpty()) {
            if (!blockingQueueWrite.isEmpty()) {
                try {
                    String sb = blockingQueueWrite.take();
                    writer.write(sb + "\n"); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }    }

}
