package com.rixyncs.fetchrecord;

import java.sql.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.datacontract.schemas._2004._07.Client.PostaClient;
import org.datacontract.schemas._2004._07.Tps.AccountInfo;
import org.datacontract.schemas._2004._07.Tps.AlertEventCode;
import org.datacontract.schemas._2004._07.Tps.USERINFO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.rixyncs.bean.Account;
import com.rixyncs.bean.Contact;
//import com.rixyncs.bean.ConvertXml;
import com.rixyncs.bean.CreateMasterAccount;
import com.rixyncs.bean.CsvToXml;
import com.rixyncs.parser.AccountDomParser;
import com.rixyncs.parser.ContactDomParser;


/*
 * Main class used to pull data from Zoho CRM and push in to postaplus oracle database using
 * postaplus soap webservice
 */

public class DataPush {
	
	
	
	
	
	
	
	/*
	 * Method used to fetch all the account data from Zoho CRM based on the criteria

	 * Send To ERP = true
	 */
	
		   static String TodayDate=getTodaydate().toString();
			public static String getTodaydate()
			{
			DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			return dateFormat.format(date);
			}
		   static Properties prop = new Properties();
	

	public String getAccountDetails() 
	{	
		String accountDetails = null;

		String authtoken = prop.getProperty("authtoken");
		
		String scope = "crmapi";

		//String selectColumns = "Accounts(Account Name,)";
		String selectColumns = "Accounts(Account Name,Updated in ERP,Area details,Station Code Upd,City Code,Country Code,AREA CODE,Billing Code,Province Code,Paci Number,Phone,Email,Fax,Salesman Code,Customer Status,Account Type Code,Sector Code,Opening Balance)";
		String newFormat = "2";
		String fromIndex = "1";
		String toIndex = "20";
		String wfTrigger="true";
		
		// String selectColumns = "All";
		//String AccountName="pavithra";
		// String criteria = "((Send to ERP:true)AND(Trigger Date:"+TodayDate+")AND(Updated in ERP:False))";
		 //System.out.println(criteria);
		//String criteria = "((Trigger Date:2017-11-13)AND(Updated in ERP:False))";
		//String criteria2="(Updated in ERP:False)";

		String targetURL = prop.getProperty("targetURL");
		System.out.println(targetURL);

		String paramname = "content";
		PostMethod post = new PostMethod(targetURL);
		post.setParameter("authtoken", authtoken);
		post.setParameter("scope", scope);
		post.setParameter("newFormat", newFormat);
		post.setParameter("selectColumns", selectColumns);
		post.setParameter("fromIndex", fromIndex);
		post.setParameter("toIndex", toIndex);
		//post.setParameter("targetURL", targetURL);
		post.setParameter("wfTrigger", wfTrigger);
	//	post.setParameter("xmlData", xmlData);
		//post.setParameter("criteria", criteria);
		//post.setParameter("criteria", criteria2);

		
		

		HttpClient httpclient = new HttpClient();
		try {
			long t1 = System.currentTimeMillis();
			int result = httpclient.executeMethod(post);
			// -----------------------Get response as a string ----------------
			accountDetails = post.getResponseBodyAsString();
		
		}
		catch (Exception e) {
			
			//ErrorLog el=new ErrorLog();
			//el.write(e.getLocalizedMessage());
			
		} finally {

			post.releaseConnection();
		}
	/*	try
		{
		    PrintStream myconsole=new PrintStream(new File("F://java.txt"));
		    System.setOut(myconsole);
		  //  System.out.println();
		   // myconsole.print(val);
		}
			
		
		catch(FileNotFoundException fx)
		{
			System.out.print(fx);
		}*/

		return accountDetails;
	}

