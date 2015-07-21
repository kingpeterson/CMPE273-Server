package data;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.mongodb.MongoException;
import com.mongodb.util.JSON;


public class MongoDb {
	private static DB db = null;
    private static MongoClient mongo = null;
    
//	public static void main(String[] args){
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
 
    
    public static boolean search(String manufacturer, String serialNumber){
    	boolean found = false;
    	if(db == null){
    		init();
    	}
    	try{
	        DBCollection collection = db.getCollection("clients");
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
	        DBCollection collection = db.getCollection("clients");
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
    
    public static void insert(String manufacturer, String productType, String modelNumber, String serialNumber, String firmwareVersion){
    	String data = "{\"Manufacturer\": \""+manufacturer+"\", \"ProductType\": \""+productType+"\", "
    			+ "\"ModelNumber\": \""+modelNumber+"\", \"SerialNumber\": \""+serialNumber+"\", "
    			+ "\"FirmwareVersion\": \""+firmwareVersion+"\"}";
    	if(db == null){
    		init();
    	}
    	try{
	        DBCollection collection = db.getCollection("clients");
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
//    	String deRegisterURI = "";
    	if(db == null){
    		init();
    	}
    	
    	try{
	        DBCollection collection = db.getCollection("clients");
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
//	        	deRegisterURI = object.getString("deRegisterURI");
	        }
	    	result = "{\"lightWatts\": \""+lightWatts+"\", \"lightColor\": \""+lightColor+"\", \"registerURI\": \""+registerURI+"\", \"found\": \"true\"}";
	
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
	        DBCollection collection = db.getCollection("clients");
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
    
    public static String register(String manufacturer, String serialNumber, int status){
    	//boolean success = false;
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
				cal.add(Calendar.SECOND, 10);
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

		try{
	        DBCollection collection = db.getCollection("clients");
	        DBObject updateData = new BasicDBObject();
	        updateData.put("$set", new BasicDBObject("isRegistered", status).append("timeStamp", myTime));
	     
	        BasicDBObject query = new BasicDBObject();
	        List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
	        obj.add(new BasicDBObject("Manufacturer", manufacturer));
	        obj.add(new BasicDBObject("SerialNumber", serialNumber));
	        query.put("$and", obj);
	    	collection.update(query, updateData);
		} catch (MongoException e) {
			e.printStackTrace();
		}
    	
		return myTime;
    }
}
