var loggedin;

$(document).ready(function() {

//TODO: return to if (loggedin)
	if (1==1) {
		$("#refreshButton").on("click", refreshData);
		$("#logout").click(logout);

		$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
			localStorage.setItem('activeTab', $(e.target).attr('href'));
		});
		var activeTab = localStorage.getItem('activeTab');
		if(activeTab){
			$('#MyTabs a[href="' + activeTab + '"]').tab('show');
		}  
	}
	else {
		window.location = "http://localhost:8080/AtlasServices/login.html"; 
	}
});


function logout(){
	loggedin=false;
	window.location = "http://localhost:8080/AtlasServices/login.html"; 
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
	var scaleXvalues=startTime+":"+endTime+":"+DELIMITER;
	var time;
	var res;
	
	time = {startTime: startTime, endTime: endTime};
	res = window.ajaxGetDetections(time);    
	var NumOfDetections = res["detectionsArr"];
	
	//var NumOfDetections=[0, 0, 0, 0, 0, 0, 0, 0, 0, startTime%900, startTime%800, startTime%900, startTime%900, startTime%990, 0];
	
	zingchart.THEME = "classic";
	var myConfig = {
      "background-color": "white",
      "type": "line",
      "title": {
        "text": "Atlas Monitor",
        "color": "#333",
        "background-color": "white",
        "width": "60%",
        "text-align": "left",
      },
      "subtitle": {
        "text": "Toggle a data item to remove the series and adjust the scale.",
        "text-align": "left",
        "width": "60%"
      },
      "legend": {
        "layout": "x1",
        "margin-top": "5%",
        "border-width": "0",
        "shadow": false,
        "marker": {
          "cursor": "hand",
          "border-width": "0"
        },
        "background-color": "white",
        "item": {
          "cursor": "hand"
        },
        "toggle-action": "remove"
      },
      "scaleX": {
        "values": scaleXvalues,
        "max-items": 8
      },
      "scaleY": {
        "line-color": "#333"
      },
      "tooltip": {
        "text": "%t: %v outbreaks in %k"
      },
      "plot": {
        "line-width": 3,
        "marker": {
          "size": 2
        },
        "selection-mode": "multiple",
        "background-mode": "graph",
        "selected-state": {
          "line-width": 4
        },
        "background-state": {
          "line-color": "#eee",
          "marker": {
            "background-color": "none"
          }
        }
      },
      "plotarea": {
        "margin": "15% 25% 10% 7%"
      },
      "series": [{
        "values": NumOfDetections,
        "text": "NumOfDetections",
        "line-color": "#a6cee3",
        "marker": {
          "background-color": "#a6cee3",
          "border-color": "#a6cee3"
        }
      }, {
        "values": [148, 137, 149, 134, 132, 136, 141, 115, 120, 146, 117, 118, 132, 114, 116],
        "text": "Salmonella",
        "line-color": "#1f78b4",
        "marker": {
          "background-color": "#1f78b4",
          "border-color": "#1f78b4"
        }
      }, {
        "values": [27, 53, 28, 11, 32, 38, 35, 33, 33, 13, 6, 4, 2, 17, 13],
        "text": "Other"
      }]
    };

    zingchart.render({
      id: 'Monitor',
      data: myConfig,
      height: 400,
      width: 1000
    });
	
	
}
