package webResource;

import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import data.ManagerDAO;

public class ClientOnManager {
	private static String ManagerUrl = "http://localhost:8080/CMPE273/webResource/ResourceManage/";
//	private static String DeviceUrl = "http://localhost:8080/CMPE273-Client/webResource/ServerOnDevice/";
	
	public static void discover(String manufacturer, String model, String sn){
		JSONObject data = null;
		try {
			System.out.println("Sending discover request to client ...");
			ManagerDAO.Connect();
			String clientUri = ManagerDAO.search(manufacturer, model, sn, "ClientUri");
			
			try{
				data = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri);
			} catch (JSONException e){
				e.printStackTrace();
			}
			Client client = Client.create();
			WebResource webResource = client.resource(ManagerUrl+"discover");
			String response = webResource.type(MediaType.APPLICATION_JSON).post(String.class, data);
	
			System.out.println("echo:\n"+response+"\n");
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}
	

	public static void read(String manufacturer, String model, String sn, String rscName){
		JSONObject data = null;
		try {
			System.out.println("Reading "+rscName+" value from client ...");
			ManagerDAO.Connect();
			String clientUri = ManagerDAO.search(manufacturer, model, sn, "ClientUri");
			try{
				data = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName);
			} catch (JSONException e){
				e.printStackTrace();
			}
			Client client = Client.create();
			WebResource webResource = client.resource(ManagerUrl+"read");
			String response = webResource.type(MediaType.APPLICATION_JSON).post(String.class, data);
	
			System.out.println("echo:\n"+response+"\n");
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}
	
	public static void execute(String manufacturer, String model, String sn, String rscName, String op){
		System.out.println("Executing Operation  "+rscName+" ...");

		JSONObject data = null;
		try {
			ManagerDAO.Connect();
			String clientUri = ManagerDAO.search(manufacturer, model, sn, "ClientUri");
			try{
				data = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName).put("Operation", op);
			} catch (JSONException e){
				e.printStackTrace();
			}
			Client client = Client.create();
			WebResource webResource = client.resource(ManagerUrl+"execute");
			String response = webResource.type(MediaType.APPLICATION_JSON).post(String.class, data);
	
			System.out.println("echo:\n"+response+"\n");
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}
	
