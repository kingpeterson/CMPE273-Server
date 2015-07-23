package webResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import data.MongoDb;


//import data.DataPreservation;

@Path("/ServerBootstrap")
public class ServerBootstrap {
	@POST
	@Path("/postClientInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postInfo(String input){
		String result = "";
		try{
			JSONObject obj = new JSONObject(input);
			String manufacturer = obj.getString("Manufacturer");
			String productType = obj.getString("ProductType");
			String objectID = obj.getString("ObjectID");
			String modelNumber = obj.getString("ModelNumber");
			String serialNumber = obj.getString("SerialNumber");
			String firmwareVersion = obj.getString("FirmwareVersion");
			if (MongoDb.search(manufacturer, serialNumber)){
				result = MongoDb.findBootstrap(manufacturer, serialNumber);
			}
			else{
				if (!MongoDb.searchType(productType))
					result = "{\"found\": \"Product denied!\"}";
				else{
					MongoDb.insert(manufacturer, productType, objectID, modelNumber, serialNumber, firmwareVersion);
					result = "{\"found\": \"Product added to server\"}";
				}
				
			}

			System.out.println(result);

		} catch (Exception e){
			e.printStackTrace();
		}
		//MongoDb.close();

		return Response.status(201).entity(result).build();
	}
}
