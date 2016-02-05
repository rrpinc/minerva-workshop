var pageContainer;

$(document).ready(function() {

	$("#refreshButton").on("click", refreshData);
	$("#logout").click(logout);

	$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
        localStorage.setItem('activeTab', $(e.target).attr('href'));
    });
    var activeTab = localStorage.getItem('activeTab');
    if(activeTab){
        $('#MyTabs a[href="' + activeTab + '"]').tab('show');
    }  
	
});

function ajaxLogout() {
    var retData = null;
    $.ajax({
        async: false,
        type: 'GET',
        url: 'http://localhost:8080/AtlasServices/Logout',
        success: function(data) {
            retData = data;
        }
    });
    return retData;
}

function logout(){
	var res = window.ajaxLogout();  
	window.location = 'login.html'; 
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

	var RAWstartTime = parseYMDHM($("#startTime").val());
	var startTime = RAWstartTime.getTime();
	var RAWendTime = parseYMDHM($("#endTime").val());
	var endTime = RAWendTime.getTime();
	
	var DELIMITER=1000;
	var scaleXvalues=startTime+":"+endTime+":"+DELIMITER;
	var time;
	var res;
	
	time = {startTime: startTime, endTime: endTime};
	res = window.ajaxGetDetections(time);    
	var NumOfDetections = JSON.parse(res["detectionsArr"]);
	
	//var NumOfDetections=[10, 2, 6];
	
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
        "values": [5, 2, 6],
        "text": "Salmonella",
        "line-color": "#1f78b4",
        "marker": {
          "background-color": "#1f78b4",
          "border-color": "#1f78b4"
        }
      }, {
        "values": [10, 12, 16],
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
