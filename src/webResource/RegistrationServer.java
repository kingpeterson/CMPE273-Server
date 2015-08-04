package webResource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import data.ManagerDAO;
import data.MongoDb;

@Path("/RegistrationServer")
public class RegistrationServer {
	@POST
	@Path("/Register")
	@Consumes(MediaType.APPLICATION_JSON)
	//input manufacturer, model, sn, clientUri, registered (0 for de-register, 1 for register)
	public Response Register(String input){
		String result = "";
		try{
			JSONObject obj = new JSONObject(input);
			String manufacturer = obj.getString("Manufacturer");
			String model = obj.getString("Model");
			String sn = obj.getString("SN");
			String clientUri = obj.getString("ClientUri");
			String registered = obj.getString("Registered");
			String registerTime =  new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
			String expireTime = "";
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			try {
				Date d = df.parse(registerTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.SECOND, 3600);
				expireTime = df.format(cal.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			result = ManagerDAO.register(manufacturer, model, sn, clientUri, registered, registerTime, expireTime);
			
				
		} catch (Exception e){
			e.printStackTrace();
		}
		return Response.status(201).entity(result).build();

	}
	
	@POST
	@Path("/Update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Update(String input){
		String result = "";
		try{
			JSONObject obj = new JSONObject(input);
			String objectID = obj.getString("ObjectID");
			String newInstance = obj.getString("NewInstance");
			int status = obj.getInt("Status");
			result = Integer.toString(MongoDb.update(objectID, newInstance, status));
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return Response.status(201).entity(result).build();
	}
}
