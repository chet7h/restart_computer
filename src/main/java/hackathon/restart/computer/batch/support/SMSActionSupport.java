package hackathon.restart.computer.batch.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

//Visist http://http://esms.vn/SMSApi/ApiSendSMSNormal for more information about API
//Website: http://esms.vn/
@Component
public class SMSActionSupport {
 
	@Value("${smsSend.APIKey}")
	private String aPIKey;
	
	@Value("${smsSend.SecretKey}")
	private String secretKey;
	@Value("${smsSend.Brandname}")
	private String brandname;

	public String sendSMS(String phone, String message) throws IOException {

		String url = "http://rest.esms.vn/MainService.svc/xml/SendMultipleMessage_V4_get?ApiKey=" 
					+ URLEncoder.encode(aPIKey, "UTF-8") 
					+ "&SecretKey=" + URLEncoder.encode(secretKey, "UTF-8") 
					+ "&SmsType=2&Brandname=" + brandname + "&Phone=" + URLEncoder.encode(phone, "UTF-8") 
					+ "&Content=" + URLEncoder.encode(message, "UTF-8");
		//De dang ky brandname rieng vui long lien he hotline 0901.888.484 hoac nhan vien kinh Doanh cua ban
		URL obj;
		try {
			obj = new URL(url);
		
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//you need to encode ONLY the values of the parameters
	            
			con.setRequestMethod("GET");
	 
			
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			if(responseCode==200)//Đã gọi URL thành công, tuy nhiên bạn phải tự kiểm tra CodeResult xem tin nhắn có gửi thành công không, vì có thể tài khoản bạn không đủ tiền thì sẽ thất bại
			{
				//Check CodeResult from response
			}
			//Đọc Response
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
 
			//print result
			System.out.println(response.toString());
			Document document = loadXMLFromString(response.toString());
			document.getDocumentElement().normalize();
			System.out.println("Root element :" 
		            + document.getDocumentElement().getNodeName());
			Node node = document.getElementsByTagName("CodeResult").item(0);
	        System.out.println("CodeResult: " + node.getTextContent());
	        node = document.getElementsByTagName("SMSID").item(0);
	        if(node != null) {
	        	System.out.println("SMSID: " + node.getTextContent());
	        } else {
	        	node = document.getElementsByTagName("ErrorMessage").item(0);
	        	System.out.println("ErrorMessage: " + node.getTextContent());
	        }
		//document.getElementsByTagName("CountRegenerate").item(0).va
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "SUCCESS";

	}
	
	public Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
}
