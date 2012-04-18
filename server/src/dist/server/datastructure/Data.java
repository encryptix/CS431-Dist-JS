package dist.server.datastructure;

import dist.server.Functions;
import dist.server.ServerGlobals;

public class Data {
	private String _data;
	private String _dataID;
	private String _jobID;
	
	public Data(String data,String jobID,String dataID){
		_data = data;
		_jobID = jobID;
		_dataID = dataID;
	}
	
	public String getData(){
		return _data;
	}
	
	public String getDataID(){
		return _dataID;
	}
	
	public String getJobID(){
		return _jobID;
	}
	
	public String getJson(){
		return "{"
		+Functions.createJSONEntry("dataID",this.getDataID())+ServerGlobals.comma
		+Functions.createJSONEntry("data",this.getData())
		+"}";
	}
}
