package webRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WebRequest {

	/**
	 * Class name as a string
	 */
	private static final String CLASSNAME = WebRequest.class.getName();
	
    private static final String APPLICATION_JSON = "application/json";
    
    private static final String CONTENT_TYPE = "Content-Type";
    
    private  static String portalJSON = "https://my-json-server.typicode.com/hectormata/JSON_Practice/books";
    
    private final String USER_AGENT = "Mozilla/5.0";
	
	private static String DATE_FORMAT = "MM/dd/yyyy";
	
	private static Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
	
	private static Map<String, Object> jsonParams = new HashMap<String, Object>();
	
  private static void collectMonitoringDataFromPortal() {
	  
	  try {
		  
		  RequestConfig portalRequestConfig = RequestConfig.custom().setConnectTimeout(10*300).setSocketTimeout(10*300).build();
		  HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(portalRequestConfig).build();
		  HttpPost post = new HttpPost(portalJSON);
		  jsonParams.put("", "");
		  jsonParams.put("", "");
		  jsonParams.put("", "");
		  jsonParams.put("", "");
		  jsonParams.put("", "");
		  jsonParams.put("", "");
		  jsonParams.put("", "");
		  
		  String jsonMapParam = new Gson().toJson(jsonParams);
		  StringEntity postingParams = new StringEntity(jsonMapParam);
		  post.setEntity(postingParams);
		  post.setHeader(CONTENT_TYPE, APPLICATION_JSON);
		  HttpResponse response = httpClient.execute(post);
		  
		  // Test that we connected to the portal
		  if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		  
			  // Process the response
			  BufferedReader bufferedReader = null;
			  StringBuffer res = new StringBuffer();
			  bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		  
			  try {
			  
				  String line = null;
				  while ((line = bufferedReader.readLine()) != null) {
					  res.append(line);
				  }
			  
				  bufferedReader.close();

			  } catch (Exception e) {
				  
				  System.err.println("Caugth exception while trying to grab data" + e);
			  }
		  }
		  
		  else {
			  
			  System.err.println("Status : - " + response.getStatusLine());
			  System.err.println("Code : -	" + response.getStatusLine().getStatusCode());
			  throw new Exception("Invalid response - " + response.getStatusLine().getStatusCode() + " - " + response.getStatusLine().getReasonPhrase());
			  
		  }
	  } catch (Exception e) {
		  
		  System.err.println("Caught exception while trying to connect to the portal: " + e);
	  }
	  
	  
  }
  
  public String sendJSONData(String message) throws IOException {

      //creating map object to create JSON object from it
	  Map< String, Object >jsonValues = new HashMap< String, Object >();
	  jsonValues.put("param1", message);
	  jsonValues.put("param2", message);
	  jsonValues.put("param3", message);
	  JSONObject myFirstReponse = new JSONObject(jsonValues);
	  HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();
	  HttpPost post = new HttpPost(portalJSON);
	  post.setHeader(CONTENT_TYPE, APPLICATION_JSON);
	  post.setHeader("headerValue", "HeaderInformation");
	  //setting json object to post request.
	  StringEntity entity = new StringEntity(myFirstReponse.toString(), "UTF8");
	  entity.setContentType(new BasicHeader(CONTENT_TYPE, "application/json"));
	  post.setEntity(entity);
	  //this is your response:
	  HttpResponse response = httpClient.execute(post);
	  System.out.println("Response: " + response.getStatusLine());
      
	  return response.getStatusLine().toString();
  }
  	
	// HTTP POST request
	@SuppressWarnings("unused")
	private void sendPost() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		System.out.println(response.toString());
	}
	
	private static String sendPostRequest(String requestUrl, String payload) {
		
	    try {
	        URL url = new URL(requestUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(payload);
	        writer.close();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuffer jsonString = new StringBuffer();
	        String line;
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
	        br.close();
	        connection.disconnect();
	        return jsonString.toString();
	    } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	    }
	}
	
	public static void main(String[] args) {
		
		// collectMonitoringDataFromPortal();
		String payload="{\"jsonrpc\":\"2.0\",\"method\":\"changeDetail\",\"params\":[{\"id\":11376}],\"id\":2}";
		String requestUrl="https://git.eclipse.org/r/gerrit/rpc/ChangeDetailService";
		sendPostRequest(requestUrl, payload);
	}
}
