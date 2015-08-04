package webResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.DBObject;

import data.ManagerDAO;
//import data.MongoDb;


//import data.DataPreservation;

@Path("/BootstrapServer")
public class BootstrapServer {
	@POST
	@Path("/postClientInfo")
	@Produces(MediaType.APPLICATION_JSON)
	//input Json String with manufacturer and model
	public JSONObject postInfo(String input){
		JSONObject result = new JSONObject();
		try{
			ManagerDAO.Connect();
			JSONObject obj = new JSONObject(input);
			String manufacturer = obj.getString("Manufacturer");
			String model = obj.getString("Model");
			result = ManagerDAO.searchBootstrap(manufacturer, model);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		//MongoDb.close();

		return result;
	}
}
