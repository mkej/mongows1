package com.avsystem.mongows.rs;

import com.mongodb.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author MKej
 */
public class WriterMain {
    public static void main(String[] args) {
        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)
        ));
        DBCollection collection = client.getDB("ws").getCollection("ws");
        new Timer(false).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("WRITE");
                    WriteConcern.
                    collection.insert(new BasicDBObject("x", new Date()));
                } catch (Exception e) {
                    System.out.println("ERROR");
                }
            }
        }, 1000, 1000);
    }
}
