package webResource;

import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.json.*;
import data.*;

@Path("/Rest")
public class Rest {
	@GET
	@Path("/getInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(){
		return "Hello Get";
	}
	
	@POST
	@Path("/postInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postInfo(String input){
		String result = "";
		try{
			JSONObject obj = new JSONObject(input);
			String manufacturer = obj.getString("Manufacturer");
			String serialNumber = obj.getString("SerialNumber");
			String data = obj.getString("Data");
			
			String currentTime =  new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

			String registerStatus = MongoDb.findRegistration(manufacturer, serialNumber);
			JSONObject object = new JSONObject(registerStatus);
			int isRegistered = object.getInt("isRegistered");
			String timeStamp = object.getString("timeStamp");
			
			if (isRegistered == 0){
				result = "Device not yet registered, cannot use service. Please register.";
			}
			else{
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				Date date1 = format.parse(currentTime);
				Date date2 = format.parse(timeStamp);
				long difference = date2.getTime() - date1.getTime();
				
				if (difference > 0){
					result = "Data received: "+data+"";
				}
				else{
					MongoDb.register(manufacturer, serialNumber, 0);
					result = "Registration expired, please register again";
				}
			}

			
//			DataPreservation.insertData(id, data);
//			String save = DataPreservation.getData(id);
//			System.out.println(save);

		}catch (Exception e){
			e.printStackTrace();
		}
		System.out.println(result);
		return Response.status(201).entity(result).build();
	}
	
	
	
}















