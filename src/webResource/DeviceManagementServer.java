package webResource;

import java.util.Scanner;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

//import data.MongoDb;

public class DeviceManagementServer {
	private static String clientURI = "http://localhost:8080/CMPE273-Client/webResource/HandleRequest/";
	private static Scanner scan;
	private static String ManagerUrl = "http://localhost:8080/CMPE273/webResource/ResourceManage";
	private static String DeviceUrl = "http://localhost:8080/CMPE273-Client/webResource/ServerOnDevice";
	
	//Initiate the connection to server
	public static String connect(String action, JSONObject newInsert){
		String output = "";
		Client client = Client.create();
		WebResource webResource = client.resource(clientURI + action);
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, newInsert);
		if(response.getStatus() != 201){
			throw new RuntimeException("Failed HTTP error code:" + response.getStatus());
		}
		output = response.getEntity(String.class);
		return output;
	}
	
	public static void read(String objectID){
		JSONObject newInsert = null;
		try {
			newInsert = new JSONObject().put("ObjectID", objectID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(connect("Read", newInsert));
	}
	
	public static void discover(String objectID, String instance, String resource){
		JSONObject newInsert = null;
		try{
			newInsert = new JSONObject().put("ObjectID", objectID).put("Instance", instance).put("Resource", resource);
		}catch(JSONException e){
			e.printStackTrace();
		}
		String output = connect("Discover", newInsert);
	}
	
	public static void write(String objectID, String instance, String field, String value){
		JSONObject newInsert = null;
		try{
			newInsert = new JSONObject().put("ObjectID", objectID).put("Instance", instance).put("Field", field).put("Value", value);
		}catch(JSONException e){
			e.printStackTrace();
		}
		System.out.println(connect("Write", newInsert));
		
	}
	
	public static String create(String objectID, String newInstance){
		JSONObject newInsert = null;
		try{
			newInsert = new JSONObject().put("ObjectID", objectID).put("NewInstance", newInstance);
		} catch(JSONException e){
			e.printStackTrace();
		}
		return connect("Create", newInsert);
	}
	
	public static String delete(String objectID, String newInstance){
		JSONObject newInsert = null;
		try{
			newInsert = new JSONObject().put("ObjectID", objectID).put("NewInstance", newInstance);
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		return connect("Delete", newInsert);
	}
	
	public static String execute(String objectID, int action){
		String toDo = "";
		String behavior = "";
		if (action == 1){
			toDo = "powerOn";
			behavior = "on";
		}
		else if (action == 2){
			toDo = "powerOn";
			behavior = "off";
		}
		else if (action == 3){
			toDo = "lightColor";
			behavior = "Yellow";
		}
		
		JSONObject newInsert = null;
		try{
			newInsert = new JSONObject().put("ObjectID", objectID).put("Action", toDo).put("Behavior", behavior);
		}catch(JSONException e){
			e.printStackTrace();
		}

		String output = connect("Execute", newInsert);
		if (action == 1 && output.equals("success")){
			return "The lightbulb has been turned on";
		}
		else if (action == 2 && output.equals("success")){
			return "The lightbulb has been turned off";
		}
		else if (action == 3 && output.equals("success")){
			return "The lightbulb has changed color";
		}
		else{
			return "failed";
		}
	}
	
	public static void menu(){
		System.out.println("Choose one of the functions\n");
		System.out.println("Press 0 to Shut down\n");
		System.out.println("Press 1 to Read\n");
		System.out.println("Press 2 to Discover\n");
		System.out.println("Press 3 to Write\n");
		System.out.println("Press 4 to Create\n");
		System.out.println("Press 5 to Delete\n");
		System.out.println("Press 6 to execute\n");
		System.out.println("Press 7 to Observe\n");
		System.out.println("Press 8 to Write Attributes\n");
		System.out.println("Press 9 to Cancel Obeserve\n");
	}
	
	public static void main(String[] args) throws JSONException{
		String objectID = "";
		int input = 0;
		while (input != 11){
			scan = new Scanner(System.in);
			System.out.println("Please choose a device");
			System.out.println("1. Philips lightbulb");
//			System.out.println("2. ");
			input = scan.nextInt();
			scan.nextLine();
			if (input == 11)
				break;
			if (input == 1){
				objectID = "PhilipsLightBulb";
			}
			else{
				objectID = "0000000000010002";
			}
			menu();
			input = scan.nextInt();
			scan.nextLine();
			while (input != 0){
				switch(input){
				//read
				case 1:
					read(objectID);
					break;
				
				//discover
				case 2:
					System.out.println("Please input a Instance ID");
					String whatInstance = scan.nextLine();
//					System.out.println("Please input the resourceID");
//					String resource = scan.nextLine();
//					discover(objectID, whatInstance, resource);
					System.out.println("Temp");
					break;
				
				//write
				case 3:
					read(objectID);
					System.out.println("Please input a Instance ID to write");
					String instance = scan.nextLine();
					System.out.println("Please input the field to write");
					String field = scan.nextLine();
					System.out.println("Please input the value");
					String value = scan.nextLine();
					write(objectID, instance, field, value);
					break;
				
				//create
				case 4:
					System.out.println("Please input the Instance ID to create");
					String newInstance = scan.nextLine();
					System.out.println(create(objectID, newInstance));
					System.out.println("\n");
					break;
					
				//delete
				case 5:
					System.out.println("Please input the Instance ID to delete");
					String newInstance1 = scan.nextLine();
					System.out.println(delete(objectID, newInstance1));
					System.out.println("\n");
					break;
				
				//execute
				case 6:
					System.out.println("What action to Execute?");
					System.out.println("1. Turn on device");
					System.out.println("2. Turn off device");
					System.out.println("3. Change color");
					input = scan.nextInt();
					System.out.println(execute(objectID, input));
					System.out.println("\n");
					break;
				
				//observe
				case 7:
					break;
				
				//write attribute
				case 8:
					break;
				
				//cancel observe
				case 9:
					break;
				default:
					System.out.println("Please input correctly\n");
				}
				menu();
				input = scan.nextInt();
				scan.nextLine();

			}
		}
	}
}
