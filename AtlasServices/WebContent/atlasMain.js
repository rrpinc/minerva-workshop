
$(document).ready(function() {

	if (localStorage.getItem('loggedin')) {
		$("#refreshButton").on("click", refreshData);
		$("#logout").click(logout);
		
		localStorage.setItem('toShow', "false");

		$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
			localStorage.setItem('activeTab', $(e.target).attr('href'));
			if (localStorage.getItem('toShow') == "true"){
				showData(startTime, endTime);
			}
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
        url: "GetLocalizations",
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
        url: "GetDetections",
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
	deleteMarkers();
	localStorage.setItem('toShow', "true");
	clearInterval(refreshIntervalId);
	var method = $('input[name=method]:checked').val();
	
	if (method === "interval"){
		var RAWstartTime = (parseYMDHM($("#startTime").val()))
		RAWstanrtTime = RAWstartTime.toUTCString();
		startTime = RAWstartTime.getTime();
		var RAWendTime = parseYMDHM($("#endTime").val());
		endTime = RAWendTime.getTime();
		showData(startTime, endTime);
	}
	else {
		refreshIntervalId = setInterval(function(){
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
function initMap() {

	 if (localStorage.getItem('activeTab') == "#Map" && localStorage.getItem('toShow') == "true"){
		map = new google.maps.Map(document.getElementById('google_map'), {
		      center: {lat: 33.120675660801325, lng: 35.59343085169261},
		      zoom: 12
		   });	 
		localStorage.setItem('map_init', "yes");

	 }   

  }
function componentToHex(c) {
    var hex = c.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
}

function rgbToHex(r, g, b) {
    return "" + componentToHex(r) + componentToHex(g) + componentToHex(b);
}

function showMap(startTime, endTime, time, DELIMITER){
	res = window.ajaxGetCoors(time);    
	var Localizations;
	var Tags;
	var valid = true;
	
	if (res["localizationArr"] == "" || res["localizationArr"] == undefined){
		valid = false;
	}
	else if (res["tags"] == "" || res["tags"]== undefined){
		valid = false;
	}
	else {
		Localizations = $.parseJSON(res["localizationArr"]);		
		Tags = $.parseJSON(res["tags"]);
	}
    
	var len = (endTime - startTime)/(DELIMITER);

	if (valid) {
		for (var tag = 0; tag < Tags.length; tag++) {
		    var j=0;
		    var path =[];
		    var locals = Localizations[tag];
			for (x in locals) {
				local = locals[x];
		        var newMark = {lat: local[1], lng: local[2]};
		        path.push(newMark);
		        var color;
		        if (tag%5 == 0){
			        color = rgbToHex(219,58,58);
		        }
		        if (tag%5 == 1){
			        color = rgbToHex(96,58,219);
		        }
		        if (tag%5 == 2){
			        color = rgbToHex(35,193,53);
		        }
		        if (tag%5 == 3){
			        color = rgbToHex(185,61,199);
		        }
		        if (tag%5 == 4){
			        color = rgbToHex(255,171,42);
		        }
		        initialize(newMark, color, local[0], Tags[tag]);
		        j++;
		    }
			if (tag%5 == 0){
				addPolyline(path, map, rgbToHex(219,58,58), time, Tags[tag]);
	        }
	        if (tag%5 == 1){
				addPolyline(path, map, rgbToHex(96,58,219), time, Tags[tag]);
	        }
	        if (tag%5 == 2){
				addPolyline(path, map, rgbToHex(35,193,53), time, Tags[tag]);
	        }
	        if (tag%5 == 3){
				addPolyline(path, map, rgbToHex(185,61,199), time, Tags[tag]);
	        }
	        if (tag%5 == 4){
				addPolyline(path, map, rgbToHex(255,171,42), time, Tags[tag]);
	        }

		}
	}
	else {
		var newMark = {lat: 33.080527, lng: 35.597305};
		color = rgbToHex(70,80,70);
		initialize(newMark, color, startTime, "0");
	}
}

function showChart(startTime, endTime, time, DELIMITER){
	var res = window.ajaxGetDetections(time);  
	var NumOfDetections;
	var valid = true;
	
	if (res["detectionsArr"] == "" || res["detectionsArr"] == undefined){
		valid = false;
	}
	else {
		NumOfDetections = $.parseJSON(res["detectionsArr"]);
	}
     
	var rows = [];
	var j=0;
	var data;
	for (var i = startTime; i < endTime+1; i=i+DELIMITER) {
		var date = (new Date(i)).toLocaleString();
		if (valid){
			data = NumOfDetections[j];
		}
		else {		
			data = 0;
		}
		rows.push([date, data]);
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
