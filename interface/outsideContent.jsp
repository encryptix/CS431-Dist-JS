<%@ page import="concepts.server.Test" session="true"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="dist.server.send.SendToClient" session="true"%>
<%@ page import="dist.server.recieve.RecieveResults" session="true"%>

<%@ page import="dist.server.Logger" session="true"%>

<%@ page import="java.util.ArrayList" session="true"%>

<%!
private boolean hasContent(String item){
   	if(item==null || item.length()<=0)
   		return false;
	return true;
}

private boolean isInteger(String item){
	if(hasContent(item)){
		try{
			Integer.parseInt(item);
			return true;
		}catch(Exception e){return false;}
	}
	return false;
}

private String wrongParameters(){
	return "Wrong_Parameters";
}

%>

<%
String getContentFrom = request.getParameter("site");
if(!hasContent(getContentFrom)){
	//setup some sort code
	out.print("Error no site specified");
	return;
}


String actionOnServer = request.getParameter("action");
if(!hasContent(actionOnServer)){
	out.print("Error no action");
	return;
}

if(actionOnServer.equals("getData")){
	String jobID = request.getParameter("job_id");
	
	if(!isInteger(jobID)){
		out.print(wrongParameters()+" GD"+" job id : "+jobID);
		return;
	}
	out.print(SendToClient.getInstance().getData(jobID));
	
}else if(actionOnServer.equals("getJob")){
	SendToClient client=null;
	try{
		client = SendToClient.getInstance();
	}catch(NullPointerException ex){
		ex.printStackTrace();	
	}
	
	if(client==null){
		Logger.debug("","client is NULL");
	}
	Logger.debug("","client is "+client);
	out.print(client.getJob());

}else if(actionOnServer.equals("recieveResult")){
	String jobID = request.getParameter("job_id");
	String result = request.getParameter("result");
	String dataID = request.getParameter("data_id");
	
	if(!isInteger(jobID) || !hasContent(result) || !isInteger(dataID)){
		out.print(wrongParameters()+" RR jobID: "+jobID+ "dataID: "+dataID+"result: "+result);
		return;
	}
	
	RecieveResults rr = new RecieveResults();
	if(jobID.equals("1")){
		rr.recieveJobOne(dataID,result);
	}else{
		Logger.severe("{Interface - recieve result}","unknown_job");
		return;
	}
	
	out.print(SendToClient.getInstance().getData(jobID));
}
%>
