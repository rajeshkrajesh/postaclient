package com.rixyncs.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.common.base.Strings;
import com.rixyncs.bean.Account;

/*Convert Account Xml data of Zoho into ArrayList*/


public class AccountDomParser {
	
	public ArrayList<Account> accountList = new ArrayList<>();

	/*public static void main(String argv[]) {
		
		
		accountDomParser accountDomParser = new accountDomParser();
		
		StringBuffer xmlString = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		xmlString.append("<response uri=\"/crm/private/xml/Accounts/searchRecords\">");
		xmlString.append("<result>");
		xmlString.append("<accounts>");
		xmlString.append("<row no=\"1\">");
		xmlString.append("<FL val=\"accountID\">2440108000001077001</FL>");
		xmlString.append("<FL val=\"ACCOUNTID\">null</FL>");
		xmlString.append("<FL val=\"Account Name\"><![CDATA[Bishnu]]></FL>");
		xmlString.append("<FL val=\"Email\"><![CDATA[null]]></FL>");
		xmlString.append("<FL val=\"Salesperson Code\"><![CDATA[null]]></FL>");
		xmlString.append("</row>");
		xmlString.append("</accounts>");
		xmlString.append("</result>");
		xmlString.append("</response>");
		
		ArrayList<account> listOfaccount= accountDomParser.pasrse(xmlString.toString());
		
		System.out.println("size = "+listOfaccount.size());
		System.out.println("Account Name retrive===="+listOfaccount.get(0).getAccountname());
		
		
		
	}*/
	
	/* 
	 * Function to parse a List and convert it into an XML for updating further in Zoho
	 * The update in ERP field is marked true over here
	 */
	public StringBuffer parseListToXml(ArrayList<Account> acctList){
		
		StringBuffer xmlString = new StringBuffer("");
		//StringBuffer xmlString = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		//xmlString.append("<response uri=\"/crm/private/xml/Accounts/updateRecords\">");
		xmlString.append("<Accounts>");
		for(Integer i=0; i<acctList.size(); i++ ){
			
			//xmlString.append("<row no=\"1\">");
			xmlString.append("<row no=" + "\"" + i + "\"" + ">");
			//xmlString.append("<FL val=\"accountID\">accList[i].Id</FL>"); // check the field name 
			xmlString.append("<FL val=\"Id\">"); // check the field name 
			xmlString.append(acctList.get(i).getAccountId());
			xmlString.append("</FL>");
			xmlString.append("<FL val=\"Updated in ERP\">False</FL>"); // check the field name for update in erp
			xmlString.append("</row>");
			//System.out.println("<row no=" + "" + i + "" + ">");
		}
		xmlString.append("</Accounts>");
				
		return xmlString;
		
	}
	
	public ArrayList<Account> pasrse(String xmlString)
	{

		
	try {

			
			
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlString));

			// Create DocumentBuilder instance to parse the xml file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);

			doc.getDocumentElement().normalize();

			// System.out.print("Root element: ");
			// System.out.println(doc.getDocumentElement().getNodeName());

			// Get the list of data under row Element.
			NodeList nList = doc.getElementsByTagName("row");
			System.out.println("----------------------------");
			for (int i = 0; i < nList.getLength(); i++) {// Loop for row element
				System.out.println("**************Loop" + i + "******************");

				Account account= new Account();
				
				Node nNode = nList.item(i);

				System.out.println("\nCurrent Element :");
				System.out.print(nNode.getNodeName());
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element eElement = (Element) nNode;
					
					System.out.println("row : " + eElement.getAttribute("no"));
				
					
					// List of element with name Element 
					NodeList flList = eElement.getElementsByTagName("FL");
					
					for (int count = 0; count < flList.getLength(); count++) 
					{ //loop for FL element
						
						Node node1 = flList.item(count);
						if (node1.getNodeType() == node1.ELEMENT_NODE) 
						{
							
							Element fl = (Element) node1;
						String a=fl.getAttribute("val");
						String b=fl.getTextContent();
						System.out.print(a);
						System.out.println("      "+b);
							//System.out.print("fl attribute " + fl.getAttribute("val") + " AND  ");
							//System.out.println("fl text : " + fl.getTextContent());
							
							//setting AccountName
							if(fl.getAttribute("val").equals("Account Name")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setAccountName(fl.getTextContent());
								}
							}
							
							//setting Landmark
						if(fl.getAttribute("val").equals("Area details")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setLandmark(fl.getTextContent());
								}
							}
							
							//setting Whatsapp No
							if(fl.getAttribute("val").equals("Whatsapp No")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setWhatsappNo(fl.getTextContent());
								}
							}
							
