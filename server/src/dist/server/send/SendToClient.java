package dist.server.send;

import java.util.ArrayList;
import java.util.HashMap;

import dist.server.Logger;
import dist.server.ServerGlobals;
import dist.server.database.DatabaseCommands;
import dist.server.datastructure.Data;
import dist.server.datastructure.Jobs;

public class SendToClient {
	private static SendToClient _instance;
	
	public static SendToClient getInstance(){
		Logger.debug("{SentToClient - getInstance}", "Getting instance");
		if(_instance == null){
			Logger.debug("{SentToClient - getInstance}", "Create instance");
			_instance = new SendToClient();
		}
		return _instance;
	}
	 
	private DatabaseCommands _dbc;
	private HashMap<String, Jobs> _jobs;
	private String[] _jobIDs;
	private int _currentKey;
	
	private SendToClient(){
		_dbc = new DatabaseCommands();
		initJobs();
	}
	
	private void initJobs(){
		synchronized(this){
			_jobs = new HashMap<String,Jobs>();
			ArrayList<Jobs> jobs = _dbc.getJobs();
			
			int jobSize = jobs.size();
			_jobIDs = new String[jobSize];
			_currentKey =0;
			for(int i=0;i<jobSize;i++){
				Jobs job = jobs.get(i);
				_jobIDs[i] = job.getID();
				_jobs.put(job.getID(),job);
			}
		}
	}
	
	public String getData(String jobID){
		synchronized(jobID){
			Jobs job = _jobs.get(jobID);
			if(job==null){
				return "this should never happen";
			}
			if(job.sizeData()<=0){
				//Before doing this perform the check to see if all answered
				_dbc.ensureConsistency();
				
				ArrayList<Data> newData = _dbc.getData(jobID, ServerGlobals.sizeOfLists);
				if(newData!=null)
					job.appendToArrayList(newData);
			}
			return job.getData();
		}
	} 
	
	public String getJob(){
		synchronized(this){
			try{
				if(_jobIDs.length == 0 ){
					initJobs();
					if(_jobIDs.length == 0 ){
						return "no_job";
					}else{
						if(_currentKey>=_jobIDs.length){
							_currentKey=0;
						}
						return _jobs.get(_jobIDs[_currentKey++]).getJson();
					}
				}
				if(_currentKey>=_jobIDs.length){
					_currentKey=0;
				}
				return _jobs.get(_jobIDs[_currentKey++]).getJson();
			}catch(Exception e){
				Logger.severe("{SendToClient - getJob","Error"+e.getLocalizedMessage());
				e.printStackTrace();
				return "error getJob";
			}
		}
	}
	
	//Used to refresh job queue
	public void updateJobs(){
		initJobs();
	}
}