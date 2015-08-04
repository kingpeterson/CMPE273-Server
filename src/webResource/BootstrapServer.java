package webResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.DBObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import data.ManagerDAO;
//import data.MongoDb;


//import data.DataPreservation;

@Path("/BootstrapServer")
public class BootstrapServer {
	@POST
	@Path("/postClientInfo")
	@Produces(MediaType.APPLICATION_JSON)
	//input Json String with manufacturer and model
	public Response postInfo(String input){
		JSONObject result = new JSONObject();
		String response = "";

		try{
			ManagerDAO.Connect();
			JSONObject obj = new JSONObject(input);
			String manufacturer = obj.getString("Manufacturer");
			String model = obj.getString("Model");
			result = ManagerDAO.searchBootstrap(manufacturer, model);
			
			Client client = Client.create();
			WebResource webResource = client.resource("http://localhost:8080/CMPE273-Client/webResource/ServerOnDevice/bootstrap");
			response = webResource.type(MediaType.APPLICATION_JSON).post(String.class, result);
		} catch (Exception e){
			e.printStackTrace();
		}
		//MongoDb.close();

		return Response.status(201).entity(response).build();
	}
}
