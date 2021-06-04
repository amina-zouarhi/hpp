package threads;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import MainFunction.MainFunction;

/**
 * This class implements Runnable, it main goal is to take a blocking queue and write the data in an  output file
 * @author Team
 *
 */
public class WritingThread implements Runnable {

	PrintWriter writer;
    BlockingQueue<String> blockingQueueWrite;

    public WritingThread(PrintWriter printWriter, BlockingQueue<String> blockingQueueWrite) {
        this.blockingQueueWrite = blockingQueueWrite;
        this.writer = printWriter;
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
        }
        writer.close();
        }

}
