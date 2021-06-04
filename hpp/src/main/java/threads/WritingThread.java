package threads;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class WritingThread {

	PrintWriter writer;
    BlockingQueue<String> blockingQueueWrite;
    Thread processingThread;
    boolean isProcessing;

    public WritingThread(PrintWriter printWriter, BlockingQueue<String> blockingQueueWrite, boolean isProcessing, Thread processingThread) {
        this.blockingQueueWrite = blockingQueueWrite;
        this.writer = printWriter;
        this.isProcessing = isProcessing;
        this.processingThread = processingThread;
    }

    public void run() {
        while (processingThread.isAlive() || !blockingQueueWrite.isEmpty()) {
            if (!blockingQueueWrite.isEmpty()) {
                try {
                    String sb = blockingQueueWrite.take();
                    writer.write(sb.trim() + "\n"); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }    }

}
