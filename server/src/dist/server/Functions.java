package dist.server;

public class Functions {

	
	public static String hello(){
		return "Hello";
	}
	
	public static String createJSONEntry(String name, String value){
		return ServerGlobals.quoteMark+name+ServerGlobals.quoteMark+ServerGlobals.colon+" "+ServerGlobals.quoteMark+value+ServerGlobals.quoteMark;
	}
}
