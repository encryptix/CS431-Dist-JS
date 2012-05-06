package dist.server.recieve;

import dist.server.database.DatabaseCommands;
import dist.server.datastructure.Data;

public class RecieveResults {
	//Base 64 at some point
	private DatabaseCommands _dbc;
	
	public RecieveResults(){
		_dbc = new DatabaseCommands();
	}
	
	/**
	 * 
	 * @param jobID
	 * @param dataID
	 * @param results
	 * 
	 * 
	 * 	check if already in DB (by returning the result/null)
	 *	if doesn't exists CREATE
	 *	if does exist CHECK RESULT
	 *	if not SAME Reset result count and delete entry.
	 *  if the SAME set the FLAG in the result row
	 */
	public void recieveJobResults(String jobID, String dataID, String results){
		Data currentResult = _dbc.getResult(jobID, dataID);
		if(currentResult==null){
			_dbc.addResult(jobID, dataID, results);
		}else{
			if(results.equals(currentResult.getData())){
				_dbc.setResultOK(jobID,dataID);
			}else{
				_dbc.deleteResult(jobID,dataID);
				_dbc.resetResultCount(jobID,dataID);
			}
		}
	}
}
