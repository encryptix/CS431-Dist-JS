<%@ page import="concepts.server.Test" session="true"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="dist.server.admin.EditTest" session="true"%>
<%@ page import="dist.server.datastructure.TestEntry" session="true"%>
<%@ page import="dist.server.send.SendJS" session="true"%>

<%@ page import="java.util.ArrayList" session="true"%>

<%!
private boolean hasContent(String item){
   	if(item==null || item.length()<=0)
   		return false;
	return true;
}

private String generateEditableLine(String id, String line){
	line = line.replaceAll("\"","\\\"");
	String form = "<form name=\"input\" action=\"admin.jsp\" method=\"post\">";
	form+="JS: <input type=\"text\" name=\"test\" value=\""+line+"\"/>";
	form+="<input type=\"hidden\" name=\"id\" value=\""+id+"\"/>";

	//<input type="submit" value="Submit" />
	//<input type="hidden" value="add" />
	form+="</form>";
	return form;
}
%>

<%

%>

<html>
<head>
<title>Test Admin</title>
</head>
<body>

<form name="input" action="admin.jsp" method="post">
Line: <input type="text" name="user" />
<input type="submit" value="Submit" />
<input type="hidden" value="add" />
</form>
<br />
<br />


<%

EditTest test = new EditTest();
ArrayList<TestEntry> entries = test.getEntries();

for(int i=0;i<entries.size();i++){
	TestEntry current = entries.get(i);
	out.println(generateEditableLine(current.getID(),current.getJS()));
}

%>


</body>

</html>