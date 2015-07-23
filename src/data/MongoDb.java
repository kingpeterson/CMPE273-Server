package data;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;


public class MongoDb {
	private static DB db = null;
    private static MongoClient mongo = null;
    
//    public static void init() {
//        try {
//            mongo = new MongoClient("localhost", 27017);
//        } catch (final UnknownHostException e) {
//        	e.printStackTrace();
//        }
//        db = mongo.getDB("cmpe273Server");
//    }
    
	public static void init(){        
        try {
            final ServerAddress serverAddress = new ServerAddress("ds045031.mongolab.com", 45031);

            final MongoCredential credential = MongoCredential.createMongoCRCredential("cmpe273team1", 
                    "lwm2m", "cmpe273".toCharArray());

            mongo = new MongoClient(serverAddress, Arrays.asList(credential));

            db = mongo.getDB("lwm2m");
        } catch (final UnknownHostException e) {
        	e.printStackTrace();
        }
	}
    
    public static void close(){
    	mongo.close();
    }
    
    //test database
//    public static void testInsert(){
//    	if (db == null)
//    		init();
//    	String testData = "{\"Manufacturer\": \"I am test data\"}";
//    	try{
//	        DBCollection collection = db.getCollection("test_Peter");
//	    	DBObject object = (DBObject)JSON.parse(testData);
//	    	collection.insert(object);
//		} catch (MongoException e) {
//			e.printStackTrace();
//		}
//    }
 
    
    public static boolean search(String manufacturer, String serialNumber){
    	boolean found = false;
    	if(db == null){
    		init();
    	}
    	try{
	        DBCollection collection = db.getCollection("bootstrap");
	        BasicDBObject b1 = new BasicDBObject();
	        BasicDBObject fields = new BasicDBObject("Manufacturer", manufacturer).append("SerialNumber", serialNumber);
	        DBObject d1 = collection.findOne(b1, fields);
	        if (d1 != null)
	        	found = true;
        
		} catch (MongoException e) {
			e.printStackTrace();
		}
    	
    	return found;
    }
    
    public static boolean searchType(String productType){
    	boolean found = false;
    	if(db == null){
    		init();
    	}
    	try{
	        DBCollection collection = db.getCollection("bootstrap");
	        BasicDBObject b1 = new BasicDBObject();
	        BasicDBObject fields = new BasicDBObject("ProductType", productType);
	        DBObject d1 = collection.findOne(b1, fields);
	        if (d1 != null)
	        	found = true;
	        
		} catch (MongoException e) {
			e.printStackTrace();
		}
    	
    	return found;
    }
    
    public static void insert(String manufacturer, String productType, String objectID, String modelNumber, String serialNumber, String firmwareVersion){
    	String data = "{\"Manufacturer\": \""+manufacturer+"\", \"ProductType\": \""+productType+"\", \"ObjectID\": \""+objectID+"\", "
    			+ "\"ModelNumber\": \""+modelNumber+"\", \"SerialNumber\": \""+serialNumber+"\", "
    			+ "\"FirmwareVersion\": \""+firmwareVersion+"\"}";
    	if(db == null){
    		init();
    	}
    	try{
	        DBCollection collection = db.getCollection("bootstrap");
	    	DBObject object = (DBObject)JSON.parse(data);
	    	collection.insert(object);
		} catch (MongoException e) {
			e.printStackTrace();
		}
    }
    