	public String getContactDetails() {
		String contactDetails = null;

		//String authtoken = prop.getProperty("authtoken");
		//String scope = "crmapi";
		//String selectColumns = "Contacts(Contacts Owner,Contact,Account Name,Salesperson Code,Email)";
		//String newFormat = "2";
		//String fromIndex = "1";
		//String toIndex = "20";
		//String criteria = "((Tigger Date:"+TodayDate+"))";
		//String criteria = "(Tigger Date:2017-10-25)";
		


		//String targetURLC =prop.getProperty("targetURLC");
		///System.out.println(targetURLC);
		String paramname = "content";
		//PostMethod post = new PostMethod(targetURLC);
		//post.setParameter("authtoken", authtoken);
		//post.setParameter("scope", scope);
		//post.setParameter("newFormat", newFormat);
		//post.setParameter("selectColumns", selectColumns);
		//post.setParameter("fromIndex", fromIndex);
		//post.setParameter("toIndex", toIndex);
		//post.setParameter("criteria",criteria);
		

		HttpClient httpclient = new HttpClient();
		PrintWriter myout = null;

		// Execute http request
		try {
			long t1 = System.currentTimeMillis();
			//int result = httpclient.executeMethod(post);
			
			//contactDetails = post.getResponseBodyAsString();
			}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			// myout.close();
		//	post.releaseConnection();

		}

