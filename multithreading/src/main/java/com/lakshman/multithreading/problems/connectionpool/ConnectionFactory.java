package com.lakshman.multithreading.problems.connectionpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Problem Design Connection Pool
 * asked in PhenomPeople round 2
 */
public class ConnectionFactory {
    private static BlockingQueue<Connection> blockingQueue;

    private static final int CAPACITY = 50;

    static {
        blockingQueue = new ArrayBlockingQueue(CAPACITY);

        for(int i = 0; i < 50 ;i++ ){
            try {
                blockingQueue.put(new Connection());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() throws InterruptedException {
        return blockingQueue.take();
    }

    public boolean returnConnection(Connection connection){
        try {
            blockingQueue.put(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

/**
 * can extend Oracle Db connection and override close method to call returnConnection()
 */
class Connection {

}