    public static String findBootstrap(String manufacturer, String serialNumber) throws JSONException{
    	String result = null;
    	int lightWatts = 0;
    	String lightColor = "";
    	String registerURI = "";
    	String updateURI = "";
    	if(db == null){
    		init();
    	}
    	
    	try{
	        DBCollection collection = db.getCollection("bootstrap");
	        BasicDBObject query = new BasicDBObject();
	        List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
	        obj.add(new BasicDBObject("Manufacturer", manufacturer));
	        obj.add(new BasicDBObject("SerialNumber", serialNumber));
	        query.put("$and", obj);
	        DBCursor cursor = collection.find(query);
	        
	        while (cursor.hasNext()){
	        	BasicDBObject object = (BasicDBObject) cursor.next();
	        	lightWatts = object.getInt("lightWatts");
	        	lightColor = object.getString("lightColor");
	        	registerURI = object.getString("registerURI");
	        	updateURI = object.getString("updateURI");
	        }
	    	result = "{\"lightWatts\": \""+lightWatts+"\", \"lightColor\": \""+lightColor+"\", \"registerURI\": \""+registerURI+"\",\"updateURI\": \""+updateURI+"\", \"found\": \"true\"}";
	
	        cursor.close();
		} catch (MongoException e) {
			e.printStackTrace();
		}
        
    	return result;
    }
    
    public static String findRegistration (String manufacturer, String serialNumber){
    	int isRegistered = 0;
    	String timeStamp = "";
    	String result = "";
    	if (db == null)
    		init();
    	
    	try{
	        DBCollection collection = db.getCollection("registration");
	        BasicDBObject query = new BasicDBObject();
	        List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
	        obj.add(new BasicDBObject("Manufacturer", manufacturer));
	        obj.add(new BasicDBObject("SerialNumber", serialNumber));
	        query.put("$and", obj);
	        DBCursor cursor = collection.find(query);
	        while (cursor.hasNext()){
	        	BasicDBObject object = (BasicDBObject) cursor.next();
	        	isRegistered = object.getInt("isRegistered");
	        	timeStamp = object.getString("timeStamp");
	        }
	        result = "{\"isRegistered\": \""+isRegistered+"\", \"timeStamp\": \""+timeStamp+"\"}";
		} catch (MongoException e) {
			e.printStackTrace();
		}
        
        return result;
    }
    
    public static String register(String manufacturer, String serialNumber, String objectID, int status){
    	String myTime ="";
    	if (db == null)
    		init();
	    	
		myTime =  new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				
		if (status == 1){
			try {
				Date d = df.parse(myTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.SECOND, 3600);
				myTime = df.format(cal.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}

	    }
		
		else if (status != 0 && status != 1){
			try {
				Date d = df.parse(myTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.SECOND, status);
				myTime = df.format(cal.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			status = 1;
		}
		
//		String data = "{\"Manufacturer\": \""+manufacturer+"\", \"SerialNumber\": \""+serialNumber+"\", "
//				+ "\"ObjectID\": \""+objectID+"\", \"isRegistered\": \""+status+"\", \"timeStamp\": \""+myTime+"\"}";
//    	try{
//	        DBCollection collection = db.getCollection("registration");
//	    	DBObject object = (DBObject)JSON.parse(data);
//	    	collection.insert(object);
//		} catch (MongoException e) {
//			e.printStackTrace();
//		}

		try{
	        DBCollection collection = db.getCollection("registration");
	        DBObject updateData = new BasicDBObject();
	        updateData.put("$set", new BasicDBObject("isRegistered", status).append("timeStamp", myTime));
	     
	        BasicDBObject query = new BasicDBObject();
	        List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
	        obj.add(new BasicDBObject("Manufacturer", manufacturer));
	        obj.add(new BasicDBObject("SerialNumber", serialNumber));
	        obj.add(new BasicDBObject("ObjectID", objectID));
	        query.put("$and", obj);
	    	collection.update(query, updateData);
		} catch (MongoException e) {
			e.printStackTrace();
		}
    	
		return myTime;
    }
    
    public static int update(String objectID, String newInstance, String newValue){
    	if (db == null)
    		init();
    	
    	WriteResult result = null;
		try{
	        DBCollection collection = db.getCollection("registration");
	        DBObject updateData = new BasicDBObject();
	        if (newValue.equals("-1")){
	        	updateData.put("$unset", new BasicDBObject(newInstance, ""));
	        }
	        else{
	        	updateData.put("$set", new BasicDBObject(newInstance, newValue));
	        }
	        DBObject query = new BasicDBObject("ObjectID", objectID);
	    	result = collection.update(query, updateData);
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return result.getN();
    }

}
