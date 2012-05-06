package dist.server.datastructure;

import java.util.ArrayList;

import dist.server.Functions;
import dist.server.Logger;
import dist.server.ServerGlobals;

public class Jobs {	
	private String _id;
	private String _job;
	private ArrayList<Data> _data;
	
	public Jobs(String id, String job){
		_id = id;
		_job = job;
		_data = new ArrayList<Data>(); 
	}
	
	public void appendToArrayList(ArrayList<Data> data){
		Logger.debug("{Jobs - addDataToArrayList 1}", "ID: "+getID()+" Size: "+sizeData());
		_data.addAll(data);
		Logger.debug("{Jobs - addDataToArrayList 2}", "ID: "+getID()+" Size: "+sizeData());
	}
	
	public int sizeData(){
		return _data.size();
	}
	
	public String getData(){
		Logger.debug("{Jobs - getData}", "JobID: "+getID()+" ArraySize: "+sizeData());
		try{
			Data d = _data.remove(0);
			Logger.debug("{Jobs - getData}", "WorkID: "+d.getDataID()+" ArraySize: "+sizeData());
			return d.getJson();
		}catch(IndexOutOfBoundsException e){
			Logger.debug("{Jobs - getData}", "JobID: "+getID()+" No more data: "+e.getLocalizedMessage());
			return "No_More";
		}
	}
	
	public String getID(){
		return _id;
	}
	
	public String getJob(){
		return _job;
	}
	
	public String getJson(){
		return "{"
		+Functions.createJSONEntry("jobID",this.getID())+ServerGlobals.comma
		+Functions.createJSONEntry("job",this.getJob())
		+"}";
	}
}
