package data;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class RegistrationDb {

	private static DB db = null;
    private static MongoClient mongo = null;
    
    public static void init() {
        try {
            mongo = new MongoClient("localhost", 27017);
        } catch (final UnknownHostException e) {
        	e.printStackTrace();
        }
        db = mongo.getDB("cmpe273Server");
    }
    
    public static void close(){
    	mongo.close();
    }
    
}