	public static void write(String manufacturer, String model, String sn, String rscName, String value){
		System.out.println("Writing "+rscName+" with "+value+" to client ...");

		JSONObject data = null;
		try {
			ManagerDAO.Connect();
			String clientUri = ManagerDAO.search(manufacturer, model, sn, "ClientUri");
			try{
				data = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName).put("Value", value);
			} catch (JSONException e){
				e.printStackTrace();
			}
			Client client = Client.create();
			WebResource webResource = client.resource(ManagerUrl+"write");
			String response = webResource.type(MediaType.APPLICATION_JSON).post(String.class, data);
	
			System.out.println("echo:\n"+response+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void create(String manufacturer, String model, String sn, String clientUri){
		System.out.println("Creating "+model+" ...");
		JSONObject data = null;
	  	try{
			ManagerDAO.Connect();

		} catch (Exception e) {
	  		e.printStackTrace();
	  	}
	}
	
//	public static void create(String device,JSONObject nrc){
//		try {
//			System.out.println("Creating new resource"+ nrc.toString()+" on client ...");
//			Client client = Client.create();
//			WebResource r = client.resource(device+"create");
//			ClientResponse response = r.type("application/json")
//					.post(ClientResponse.class,nrc);
//	
//			if (response.getStatus() > 202) {
//			   throw new RuntimeException("Failed : HTTP error code : "
//				+ response.getStatus());
//			}
//			System.out.println("echo:\n"+response.getEntity(String.class));
//		  } catch (Exception e) {
//			e.printStackTrace();
//		  }
//	}
	
	public static void observe(String manufacturer, String model, String sn, String rscName, String when, String value){
		if (!when.equals("="))
			System.out.println("Notify the server when "+rscName+" is "+when+ " "+value+" ...");
		else
			System.out.println("Notify the server when "+rscName+" is changed");
		JSONObject data = null;
		try{
			ManagerDAO.Connect();
			String clientUri = ManagerDAO.search(manufacturer, model, sn, "ClientUri");
			data = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName).put("When", when).put("Value", value);
		
			Client client = Client.create();
			WebResource webResource = client.resource(ManagerUrl+"observe");
			String response = webResource.type(MediaType.APPLICATION_JSON).post(String.class, data);
	
			System.out.println("echo:\n"+response+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void cancelObserve(String manufacturer, String model, String sn, String rscName){
		System.out.println("Cancel observing "+rscName+" ...");
		JSONObject data = null;
		try{
			ManagerDAO.Connect();
			String clientUri = ManagerDAO.search(manufacturer, model, sn, "ClientUri");
			data = new JSONObject().put("Manufacturer", manufacturer).put("Model", model).put("SN", sn).put("ClientUri", clientUri).put("RscName", rscName);
		
			Client client = Client.create();
			WebResource webResource = client.resource(ManagerUrl+"cancelObserve");
			String response = webResource.type(MediaType.APPLICATION_JSON).post(String.class, data);
	
			System.out.println("echo:\n"+response+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void observe(String device,String rsc,String url){
//		try {
//			System.out.println("Start observing resource"+ rsc.toString()+" on client ...");
//			Client client = Client.create();
//			WebResource r = client.resource(device+"observe");
//			JSONObject obj = new JSONObject().put("Resource", rsc).put("Url", url);
//			ClientResponse response = r.type("application/json")
//					.post(ClientResponse.class,obj);
//	
//			if (response.getStatus() > 202) {
//			   throw new RuntimeException("Failed : HTTP error code : "
//				+ response.getStatus());
//			}
//			System.out.println("echo:\n"+response.getEntity(String.class));
//		  } catch (Exception e) {
//			e.printStackTrace();
//		  }
//	}
	
	public static void writeAttr(String device,String rsc){
		try {
			System.out.println("Start observing resource"+ rsc.toString()+" on client ...");
			Client client = Client.create();
			WebResource r = client.resource(device+"writeAttr");
			JSONObject obj = new JSONObject().put("Resource", rsc).put("Cancel", "True");
			ClientResponse response = r.type("application/json")
					.post(ClientResponse.class,obj);
	
			if (response.getStatus() > 202) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
			}
			System.out.println("echo:\n"+response.getEntity(String.class));
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}
	
	public static void delete(String device,String rsc){
		try {
			System.out.println("Deleting resource"+ rsc+" on client ...");
			Client client = Client.create();
			WebResource r = client.resource(device+"delete");
			ClientResponse response = r.type("text/plain")
					.post(ClientResponse.class,rsc);
	
			if (response.getStatus() > 202) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
			}
			System.out.println("echo:\n"+response.getEntity(String.class));
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}
	
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);  

		discover("DAIKIN", "AC", "CMPE273AC001");
		System.out.print("Press enter to continue....\n");
		input.hasNextLine();	
		input = new Scanner(System.in);
		
		read("DAIKIN", "AC", "CMPE273AC001","Thermometer");
		System.out.print("Press enter to continue....\n");
		input.hasNextLine();
		input = new Scanner(System.in);
		
//		read("DAIKIN", "AC", "CMPE273AC001","wefwef");

		execute("DAIKIN", "AC", "CMPE273AC001","Freezer", "Off");
		System.out.print("Press enter to continue....\n");
		input.hasNextLine();
		input = new Scanner(System.in);
		
		write("DAIKIN", "AC", "CMPE273AC001","Thermometer", "4");
		System.out.print("Press enter to continue....\n");
		input.hasNextLine();
		input = new Scanner(System.in);
		
//		create("PHILIPS", "LOCK", "CMPE273LK001", "http://localhost:8080/CMPE273-Client/webResource/ServerOnDevice");
		observe("DAIKIN", "AC", "CMPE273AC001","Thermometer", ">", "3");
		System.out.print("Press enter to continue....\n");
		input.hasNextLine();
		input = new Scanner(System.in);
		
//		cancelObserve("DAIKIN", "AC", "CMPE273AC001","Thermometer");
		write("DAIKIN", "AC", "CMPE273AC001","Thermometer", "5");
		System.out.print("Press enter to continue....\n");
		input.hasNextLine();
		input = new Scanner(System.in);
		
		cancelObserve("DAIKIN", "AC", "CMPE273AC001","Thermometer");
		



		


		
//		discover(DeviceUrl);
//		read(DeviceUrl,"Thermometer");
//		writeAttr(DeviceUrl,"Thermometer");
//
//		write(DeviceUrl,"Thermometer","4 Celcius");
//		read(DeviceUrl,"Thermometer");
//		JSONObject nrc = new JSONObject().put("Name","Light").put("Value", "Off").put("Observed", "N").put("Observer", "");
//		create(DeviceUrl,nrc);
//		discover(DeviceUrl);
//		delete(DeviceUrl,"Light");
//		discover(DeviceUrl);
//		execute(DeviceUrl,"IceMaker","On");
//		discover(DeviceUrl);
//		observe(DeviceUrl,"Thermometer",ManagerUrl+"notification");
//		discover(DeviceUrl);
//		for(int i=0;i<12;i++){
//			write(DeviceUrl,"Thermometer",i+" Celcius");
//			read(DeviceUrl,"Thermometer");
//		}
//		discover(DeviceUrl);
	}

}
