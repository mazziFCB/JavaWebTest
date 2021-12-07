package utils;


public class Global {

	public static String getEmptyString(String value){
		if(value == null)
			return "";
		return value.trim();
	}

}
