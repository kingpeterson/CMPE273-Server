package webResource;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import data.ManagerDAO;
import data.MongoDb;

@Path("/ResourceManage")
public class ResourceManageServer {
	public static String log="tmp:";
	public static int count = 0;
	
	public boolean checkRegistered(JSONObject input){
		boolean isRegistered = false;
		String registered = "";
		try {
			ManagerDAO.Connect();
			registered = ManagerDAO.search(input.getString("Manufacturer"), input.getString("Model"), input.getString("SN"), "Registered");
			if (registered.equals("1")){
				String endTime = ManagerDAO.search(input.getString("Manufacturer"), input.getString("Model"), input.getString("SN"), "EndTime");
				String currentTime =  new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date date1 = format.parse(currentTime);
				Date date2 = format.parse(endTime);
				long difference = date2.getTime() - date1.getTime();
				int diffSec = (int) (difference/(1000));

				if (diffSec > 0){
					isRegistered = true;
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isRegistered;
	}
	
	
	@Path("/discover")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
//	input = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri);
	public String discover(JSONObject input){
		if (!checkRegistered(input)){
			return "Cannot complete, device not yet registered";
		}
		
		String result = "";
		String clientUri = "";
		try {
			clientUri = input.getString("ClientUri");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Client client = Client.create();
		WebResource webResource = client.resource(clientUri+"discover");
		result = webResource.type(MediaType.APPLICATION_JSON).post(String.class, input);
		
		return result;
	}
	
	@Path("/read")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
//	input = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName);
	public String read(JSONObject input){
		if (!checkRegistered(input)){
			return "Cannot complete, device not yet registered";
		}
		
		String result = "";
		String clientUri = "";
		try {
			clientUri = input.getString("ClientUri");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Client client = Client.create();
		WebResource webResource = client.resource(clientUri+"read");
		result = webResource.type(MediaType.APPLICATION_JSON).post(String.class, input);
		return result;
	}
	
	@Path("/execute")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
//	input = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName).put("Operation", op);
	public String execute(JSONObject input){
		if (!checkRegistered(input)){
			return "Cannot complete, device not yet registered";
		}
		String result = "";
		String clientUri = "";
		try {
			clientUri = input.getString("ClientUri");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Client client = Client.create();
		WebResource webResource = client.resource(clientUri+"execute");
		result = webResource.type(MediaType.APPLICATION_JSON).post(String.class, input);
		return result;
	}
	
	@Path("/write")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
//	input = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName).put("Value", value);
	public String write(JSONObject input){
		if (!checkRegistered(input)){
			return "Cannot complete, device not yet registered";
		}
		String result = "";
		String clientUri = "";
		try {
			clientUri = input.getString("ClientUri");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Client client = Client.create();
		WebResource webResource = client.resource(clientUri+"write");
		result = webResource.type(MediaType.APPLICATION_JSON).post(String.class, input);
		return result;
	}
	
	@Path("/observe")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
//	input = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName).put("When", when).put("Value", value);
	public String observe(JSONObject input){
		if (!checkRegistered(input)){
			return "Cannot complete, device not yet registered";
		}
		String result = "";
		String clientUri = "";
		try {
			clientUri = input.getString("ClientUri");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Client client = Client.create();
		WebResource webResource = client.resource(clientUri+"observe");
		result = webResource.type(MediaType.APPLICATION_JSON).post(String.class, input);
		return result;
	}
	
	@Path("/cancelObserve")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
//	input = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName);
	public String cancelObserve(JSONObject input){
		if (!checkRegistered(input)){
			return "Cannot complete, device not yet registered";
		}
		String result = "";
		String clientUri = "";
		try {
			clientUri = input.getString("ClientUri");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Client client = Client.create();
		WebResource webResource = client.resource(clientUri+"cancelObserve");
		result = webResource.type(MediaType.APPLICATION_JSON).post(String.class, input);
		return result;
	}
	
	@Path("/notification")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String notification(JSONObject input){
		try {
			if (input.getString("Compare").equals("!=")){
				System.out.println(input.getString("Resource")+ " changed value");
			}
			else if (input.getString("Compare").equals(">")){
				System.out.println(input.getString("Resource")+ " is greater than " + input.getString("Value"));
			}
			else if (input.getString("Compare").equals("<"))
				System.out.println(input.getString("Resource")+ " is less than " + input.getString("Value"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "Notification received";
	}

	
//	@Path("/get")
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public String get(){
////		String url = "http://localhost:8080/com.dexter.device/get";
//		String url = "http://localhost:8080/CMPE273-Client/webResource/ServerOnDevice/get";
//		try {
//			Client client = Client.create();
//			WebResource r = client.resource(url);
//			ClientResponse response = r.accept("text/plain").get(ClientResponse.class);
//	
//			if (response.getStatus() > 202) {
//			   throw new RuntimeException("Failed : HTTP error code : "
//				+ response.getStatus());
//			}
//			
//			String retstr=response.getEntity(String.class);
//			return retstr+"\n"+log;
//		  } catch (Exception e) {
//			e.printStackTrace();
//			return "we got problem here\n";
//		  }
//	}
	
//	@Path("/notification")
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response setName(String objString){
//		JSONObject obj;
//		ManagerDAO.Connect();
//		try {
//			obj = new JSONObject(objString);
//			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//			Calendar calobj = Calendar.getInstance();
//			obj.put("Time",df.format(calobj.getTime()));
//			//log+="\n"+obj.toString();
//			ManagerDAO.InsertRecord(obj);
//			if(ManagerDAO.CountRecord()==10){
//				return Response.status(201).entity("stop").build();
//			}else
//				return Response.status(201).entity("go on").build();
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return Response.status(202).entity("rename failed").build();
//		}
//	}
	
//	@Path("/set/{new}")
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public String set(@PathParam("new") String nname){
//		String url = "http://localhost:8080/CMPE273-Client/webResource/ServerOnDevice/set";
//
//		try {
//			Client client = Client.create();
//			WebResource r = client.resource(url);
//			ClientResponse response = r.type("application/json")
//					.post(ClientResponse.class, new JSONObject().put("Name", nname));
//	
//			if (response.getStatus() > 202) {
//			   throw new RuntimeException("Failed : HTTP error code : "
//				+ response.getStatus());
//			}
//			
//			String retstr=response.getEntity(String.class);
//			return retstr;
//		  } catch (Exception e) {
//			e.printStackTrace();
//			return "we got problem here\n";
//		  }
//	}
}
