package dist.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import dist.server.Logger;
import dist.server.datastructure.Data;
import dist.server.datastructure.Jobs;

public class DatabaseCommands {
	private Database _theBase = null;

	public DatabaseCommands(){
		_theBase = new Database();
	}

	public void ensureConsistency(){
		ArrayList<String> jobs = new ArrayList<String>();
		String statement = "SELECT id FROM JOBS";
		try{
			ResultSet results = _theBase.executePreparedSelectStatement(statement, new String[0]);			
			while(results.next()){
				String jobID = results.getString("id");
				jobs.add(jobID);
			}
		}catch(SQLException e){
			Logger.severe("{DBC - ensureConsistency 1}", e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		for(String jobID:jobs){
			String statement2 = "UPDATE DATA_"+jobID+" SET resultCount=resultCount-1 WHERE resultCount > 0 AND isAnswered = 0";
			try{
				_theBase.executePreparedUpdateOrInsertStatement(statement2, new String[0]);
			}catch(SQLException e){
				Logger.severe("{DBC - ensureConsistency 2}", e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<Data> getData(String jobID,String limit){
		//this statement should increment resultCount
		String statement = "SELECT * FROM DATA_"+jobID+" WHERE resultCount < 1 LIMIT "+limit;
		String[] inputs = new String[0];
		ArrayList<Data> dataList = new ArrayList<Data>();
		
		try{
			ResultSet results = _theBase.executePreparedSelectStatement(statement, inputs);

			while(results.next()){
				String dataID = results.getString("data_id");
				String data = results.getString("data");
				dataList.add(new Data(data,jobID,dataID));
			}
		}catch(SQLException e){
			Logger.severe("{DBC - getData 1}", e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
		
		String statementInner = "UPDATE DATA_"+jobID+" SET resultCount = resultCount +1 WHERE data_id = ?";
		String[] inputInner = new String[1];
		for(int i=0;i<dataList.size();i++){
			inputInner[0] = dataList.get(i).getDataID();

			try{
				_theBase.executePreparedUpdateOrInsertStatement(statementInner, inputInner);
			}catch(SQLException e){
				Logger.severe("{DBC - getData 2}", e.getLocalizedMessage());
				e.printStackTrace();
				return null;
			}
		}
		return dataList;
	}

	public ArrayList<Jobs> getJobs(){
		String statement = "SELECT * FROM JOBS";

		try{
			ResultSet rs = _theBase.executePreparedSelectStatement(statement,new String[0]);
			ArrayList<Jobs> results = new ArrayList<Jobs>();
			while(rs.next()){
				results.add(new Jobs(rs.getString(1),rs.getString(2)));
			}
			return results;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - getJob()","Exception: "+ex.getLocalizedMessage());
			ex.printStackTrace();
			return null;
		}
	}

	//returns the result row or null if it does not exist
	public Data getResult(String jobID, String dataID){
		String statement = "SELECT result FROM RESULTS_"+jobID+" WHERE id = ?";
		String[] inputs = new String[1];
		inputs[0] = dataID;
		
		try{
			Logger.debug("DBC - getResult", "Checking if id "+dataID+" exists with statement "+statement);
			ResultSet result = _theBase.executePreparedSelectStatement(statement, inputs);
			
			if(result.next()){
				String resStr = result.getString("result");
				return new Data(resStr,jobID,dataID);
			}
			return null;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - getResult()","Exception: "+ex.getLocalizedMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	public boolean deleteResult(String jobID, String dataID){
		String statement = "DELETE FROM RESULTS_"+jobID+" WHERE dataID = ?";
		String[] inputs = new String[1];
		inputs[0] = dataID;
		
		try{
			_theBase.executePreparedUpdateOrInsertStatement(statement, inputs);
			return true;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - deleteResult()","Exception: "+ex.getLocalizedMessage());
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean resetResultCount(String jobID, String dataID) {
		String statement = "UPDATE DATA_"+jobID+" SET resultCount = 0 WHERE id = ?";
		String[] inputs = new String[1];
		inputs[0] = jobID;
		
		try{
			_theBase.executePreparedUpdateOrInsertStatement(statement, inputs);
			return true;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - resetResultCount()","Exception: "+ex.getLocalizedMessage());
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean addResult(String jobID, String dataID, String results) {
		String statement = "INSERT INTO RESULTS_"+jobID+" VALUES( ? , ? )";
		String[] inputs = new String[2];
		inputs[0] = dataID;
		inputs[1] = results;

		try{
			_theBase.executePreparedUpdateOrInsertStatement(statement, inputs);
			return true;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - addResult()","Exception: "+ex.getLocalizedMessage());
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean setResultOK(String jobID, String dataID){
		String statement = "UPDATE DATA_"+jobID+" SET isAnswered = 1 WHERE data_id = ?";
		String[] inputs = new String[1];
		inputs[0] = dataID;
		
		try{
			_theBase.executePreparedUpdateOrInsertStatement(statement, inputs);
			return true;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - setResultOK()","Exception: "+ex.getLocalizedMessage());
			ex.printStackTrace();
			return false;
		}
	}
}