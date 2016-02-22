package atlasWebServices;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.JsonObject;

import atlasService.LoginReader;
import sun.misc.BASE64Decoder;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private LoginReader loginReader = new LoginReader();

	public static final int HTTP_STATUS_OK = 200;
	public static final int HTTP_STATUS_CREATED = 201;
	public static final int HTTP_STATUS_ERROR = 500;
    private QueryStringParser paramParser;
    

    public Login() {
        super();
    }

	
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//    	request.getRequestDispatcher("/WebContent/WEB-INF/login/Login.jsp").forward(request, response);
	    	
    	paramParser = new QueryStringParser(request);
    	String email = (String) paramParser.GetValueOrNull("email");
    	
    	String encrypted  = (String) paramParser.GetValueOrNull("pass");
    	String key = (String) paramParser.GetValueOrNull("key");
    	String salt = (String) paramParser.GetValueOrNull("salt");
    	String iv = (String) paramParser.GetValueOrNull("iv");
    	/*
    	String password = (String) paramParser.GetValueOrNull("pass");
    	*/
    	
//    	String jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");
//		JsonParser parser = new JsonParser();
//		JsonObject requestJson = (JsonObject)parser.parse(jsonString);
//		String email = (String) requestJson.get("email").getAsString();
//		String password = (String) requestJson.get("pass").getAsString();
    	
    	
    	 String password = null;
    	 
		try {
			password = decryptAESEncryptWithSaltAndIV(encrypted, key, salt, iv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (email == null || password == null) {
			response.setStatus(HTTP_STATUS_ERROR);
			return;
		}
		
		
		boolean logged = loginReader.loginToWebsite(email, password);
		
		
		if (logged) {
			request.getSession().setAttribute("userid", email);
		}
		else {
			request.getSession().setAttribute("userid", null);
		}
		JsonObject responseJson = new JsonObject();
		responseJson.addProperty("userlogged", logged ? "true" : "false");
		response.setStatus(HTTP_STATUS_OK);
		response.setContentType("application/json");
		response.getOutputStream().println(responseJson.toString());	
    
    }
    
    
    /**
     * Hex string to byte array.
     *
     * @param s the s
     * @return the byte[]
     */
    public static byte [] hexStringToByteArray ( String s )
    {
        int len = s.length ();
        byte [] data = new byte[len / 2];
        for ( int i = 0; i < len; i += 2 )
        {
            data[i / 2] = (byte) ( ( Character.digit ( s.charAt ( i ), 16 ) << 4 ) + Character.digit ( s.charAt ( i + 1 ), 16 ) );
        }
        return data;
    }

    
    /**
     * Generate key from password with salt.
     *
     * @param password the password
     * @param saltBytes the salt bytes
     * @return the secret key
     * @throws GeneralSecurityException the general security exception
     */
    public static SecretKey generateKeyFromPasswordWithSalt ( String password, byte [] saltBytes ) throws GeneralSecurityException
    {
        KeySpec keySpec = new PBEKeySpec ( password.toCharArray (), saltBytes, 100, 128 );
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance ( "PBKDF2WithHmacSHA1" );
        SecretKey secretKey = keyFactory.generateSecret ( keySpec );

        return new SecretKeySpec ( secretKey.getEncoded (), "AES" );
    }

    /**
     * Decrypt aes encrypt with salt and iv.
     *
     * @param encryptedData the encrypted data
     * @param key the key
     * @param salt the salt
     * @param iv the iv
     * @return the string
     * @throws Exception the exception
     */
    public static String decryptAESEncryptWithSaltAndIV ( String encryptedData, String key, String salt, String iv ) throws Exception
    {

        byte [] saltBytes = hexStringToByteArray ( salt );
        byte [] ivBytes = hexStringToByteArray ( iv );
        IvParameterSpec ivParameterSpec = new IvParameterSpec ( ivBytes );
        SecretKeySpec sKey = (SecretKeySpec) generateKeyFromPasswordWithSalt ( key, saltBytes );

        Cipher c = Cipher.getInstance ( "AES/CBC/PKCS5Padding" );
        c.init ( Cipher.DECRYPT_MODE, sKey, ivParameterSpec );
        byte [] decordedValue = new BASE64Decoder ().decodeBuffer ( encryptedData );
        byte [] decValue = c.doFinal ( decordedValue );
        String decryptedValue = new String ( decValue );

        return decryptedValue;
    }


}
