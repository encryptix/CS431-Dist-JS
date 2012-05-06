<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="dist.server.send.SendToClient" session="true"%>
<%@ page import="dist.server.recieve.RecieveResults" session="true"%>

<%@ page import="dist.server.Logger" session="true"%>

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
		Logger.severe("Interface ","client is NULL");
		ex.printStackTrace();	
	}

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
	rr.recieveJobResults(jobID,dataID,result);
	
	String data = SendToClient.getInstance().getData(jobID);
	out.print(data);
}
%>
