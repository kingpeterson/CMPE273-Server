package webResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import data.MongoDb;

@Path("/Registration")
public class Registration {
	@POST
	@Path("/Register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Register(String input){
		String result = "";
		String myTime = "";
		try{
			JSONObject obj = new JSONObject(input);
			String manufacturer = obj.getString("Manufacturer");
			String serialNumber = obj.getString("SerialNumber");
			int status = obj.getInt("Status");
			myTime = MongoDb.register(manufacturer, serialNumber, status);
			result = "Registration expiration time: "+myTime+"";
				
		} catch (Exception e){
			e.printStackTrace();
		}
		System.out.println(result);
		return Response.status(201).entity(result).build();

	}
//	@POST
//	@Path("/DeRegister")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response DeRegister(String input){
//		String result = "";
//		String myTime = "";
//		try{
//			JSONObject obj = new JSONObject(input);
//			String manufacturer = obj.getString("Manufacturer");		
//			String serialNumber = obj.getString("SerialNumber");
//			myTime = MongoDb.register(manufacturer, serialNumber, 0);
//			result = "DERegistration time: "+myTime+"";	
//			
//		} catch (Exception e){
//			e.printStackTrace();
//		} 
//		return Response.status(201).entity(result).build();
//
//	}
	
}