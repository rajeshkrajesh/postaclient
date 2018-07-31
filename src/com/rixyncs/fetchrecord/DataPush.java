package com.rixyncs.fetchrecord;
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.datacontract.schemas._2004._07.Client.PostaClient;
import org.datacontract.schemas._2004._07.Tps.AccountInfo;
import org.datacontract.schemas._2004._07.Tps.AlertEventCode;
import org.datacontract.schemas._2004._07.Tps.USERINFO;

import com.rixyncs.bean.Account;
import com.rixyncs.bean.Contact;
import com.rixyncs.bean.CreateMasterAccount;
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
		String selectColumns = "Accounts(Account Name,Updated in ERP,Area details,Station Code Upd,City Code,Country Code,AREA CODE,Billing Code,Province Code,Paci Number,Phone,Email,Fax,Salesman Code,Customer Status,Account Type Code,Sector Code,Opening Balance)";
		String newFormat = "2";
		String fromIndex = "1";
		String toIndex = "20";
		// String selectColumns = "All";
		 String criteria = "((Send to ERP:true)AND(Trigger Date:"+TodayDate+")AND(Updated in ERP:False))";
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

		String authtoken = prop.getProperty("authtoken");
		String scope = "crmapi";
		String selectColumns = "Contacts(Contacts Owner,Contact,Account Name,Salesperson Code,Email)";
		String newFormat = "2";
		String fromIndex = "1";
		String toIndex = "20";
		String criteria = "((Tigger Date:"+TodayDate+"))";
		//String criteria = "(Tigger Date:2017-10-25)";
		


		String targetURLC =prop.getProperty("targetURLC");
		System.out.println(targetURLC);
		String paramname = "content";
		PostMethod post = new PostMethod(targetURLC);
		post.setParameter("authtoken", authtoken);
		post.setParameter("scope", scope);
		post.setParameter("newFormat", newFormat);
		post.setParameter("selectColumns", selectColumns);
		post.setParameter("fromIndex", fromIndex);
		post.setParameter("toIndex", toIndex);
		post.setParameter("criteria",criteria);
		

		HttpClient httpclient = new HttpClient();
		PrintWriter myout = null;

		// Execute http request
		try {
			long t1 = System.currentTimeMillis();
			int result = httpclient.executeMethod(post);
			
			contactDetails = post.getResponseBodyAsString();
			}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			// myout.close();
			post.releaseConnection();

		}

		return contactDetails;
	}
	public String getAccountUpdate(String xmlString2) {
		String updateDetails = null;

		String authtoken = prop.getProperty("authtoken");
		String scope = "crmapi";
		String newFormat = "2";
		String version="4";
		String fromIndex = "1";
		String toIndex = "20";
		String xmlDataString1=xmlString2;
		System.out.println(xmlDataString1);
			
		String targetURLCC =prop.getProperty("targetURLCC");
		String paramname = "content";
		PostMethod post = new PostMethod(targetURLCC);
		post.setParameter("authtoken", authtoken);
		post.setParameter("scope", scope);
		post.setParameter("newFormat", newFormat);
		//post.setParameter("fromIndex", fromIndex);
		//post.setParameter("toIndex", toIndex);
		post.setParameter("xmlData", xmlDataString1);
		post.setParameter("version", version);
		
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
		//	System.out.println("Account Name retrive  from ===="+listOfContact.get(0).getAccountName());
			
			// setting the bean value for AccountInfo
			List<AccountInfo> listOfAccountInfo =sc.getAccountsInfo(listOfAccount,listOfContact);
			
			USERINFO userInfo = new USERINFO();
			userInfo.setAPIKey(prop.getProperty("APIKey"));
			userInfo.setPassword(prop.getProperty("Password"));
			userInfo.setUserName(prop.getProperty("UserName"));
			
			System.out.println("Calling Account records");
			
			
			//calling Postaplus
			
			System.out.println("Size=================================="+listOfAccountInfo.size());

			PostaClient postaClient = new PostaClient();
			if(listOfAccountInfo.size()>0)
			postaClient.createMasterAccount(userInfo, listOfAccountInfo.get(0));
			
			//Updating data into Zoho
			
			//AccountDomParser accountDomParser1 = new AccountDomParser();
			//StringBuffer xmlString1 =accountDomParser1.parseListToXml(listOfAccount);
			//String updateDetails=sc.getAccountUpdate(xmlString1.toString());
			//System.out.println(updateDetails);
			
			//ErrorLog el=new ErrorLog();
			//el.write("Helloooooo");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		}
	}