		return contactDetails;
	}
	public String getAccountUpdate(String xmlString2) {
		String updateDetails = null;

		String authtoken = prop.getProperty("authtoken");
		String xmlData = prop.getProperty("xmlData");
		String scope = "crmapi";
		String newFormat = "2";
		String version="4";
		String fromIndex = "1";
		String toIndex = "20";
		String customercsv=prop.getProperty("customercsv");
		//String xmlDataString1=xmlString2;
		System.out.println(xmlData);
			
		String targetURL =prop.getProperty("targetURL");
		String paramname = "content";
		PostMethod post = new PostMethod(targetURL);
		post.setParameter("authtoken", authtoken);
		post.setParameter("scope", scope);
		post.setParameter("newFormat", newFormat);
		//post.setParameter("fromIndex", fromIndex);
		//post.setParameter("toIndex", toIndex);
		post.setParameter("xmlData", xmlData);
		post.setParameter("version", version);
		post.setParameter("customercsv",customercsv);
		
		HttpClient httpclient = new HttpClient();
		PrintWriter myout = null;

		// Execute http request
		try {
			long t1 = System.currentTimeMillis();
			int result = httpclient.executeMethod(post);
			
			updateDetails= post.getResponseBodyAsString();
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			// myout.close();
			post.releaseConnection();

		}

		return updateDetails;
	}
	
	public List<AccountInfo> getAccountsInfo(ArrayList<Account> listOfAccount,ArrayList<Contact> listOfContact){
		
		List<AccountInfo> listOfAccountInfo = new ArrayList<>();
		

		for (Account account : listOfAccount) {

			for (Contact contact : listOfContact) {

				// common column
				
				if (account.getAccountId().equals(contact.getAccountId())) 
				{
					//Mapping Fields
					AccountInfo accountInfo = new AccountInfo();
					
					
					//-----------------------------------------------------------------------------
					
				/*	accountInfo.setAccountName("pavithra");
					accountInfo.setAccountNumber("365353");
					accountInfo.setAccountPassword("Postaplus"); //As per abielnet indira the value is hardcoded
					accountInfo.setAddress("App");
					accountInfo.setCodeAccountType("hhh57");
					accountInfo.setCodeArea("NA");
					accountInfo.setCodeCity("bangalore");
					accountInfo.setCodeCountry("india");
					accountInfo.setCodeProvince("46t46");
					accountInfo.setCodeSector("7yff");
					accountInfo.setCodeStation("hf8f8");
					accountInfo.setCodeWareHouseType("NRML"); //As per abielnet indira the value is hardcoded
					accountInfo.setContactPerson("pavi");
					accountInfo.setEmail("pavithra@rixyncs.co.in");
					accountInfo.setFax("abc");
					accountInfo.setLanguage("EN"); //As per abielnet indira the value is hardcoded
					accountInfo.setMobile("75757577");
					//accountInfo.setOpeningBalance(account.getOpeningBalance());
					accountInfo.setPACINumber("46765");
					accountInfo.setPinCode("111");
					accountInfo.setSalesCode("NA");
					accountInfo.setStatus("started");
					
					listOfAccountInfo.add(accountInfo);*/
		
					//-----------------------------------------------------------------------------


					accountInfo.setAccountName(account.getAccountName());
					accountInfo.setAccountNumber(account.getAccountId());
					accountInfo.setAccountPassword("Postaplus"); //As per abielnet indira the value is hardcoded
					accountInfo.setAddress(account.getLandmark());
					accountInfo.setCodeAccountType(account.getAccountTypeCode());
					accountInfo.setCodeArea("NA");
					accountInfo.setCodeCity(account.getBillingCity());
					accountInfo.setCodeCountry(account.getBillingCountry());
					accountInfo.setCodeProvince(account.getBillingState());
					accountInfo.setCodeSector(account.getSectorCode());
					accountInfo.setCodeStation(account.getStationCodeUpd());
					accountInfo.setCodeWareHouseType("NRML"); //As per abielnet indira the value is hardcoded
					accountInfo.setContactPerson(contact.getContactName());
					accountInfo.setEmail(contact.getEmail());
					accountInfo.setFax(account.getFax());
					accountInfo.setLanguage("EN"); //As per abielnet indira the value is hardcoded
					accountInfo.setMobile(account.getPhone());
					//accountInfo.setOpeningBalance(account.getOpeningBalance());
					accountInfo.setPACINumber(account.getPaciNumber());
					accountInfo.setPinCode(account.getBillingCode());
					accountInfo.setSalesCode("NA");
					accountInfo.setStatus(account.getCustomerStatus());
					
					listOfAccountInfo.add(accountInfo);
			}

			}

		}
		
		return listOfAccountInfo;
		
	}

	public static void main(String args[]) 
	{
		try{
			
			prop.load(DataPush.class.getClassLoader().getResourceAsStream(
					"postaclient.properties"));
	
			DataPush sc = new DataPush();
			CsvToXml cs=new CsvToXml();
			//cs.convert(csvFileName, xmlFileName)
			cs.convert("Abiel-jashan (1).csv", "new.xml");
			String accountDetails = sc.getAccountDetails();
		 String contactDetails = sc.getContactDetails();
		
			
			
		//	System.out.println("accountXml = " + accountDetails);
			//System.out.println("contact =" + contactDetails);

			//Getting account data
			AccountDomParser accountDomParser = new AccountDomParser();
			ArrayList<Account> listOfAccount =accountDomParser.pasrse(accountDetails);
			
				
			System.out.println("list of Account size = "+listOfAccount.size()); 
			//database connection
			
			//data base end
			
			
			//System.out.println("Account Name retrive  from Account===="+listOfAccount.get(0).getAccountName());
				
			//Getting contact data
			ContactDomParser contactDomParser = new ContactDomParser();
			ArrayList<Contact> listOfContact = contactDomParser.pasrse(contactDetails);
			
			
			System.out.println("list of Contact size = "+listOfContact.size()); 
		//System.out.println("Account Name retrive  from ===="+listOfContact.get(0).getAccountName());
			
			// setting the bean value for AccountInfo
		//	List<AccountInfo> listOfAccountInfo =sc.getAccountsInfo(listOfAccount);
			
			USERINFO userInfo = new USERINFO();
			userInfo.setAPIKey(prop.getProperty("APIKey"));
			userInfo.setPassword(prop.getProperty("Password"));
			userInfo.setUserName(prop.getProperty("UserName"));
			
			System.out.println("Calling Account records");
			
			
			//calling Postaplus;
			
		//	System.out.println("Size=================================="+listOfAccountInfo.size());

			PostaClient postaClient = new PostaClient();
		//	if(listOfAccountInfo.size()>0)
			//postaClient.createMasterAccount(userInfo, listOfAccountInfo.get(0));
			
			//Updating data into Zoho
			
			AccountDomParser accountDomParser1 = new AccountDomParser();
			StringBuffer xmlString1 =accountDomParser1.parseListToXml(listOfAccount);
			String updateDetails=sc.getAccountUpdate(xmlString1.toString());
		System.out.println(updateDetails);
			
			//ErrorLog el=new ErrorLog();
			//el.write("Helloooooo");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		//ConvertXml cs=new ConvertXml();
		//cs.convert(null, null);
		
		}
	/*--------------testing-------------------*/
	

	 /* Protected members to avoid instantiation */
   protected DocumentBuilderFactory domFactory = null;
   protected DocumentBuilder domBuilder = null;
   /* Constant strings */
   // Input CSV file
   public String INPUT_FILE= "C:/Users/pavithra/Downloads/Abiel-jashan (1).csv";
   // Output XML document
   public String OUTPUT_FILE="C:/Users/pavithra/Desktop/new.xml";
   // First element in the XML document
   final String FIRST_ELEMENT="Contacts";
   final String Row_Element="row";
  
   /**
    * This method converts the given CSV file into an XML document
    */
   public  String convert(String csvFileName, String xmlFileName) {
       int rowCount = -1;
       try {
           /* Initializing the XML document  */
           Document newDoc = domBuilder.newDocument();
           /* Creating the root element in the XML */
           Element rootElem = newDoc.createElement(FIRST_ELEMENT);
           newDoc.appendChild(rootElem);
           /* Reading the CSV file */
           BufferedReader csvFileReader;
           csvFileName = INPUT_FILE;
           csvFileReader = new BufferedReader(new FileReader(csvFileName));
           /* Initialize the number of fields to 0 */
           int fieldCount = 0;
           String[] csvFields = null;
           StringTokenizer stringTokenizer = null;

           /**
            * Map the column names in the CSV file as the elements in the XML
            * document, eliminate any other characters not eligible for XML element
            * naming
            */
           /* Initialize the current line variable */
           String currLine = csvFileReader.readLine();
           /* Loop until we reach the end of the file 
            * edge case: Empty CSV file
            * */

           if(currLine != null) {
               /* Separate fields based on commas */
               stringTokenizer = new StringTokenizer(currLine, ",");
               fieldCount = stringTokenizer.countTokens();
               /* If there is data in the CSV file */
               if(fieldCount > 0) {
                   /* Initialize a String Array of Fields */
                   csvFields = new String[fieldCount];
                   int i = 0;
                   /* Loop till all elements are found and save fields */
                   while (stringTokenizer.hasMoreElements()) {
                       csvFields[i++] = String.valueOf(stringTokenizer.nextElement());
                   }
               }

           }
           else {
               System.out.println("Nothing to parse");
           }
           /* reading rows from the CSV file */
           while((currLine = csvFileReader.readLine()) != null) {
               stringTokenizer = new StringTokenizer(currLine, ",");
               fieldCount = stringTokenizer.countTokens();
               /* if rows exist in the CSV file*/
               if(fieldCount > 0) {
                   /* Create the row element*/
                   Element rowElem = newDoc.createElement("row");
                  
                   int i = 0;
                   /* until there are more elements*/
                   while(stringTokenizer.hasMoreElements()) {
                       try {
                           /* Append each element found to each row element*/
                           String currValue = String.valueOf(stringTokenizer.nextElement());
                           Element currElem = newDoc.createElement(csvFields[i++]);
                           currElem.appendChild(newDoc.createTextNode(currValue));
                           rowElem.appendChild(currElem);
                       }
                       catch(Exception exp) {

                       }
                   }
                   /* Append the rows to the root element*/
                   rootElem.appendChild(rowElem);
                   rowCount++;
               }
           }
           /* Finish reading the CSV file */
           csvFileReader.close();

           /* Saving the generated XML doc into required format file to disk */
           TransformerFactory tranFactory = TransformerFactory.newInstance(); 
           Transformer aTransformer = tranFactory.newTransformer(); 
           aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
           aTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
           aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
           Source src = new DOMSource(newDoc);
           xmlFileName = OUTPUT_FILE;
           Result dest = new StreamResult(new File(xmlFileName)); 
           aTransformer.transform(src, dest); 

           rowCount++;
       }
       catch(IOException exp) {
           System.err.println(exp.toString());
       }
       catch(Exception exp) {
           System.err.println(exp.toString());
       }
       /* Number of rows parsed into XML */
       return OUTPUT_FILE;

   }

	}