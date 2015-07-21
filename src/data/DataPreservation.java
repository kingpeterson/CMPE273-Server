package data;

import java.util.ArrayList;
import java.util.HashMap;

public class DataPreservation {
	public static HashMap<String, ArrayList<String>> myHashMap = new HashMap<String, ArrayList<String>>();

	public static void insertData(String id, String data){
		if(myHashMap.get(id)!=null){
			myHashMap.get(id).add(data);	
		}else{
			ArrayList<String> arryList = new ArrayList<String>();
			arryList.add(data);
			myHashMap.put(id, arryList);
		}
	}
	
	public static String getData(String id){
		String retStr = "Device ID: "+id + " ";
		int count = 0;
		for(String dta : myHashMap.get(id)){
			retStr += "No."+ count + ":" + dta+"  ";
			count++;
		}
		return retStr;
	}
}
