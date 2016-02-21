
$(document).ready(function() {

	if (localStorage.getItem('loggedin')) {
		$("#refreshButton").on("click", refreshData);
		$("#logout").click(logout);

		$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
			localStorage.setItem('activeTab', $(e.target).attr('href'));
		});
		document.getElementById('refreshButton').disabled=true;
		var activeTab = localStorage.getItem('activeTab');
		if(activeTab){
			$('#MyTabs a[href="' + activeTab + '"]').tab('show');
		}  
	}
	else {
		window.location = "login.html"; 
	}
});

function logout(){
	loggedin=false;
	window.location = "login.html"; 
}

function ajaxGetCoors(time) {
    var retData = null;
    $.ajax({
        async: false,
        type: 'GET',
        data: time,
        url: "http://localhost:8080/AtlasServices/GetLocalizations",
        success: function(data) {
            retData = data;
        }
    });
    return retData;
}


function ajaxGetDetections(time) {
    var retData = null;
    $.ajax({
        async: false,
        type: 'GET',
        data: time,
        url: "http://localhost:8080/AtlasServices/GetDetections",
        success: function(data) {
            retData = data;
        }
    });
    return retData;
}
function parseYMDHM(s) {
  var b = s.split(/\D+/);
  return new Date(b[0], --b[1], b[2], b[3], b[4], b[5]||0, b[6]||0);
}
function refreshData(){
					   
	var method = $('input[name=method]:checked').val();
	
	if (method === "interval"){
		var RAWstartTime = parseYMDHM($("#startTime").val());
		startTime = RAWstartTime.getTime();
		var RAWendTime = parseYMDHM($("#endTime").val());
		endTime = RAWendTime.getTime();
		showData(startTime, endTime);
	}
	else {
		setInterval(function(){
			var today = new Date();
			startTime = today.getTime() - ($("#minutes").val())*60000;
			endTime = today.getTime();
			showData(startTime, endTime);
			}, 1000);
	}
	
}
function showData(startTime, endTime){

	var DELIMITER=1000;
	var time = {startTime: startTime, endTime: endTime};
	var activeTab = localStorage.getItem('activeTab');
	
	if (activeTab == "#Monitor"){
		showChart(startTime, endTime, time, DELIMITER);

	}
	else if (activeTab == "#Map"){
	    showMap(startTime, endTime, time, DELIMITER);
	}  
}

function showMap(startTime, endTime, time, DELIMITER){
	res = window.ajaxGetCoors(time);    
	var Localizations = res["localizationsArr"];
    //var Localizations = [[0,'33.120675660801325','35.59343085169261'],[1,'33.10145464530716','35.62557091961993']];
    
    var map_rows = [];
	var j=0;
	for (var i = startTime; i < endTime+1; i=i+60*DELIMITER) {
		var date = (new Date(i)).toLocaleString();
		map_rows.push([date,Localizations[j][1], Localizations[j][2]]);
		j++;
	}

    google.charts.setOnLoadCallback(drawMap);

    function drawMap() {
	    var data = new google.visualization.DataTable();
		data.addColumn('string', 'dateTime');
	  	data.addColumn('string', 'latitude');
	  	data.addColumn('string', 'longtitude')
		data.addRows(map_rows);
	
	    options = { 
			zoomLevel :8,
	    	'width':700,
			'height':400,
			chartArea: { width: 380 },
	    	showTip: true };
	
	    var map = new google.visualization.Map(document.getElementById('Map'), {
	        center: {lat: -34.397, lng: 150.644},
	        zoom: 8
	      });
	      
	    map.draw(data, options);
    }
}

function showChart(startTime, endTime, time, DELIMITER){
	var res = window.ajaxGetDetections(time);    
	var NumOfDetections = res["detectionsArr"];	
      
	var rows = [];
	var j=0;
	for (var i = startTime; i < endTime+1; i=i+DELIMITER) {
		var date = (new Date(i)).toLocaleString();
		rows.push([date, NumOfDetections[j]]);
		j++;
	}
	
    google.charts.setOnLoadCallback(drawChart); 
    function drawChart() {
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Date');
		data.addColumn('number', 'Detections');
		data.addRows(rows);

		var options = {
			'width':1000,
			'height':450,
			chartArea: { width: 380 },
		};
	
	    var chart = new google.visualization.LineChart(document.getElementById('Monitor'));
	    chart.draw(data, options);
    }
}
