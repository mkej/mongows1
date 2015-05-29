package com.avsystem.mongows.rs;

import com.mongodb.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author MKej
 */
public class ReaderMain {
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
                    for (DBObject object : collection.find()
                            .setReadPreference(ReadPreference.secondaryPreferred())
                            .sort(new BasicDBObject("_id", -1))
                            .limit(1)) {
                        System.out.println(object.get("x"));
                    }
                } catch (Exception e) {
                    System.out.println("READ ERROR");
                }
            }
        }, 1000, 1000);
    }
}
