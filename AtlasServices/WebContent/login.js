
//$(document).ready(function() {
//
//	$('#login').on('click',checkLogin);
//	
//});


function ajaxLogin(credentials) {
    var retData = null;
    $.ajax({
        async: false,
        type: 'GET',
        data: credentials,
        url: "http://localhost:8080/AtlasServices/Login",
        success: function(data) {
            retData = data;
        }
    });
    return retData;
}

function checkLogin(encrypted, key, s, i) {
//function checkLogin() {
    var credentials = {email: $("#email").val(), pass: encrypted, key: key, salt: s, iv: i };
	//var credentials = {email: $("#email").val(), pass: $("#pwd").val()};

	var res = window.ajaxLogin(credentials);
    if (res["userlogged"] !== "true") {
        alert("Failed to log in");
        $("#email").val("");
		$("#pwd").val("");
		localStorage.setItem('loggedin', false);
    } else {
    	localStorage.setItem('loggedin', true);
        window.location = "http://localhost:8080/AtlasServices/main.html"; 
    }
}
	

