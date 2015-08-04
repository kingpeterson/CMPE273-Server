package data;

import java.net.UnknownHostException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ManagerDAO {
	private static MongoClient client=null;
	private static JSONObject result;
	
	public static MongoClient Connect(){
		if(client!=null)
			return client;
        try {
            final ServerAddress serverAddress = new ServerAddress("ds045031.mongolab.com", 45031);

            final MongoCredential credential = MongoCredential.createMongoCRCredential("cmpe273team1", 
                    "lwm2m", "cmpe273".toCharArray());

            client = new MongoClient(serverAddress, Arrays.asList(credential));
        } catch (final UnknownHostException e) {
        	e.printStackTrace();
        }
		return client;
	}
	
	public static JSONObject searchBootstrap(String manufacturer, String model){
    	DBCursor cursor = null;
    	JSONObject result = new JSONObject();
		DB db = client.getDB("lwm2m");
    	try{
	        DBCollection collection = db.getCollection("BOOTSTRAP");
	        BasicDBObject b1 = new BasicDBObject();
	        BasicDBObject fields = new BasicDBObject("Manufacturer", manufacturer).append("Model", model);
	        DBObject query = collection.findOne(b1, fields);
	        cursor = collection.find(query);
	        while(cursor.hasNext()){
				DBObject obj = cursor.next();
					JSONObject obj2 = new JSONObject(obj.toString());
					result.put("Resource", obj2.getJSONArray("Resource"));
					result.put("ServiceProvider", obj2.getString("ServiceProvider"));
			}
        
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	return result;
    }
	
	public static String register(String manufacturer, String model, String sn, String clientUri, String registered, String registerTime, String expireTime){
    	WriteResult status = null;
    	String result = "";
		DB db = client.getDB("lwm2m");
		try{
	        DBCollection collection = db.getCollection("REGISTRATION");
	        DBObject updateData = new BasicDBObject();
	        updateData.put("$set", new BasicDBObject("ClientUri", clientUri).append("Registered", registered).append("StartTime", registerTime).append("EndTime", expireTime)); 
	        
	        BasicDBObject query = new BasicDBObject();
	        List<BasicDBObject> queryObj = new ArrayList<BasicDBObject>();
	        queryObj.add(new BasicDBObject("Manufacturer", manufacturer));
	        queryObj.add(new BasicDBObject("Model", model));
	        queryObj.add(new BasicDBObject("SN", sn));
	        query.put("$and", queryObj);
	        status = collection.update(query, updateData);
		} catch (MongoException e) {
			e.printStackTrace();
		}
	    if (status.getN() == 1)
	    	result = "Registration completed, expiration time: "+expireTime+"";
	    else
	    	result = "Registraion failed";
		
	    return result;
	}
	
	public static String search(String manufacturer, String model, String sn, String item){
		String result = "";
		DB db = client.getDB("lwm2m");
		try{
	        DBCollection collection = db.getCollection("REGISTRATION");
	        
	        BasicDBObject query = new BasicDBObject();
	        List<BasicDBObject> queryObj = new ArrayList<BasicDBObject>();
	        queryObj.add(new BasicDBObject("Manufacturer", manufacturer));
	        queryObj.add(new BasicDBObject("Model", model));
	        queryObj.add(new BasicDBObject("SN", sn));
	        query.put("$and", queryObj);
	        DBCursor cursor = collection.find(query);
	        while (cursor.hasNext()){
	        	BasicDBObject object = (BasicDBObject) cursor.next();
	        	result = object.getString(item);
	        }

		} catch (MongoException e) {
			e.printStackTrace();
		}
		return result;
	}


	public static void InsertRecord(JSONObject obj) throws JSONException{
		DB db = client.getDB("Record");
		//String maker = (String)obj.get("Manufacturer");
		DBCollection clnt=db.getCollection("FRIDGE");
		BasicDBObject dbj = (BasicDBObject)JSON.parse(obj.toString());
		clnt.insert(dbj);
	}
	
	public static int CountRecord() throws JSONException{
		DB db = client.getDB("Record");
		//String maker = (String)obj.get("Manufacturer");
		DBCollection clnt=db.getCollection("FRIDGE");
		DBCursor rst = clnt.find(new BasicDBObject());
		return rst.count();
	}
	/*
	public static boolean FindModel(String manufacturer,String model){
		DB db=client.getDB("RegServer1");
		DBCollection clnt = db.getCollection("inventory");
		DBCursor rst = clnt.find(new BasicDBObject().append("Manufacturer", manufacturer).append("Model",model));
		if(rst.count()==1){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean FindSubscriber(JSONObject device) throws JSONException{
		DB db=client.getDB("RegServer1");
		String collection=(String) device.get("Manufacturer");
		
		DBCollection clnt= db.getCollection(collection);
		try{
			DBCursor rst= clnt.find(new BasicDBObject()
										.append("Manufacturer", device.get("Manufacturer"))
										.append("Model",device.get("Model"))
										.append("SN",device.get("SN")));
			if(rst.count()==1) {
				return true;
			}else{
				return false;
			}
		}catch(JSONException ex){
			return false;
		}
	}
	
	public static String UpdateSubscriber(JSONObject device) throws JSONException{
		DB db=client.getDB("RegServer1");
		String collection=(String) device.get("Manufacturer");
		
		DBCollection clnt= db.getCollection(collection);
		try{
			BasicDBObject query = new BasicDBObject()
										.append("Manufacturer", device.get("Manufacturer"))
										.append("Model",device.get("Model"))
										.append("SN",device.get("SN"));
	        BasicDBObject update = new BasicDBObject();
	        update.put("$set", new BasicDBObject("Resources",(BasicDBObject)JSON.parse(device.get("Resources").toString())));
			
	        WriteResult rst= clnt.update(query,update);
	        if(rst.getN()==1)
	        	return "Update Done";
	        else
	        	return "Update aborted";
		}catch(JSONException ex){
			return ex.toString();
		}
	}
	
	public static String DeregisterSubscriber(JSONObject device) throws JSONException{
		DB db=client.getDB("RegServer1");
		String collection=(String) device.get("Manufacturer");
		
		DBCollection clnt= db.getCollection(collection);
		try{
			BasicDBObject query = new BasicDBObject()
										.append("Manufacturer", device.get("Manufacturer"))
										.append("Model",device.get("Model"))
										.append("SN",device.get("SN"));
	        BasicDBObject update = new BasicDBObject();
	        update.put("$set", new BasicDBObject("EndTime",new Date().toString()));
			
	        WriteResult rst= clnt.update(query,update);
	        if(rst.getN()==1)
	        	return "De-register Done";
	        else
	        	return "De-register failed";
		}catch(JSONException ex){
			return ex.toString();
		}
	}
		*/
	
	public static void DisConnect(){
		client.close();
	}
	public static void main(String[] args){
		Connect();
//		System.out.println(search("DAIKIN", "AC", "CMPE273AC001", "ClientUri"));
	}

}
