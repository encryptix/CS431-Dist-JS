package dist.server.recieve;

import dist.server.Logger;
import dist.server.database.DatabaseCommands;

public class RecieveResults {
//depending on the job id, the table to save into is different
//RESULT_JOBID
//Each one should have the option of different encoding(?)
	private DatabaseCommands _dbc;
	
	public RecieveResults(){
		_dbc = new DatabaseCommands();
	}
	
	public void recieveJobOne(String dataID, String results){
		Logger.debug("{RecieveResults - recieveJobOne}", "Recieved Result: dataID: "+dataID+"\nResult:"+results);
		_dbc.addJobOne(dataID,results);
		//Job one returns a number
	}
}