							//setting Account Type Code
							if(fl.getAttribute("val").equals("Account Type Code")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setAccountTypeCode(fl.getTextContent());
								}
							}
							
							
							//setting AreaDetails
							if(fl.getAttribute("val").equals("AREA CODE")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setAreaCode(fl.getTextContent());
								}
							}
							
							//setting Billing City
							if(fl.getAttribute("val").equals("City Code")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setBillingCity(fl.getTextContent());
								}
							}
							
							
							//setting Billing Country
							if(fl.getAttribute("val").equals("Country Code")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setBillingCountry(fl.getTextContent());
								}
							}
							
							
							//setting Billing State
							if(fl.getAttribute("val").equals("Province Code")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setBillingState(fl.getTextContent());
								}
							}
							
							
							//setting Station Code Upd
							if(fl.getAttribute("val").equals("Station Code Upd")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setStationCodeUpd(fl.getTextContent());
								}
							}
							
							
							//setting Sector Code
							if(fl.getAttribute("val").equals("Sector Code")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setSectorCode(fl.getTextContent());
								}
							}
							
							
							//setting Fax
							if(fl.getAttribute("val").equals("Fax")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setFax(fl.getTextContent());
								}
							}
							
							
							//setting Phone
							if(fl.getAttribute("val").equals("Phone")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setPhone(fl.getTextContent());
								}
							}
							
							//setting Customer Status
							if(fl.getAttribute("val").equals("Customer Status")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setCustomerStatus(fl.getTextContent());
								}
							}
						
							
							//setting Billing Code
							if(fl.getAttribute("val").equals("Billing Code")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setBillingCode(fl.getTextContent());
								}
							}
							
						
							//setting Opening balance
							if(fl.getAttribute("val").equals("Opening balance")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setOpeningBalance(Double.parseDouble(fl.getTextContent()));
								}
							}
							
							//setting Paci Number
							if(fl.getAttribute("val").equals("Paci Number")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setPaciNumber(fl.getTextContent());
								}
							}
							
							
							//setting EnTity Code Upd
							if(fl.getAttribute("val").equals("EnTity Code Upd")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setEnTityCodeUpd(fl.getTextContent());
								}
							}
							
							
							//setting Created By
							if(fl.getAttribute("val").equals("Created By")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setCreatedBy(fl.getTextContent());
								}
							}
							
							//setting Created Time
							if(fl.getAttribute("val").equals("Created Time")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setCreatedTime(fl.getTextContent());
								}
							}
							
							
							//setting Modified By
							if(fl.getAttribute("val").equals("Modified By")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setModifiedBy(fl.getTextContent());
								}
							}
							if(fl.getAttribute("val").equals("ACCOUNTID")){
								if (!"null".equals(fl.getTextContent()) && !Strings.isNullOrEmpty(fl.getTextContent())) {
									account.setAccountId(fl.getTextContent());
								}
							}
	
							

							
							
							
						}//End IF
						
						
						
					}//End 2nd Loop
				}
				
				//Adding account instance to accountList
				accountList.add(account);
				
				
				
				
			  //Database conection to input data to sqlplus     
			

				 
		          ResultSet rs = null;
		          PreparedStatement ps = null;
		          Connection con=null;
		        
		           try {
		        	  	
		        	  	Class.forName("oracle.jdbc.driver.OracleDriver");
		             
		        	  	DriverManager.registerDriver( new oracle.jdbc.driver.OracleDriver());
		        	  	 con = DriverManager.getConnection("jdbc:oracle:thin://localhost:1521/account1:myoracle","scott","tiger");
		        	 //my new code
		        	  	
		        	  	
		        	  	String sql="select * from account1 where accountid="+account.getAccountId();
		        	  	Statement st = con.createStatement();
		        	  	
		        	  	  rs = st.executeQuery(sql);
		        	  	if (!rs.next())
		        	      {
		        	  		
		        	  		 String sql1="insert into account1 values(?,?,?,?,?) ";
				        	  ps = con.prepareStatement(sql1);
				        	  ps.setString(1, account.getAccountId());
				        	  ps.setString(2, account.getAccountName());
				        	  ps.setString(3, account.getPhone());
				        	  ps.setString(4, account.getFax());
				        	  ps.setString(5, account.getBillingCode());
				    	      ps.execute();
				    	      System.out.println("Inserted successfully");
		        	      }
		        	  	else
		        	  	{
		        	  		 String updateQuery = "update account1 set accountname=?, phone=?, fax=?,billingcode=? where accountid='"
		        	                    + account.getAccountId() + "'";
		        	  		 
		        	  		PreparedStatement ps1 = con.prepareStatement(updateQuery);
		                   // ps1.setString(1, account.getAccountId());
		                    ps1.setString(1, account.getAccountName());
		                    ps1.setString(2, account.getPhone());
		                    ps1.setString(3, account.getFax());
		                    ps1.setString(4, account.getBillingCode());
		                    ps1.executeUpdate();    
		                    System.out.println("updated successfully");

		        	  	}
		        	  	  
		        	  
		        	  	st.close();
		        	    rs.close();
		        	    con.close();
		              }
		           catch (Exception e)
		          		{
		        	   
		        	   		e.printStackTrace();
		        	   		throw e;
		          		} 
		         

finally{ 
		
	   con.close();		
}
		   //database
		          
    	      
			}	
			
			
			//End of Fist Loop
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return accountList;

	}
}

