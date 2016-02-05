<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
    <title>Login</title>
        <link rel="stylesheet" href="css/style.css">
  </head>

  <body>

    <div class="container">
  <div id="login-form">
    <h3>Login</h3>

    <fieldset>

      <form action="javascript:void(0);" method="get">

        <input type="email" name="email" required value="Email" onBlur="if(this.value=='')this.value='Email'" onFocus="if(this.value=='Email')this.value='' "> <!-- JS because of IE support; better: placeholder="Email" -->

        <input type="password" name="pwd" required value="Password" onBlur="if(this.value=='')this.value='Password'" onFocus="if(this.value=='Password')this.value='' "> <!-- JS because of IE support; better: placeholder="Password" -->

        <input type="submit" value="Login">

      </form>

    </fieldset>

  </div> <!-- end login-form -->

</div>
    
    
    
    
    
  </body>
</html>
