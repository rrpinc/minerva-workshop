$(document).ready(function() {

	$('#login').on('click',checkLogin);
	
});

function ajaxLogin(credentials) {
    var retData = null;
    $.ajax({
        async: false,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(credentials),
        url: 'Login',
        success: function(data) {
            retData = data;
        }
    });
    return retData;
}

function checkLogin() {
    var credentials = {email: $("#email").val(), pass: $("#pwd").val()};
	var res = window.ajaxLogin(credentials);
    if (res["userlogged"] !== "true") {
        alert("Failed to log in");
        $("#email").val("");
		$("#pwd").val("");
    } else {
        window.location = 'main.html'; 
    }
}
	

