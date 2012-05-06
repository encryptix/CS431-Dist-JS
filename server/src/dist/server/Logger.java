package dist.server;

import java.util.Calendar;

public class Logger {
	//These messages come up in the server log
	public static void severe(String location, String message){
		System.out.println(" ["+getTime()+" ERROR @ "+location+"] "+message);
	}

	public static void info(String location, String message){
		if(ServerGlobals.logLevel.equals("ALL"))
			System.out.println(" ["+getTime()+" INFO @ "+location+"] "+message);
	}

	public static void debug(String location, String message){
		if(ServerGlobals.logLevel.equals("ALL")||ServerGlobals.logLevel.equals("DEBUG"))
			System.out.println(" ["+getTime()+" DEBUG @ "+location+"] "+message);
	}
	
	public static String getTime(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		String hourOffset=hour<10 ? "0":"";
		String minuteOffset=minute<10 ? "0":"";
		String secondOffset=second<10 ? "0":"";
				
		return hourOffset+hour+":"+minuteOffset+minute+":"+secondOffset+second;
	}

}