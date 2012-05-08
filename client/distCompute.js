var site="site=http://localhost:8080/concepts";
var job;
var jobID;
var dataID;
var data;
var result;
var timeDebug = "1000";
var runJobDebugTimer="500";
var stopped = true;

function log(fn){
	console.log(fn);
	//lots of checks here if needed
}

function debugPause(){
	if(stopped){
		document.getElementById("pauseButton").value="Pause";
		stopped=false;
		if(job!=null)
			getData();
		else
			getJob();
	}else{
		document.getElementById("pauseButton").value="Resume";
		stopped=true;
	}
}
Result:3
function setTimeoutVariable(){
	timeDebug = document.getElementById("amount").value;
	log("setTimeout: timeDebug is now : "+timeDebug);
}

function recieveJob(fnJSON){
	log("Recieve Job: "+fnJSON);
	if(fnJSON=="no_job"){
		log("no more jobs");
	}else{
		var jsonObj = JSON.parse(fnJSON);
		if(jsonObj=="2"){
			//error stuff here
		}
		jobID = jsonObj.jobID;
		job = jsonObj.job;
		setTimeout ( getData, 100 );
	}
}

function recieveData(dataJSON){
	if(!stopped){
		log("Recieve Data: "+dataJSON);
		
		if(dataJSON=="No_More"){
			log("no more data");
			setTimeout ( getJob, timeDebug );
			
		}else{
			var jsonObj = JSON.parse(dataJSON);
			data = jsonObj.data;
			log(data);
			dataID = jsonObj.dataID;
			setTimeout ( runJob, timeDebug );
		}
	}
}

function getJob(){
	stopped = false;
	document.getElementById("pauseButton").value="Pause";
	
	log("Get Job");
	ajax(recieveJob,"action=getJob");
}

function getData(){
	log("Get Data");
	ajax(recieveData,"action=getData&job_id="+jobID);
}

function runJob(){
	log("Run Job");

	if(job!=null){
		if(data!=null){
			//Run the Job
			var runnableFunc = eval( '(' + job + ')');
			result = runnableFunc(data);
			sendResult();
		}else{
			getData();
		}
	}else{
		getJob();
	}
}

function sendResult(){
	log("Send Result");
	log("result is : "+result+" STOPPED");
	ajax(recieveData,"action=recieveResult&job_id="+jobID+"&data_id="+dataID+"&result="+result);
}

function ajax(callbackfun,params){
	var xmlHttp;
	var action = site+"&"+params;
	if(window.XMLHttpRequest){
		xmlHttp = new XMLHttpRequest();
	}else{
		//Its IE
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlHttp.onreadystatechange=function(){
		if(xmlHttp.readyState == 4){
			if(xmlHttp.status==200){
				callbackfun(xmlHttp.responseText);
			}else{
				log("ERROR: "+xmlHttp.status+" RS: "+xmlHttp.readyState);
			}
		}
	}
	xmlHttp.open("POST","interface.jsp",true);
	xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlHttp.send(action);
}
