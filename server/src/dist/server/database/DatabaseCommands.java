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

	public boolean editTestEntry(String id, String content){
		//move this to global functions
		content=content.replaceAll("'", "\"");
		String statement = "UPDATE TEST SET js= ? WHERE id= ?";
		String[] inputs = new String[2];
		inputs[0] = content;
		inputs[1] = id;

		try{
			_theBase.executePreparedUpdateOrInsertStatement(statement, inputs);
			return true;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - editTestEntry","Exception: "+ex.getLocalizedMessage());
			return false;
		}
	}

	public boolean addTestEntry(String content){
		String statement = "INSERT INTO TEST VALUES ( NULL, ?)";
		String[] inputs = new String[1];
		inputs[0] = content;

		try{
			_theBase.executePreparedUpdateOrInsertStatement(statement, inputs);
			return true;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - addTestEntry","Exception: "+ex.getLocalizedMessage());
			return false;
		}
	}

	//REDO this method
	public ArrayList<Data> getData(String jobID,String limit){
		//this statement should increment resultCount
		String statement = "SELECT * FROM DATA WHERE resultCount < 2 AND job_id = ? LIMIT "+limit;
		String[] inputs = new String[1];
		inputs[0] = jobID;
		ArrayList<Data> dataList = new ArrayList<Data>();
		
		try{
			ResultSet results = _theBase.executePreparedSelectStatement(statement, inputs);

			while(results.next()){
				String dataID = results.getString(1);
				String data = results.getString(4);
				dataList.add(new Data(data,jobID,dataID));
			}
		}catch(SQLException e){
			Logger.severe("{DBC - getData 1}", e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
		
		String statementInner = "UPDATE DATA SET resultCount = resultCount +1 WHERE data_id = ?";
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
		String statement = "SELECT * FROM JOBS WHERE id=1";

		try{
			ResultSet rs = _theBase.executePreparedSelectStatement(statement,new String[0]);
			ArrayList<Jobs> results = new ArrayList<Jobs>();
			while(rs.next()){
				results.add(new Jobs(rs.getString(1),rs.getString(2)));
			}
			return results;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - getJob()","Exception: "+ex.getLocalizedMessage());
			return null;
		}
	}
	/*
	 * Give out a job - > give each job an id
	 * Give each piece of work a job_id, piece_id (each job a table?)
	 * When a piece of work is given out , timestamp it .. allow 2 minutes...
	 * When result comes back check that it is within the 2 minutes and if so save
	 * This should also give out another piece of work
	 * 
	 * 
	 * Upon First load is when a job is selected -> WHen units of work are done, give more units
	 * 
	 * for units select the closest one near the top which has not been given out
	 * 		[How do I tell the difference between one solved, and then the 2nd answer]
	 * 
	 * when selecting work I return the results with id
	 * so i pick a job where the id is > than the one returned
	 * this ensures i dont get the same job twice
	 * 
	 */

	public boolean addJobOne(String dataID, String results) {
		String statement = "INSERT INTO RESULT_TEST_ONE VALUES(?, ?)";
		String[] inputs = new String[2];
		inputs[0] = dataID;
		inputs[1] = results;
	
		try{
			_theBase.executePreparedUpdateOrInsertStatement(statement, inputs);
			return true;
		}catch(SQLException ex){
			Logger.severe("DatabaseCommands - addJobOne()","Exception: "+ex.getLocalizedMessage());
			return false;
		}
	}
}
