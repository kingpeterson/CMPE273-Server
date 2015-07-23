package webResource;

import java.util.Scanner;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.InputMismatchException;

//import data.MongoDb;

public class Request {
//	private static String clientURI = "";
	private static Scanner scan;


	public static void main(String[] args){
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
				case 1:
					break;
					
				case 2:
					break;
					
				case 3:
					break;
					
				case 4:

					System.out.println("Please input the Instance ID to create");
					String newInstance = scan.nextLine();
	
					System.out.println(create(objectID, newInstance));
					System.out.println("\n");
					break;
				case 5:
					System.out.println("Please input the Instance ID to delete");
					String newInstance1 = scan.nextLine();
					System.out.println(delete(objectID, newInstance1));
					System.out.println("\n");
					break;
					
				case 6:
					System.out.println("What action to Execute?");
					System.out.println("1. Turn on device");
					System.out.println("2. Turn off device");
					System.out.println("3. Change color");
					input = scan.nextInt();
					System.out.println(execute(objectID, input));
					System.out.println("\n");
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
	
	public static String create(String objectID, String newInstance){
		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost:8080/CMPE273-Client/webResource/HandleRequest/Create");
		String newInsert = "{\"ObjectID\": \""+objectID+"\", \"NewInstance\": \""+newInstance+"\"}";
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, newInsert);
		if(response.getStatus() != 201){
			throw new RuntimeException("Failed HTTP error code:" + response.getStatus());
		}
		String output = response.getEntity(String.class);
		return output;
	}
	
	public static String delete(String objectID, String newInstance){
		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost:8080/CMPE273-Client/webResource/HandleRequest/Delete");
		String newDelete = "{\"ObjectID\": \""+objectID+"\", \"NewInstance\": \""+newInstance+"\"}";
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, newDelete);
		if(response.getStatus() != 201){
			throw new RuntimeException("Failed HTTP error code:" + response.getStatus());
		}
		String output = response.getEntity(String.class);
		
		return output;
	}
	
	public static String execute(String objectID, int action){
		String toDo = "";
		String behavior = "";
		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost:8080/CMPE273-Client/webResource/HandleRequest/Execute");
		if (action == 1){
			toDo = "powerOn";
			behavior = "on";
			String newInsert = "{\"ObjectID\": \""+objectID+"\", \"Action\": \""+toDo+"\" , \"Behavior\": \""+behavior+"\"}";
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, newInsert);
			if(response.getStatus() != 201){
				throw new RuntimeException("Failed HTTP error code:" + response.getStatus());
			}
			String output = response.getEntity(String.class);
			if (output.equals("success"))
				return "The lightbulb has been turned on";
			else
				return "Failed";
		}
		else if (action == 2){
			toDo = "powerOn";
			behavior = "off";
			String newInsert = "{\"ObjectID\": \""+objectID+"\", \"Action\": \""+toDo+"\" , \"Behavior\": \""+behavior+"\"}";
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, newInsert);
			if(response.getStatus() != 201){
				throw new RuntimeException("Failed HTTP error code:" + response.getStatus());
			}
			String output = response.getEntity(String.class);
			if (output.equals("success"))
				return "The lightbulb has been turned off";
			else
				return "failed";
		}
		else{
			toDo = "lightColor";
			behavior = "Yellow";
			String newInsert = "{\"ObjectID\": \""+objectID+"\", \"Action\": \""+toDo+"\" , \"Behavior\": \""+behavior+"\"}";
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, newInsert);
			if(response.getStatus() != 201){
				throw new RuntimeException("Failed HTTP error code:" + response.getStatus());
			}
			String output = response.getEntity(String.class);
			if (output.equals("success"))
				return "The lightbulb has changed color";
			else
				return "failed";
		}
//		String newInsert = "{\"ObjectID\": \""+objectID+"\", \""+newInstance+"\": \""+newValue+"\"}";
//		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, newInsert);
//		if(response.getStatus() != 201){
//			throw new RuntimeException("Failed HTTP error code:" + response.getStatus());
//		}
//		String output = response.getEntity(String.class);
//		System.out.println(output);
//		return clientResponse;
	}
	
	public static void menu(){
		System.out.println("Choose one of the functions\n");
		System.out.println("Press 0 to Shut down\n");
		System.out.println("Press 4 to Create\n");
		System.out.println("Press 5 to Delete\n");
		System.out.println("Press 6 to execute\n");
	}
}
