package com.rixyncs.fetchrecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ErrorLog {
	final static Logger logger = 
LogManager.getLogger(ErrorLog.class.getName());
	
	public void write(String data) {
		try {
			//DataPush dp = new DataPush();
			//FileWriter fw = new FileWriter(new File("/src/com.rixyncs.fetchrecord/errorFile.txt"));
			FileWriter fw = new FileWriter(new File("F:/postasoapclient/src/com/rixyncs/fetchrecord/errorFile-"+DataPush.getTodaydate()+".txt"));
			//FileWriter fw = new FileWriter(new File("\errorFile-"+DataPush.getTodaydate()+".txt"));
			BufferedWriter bw = new BufferedWriter(fw);

			fw.write("DATE : ");
			fw.write(DataPush.getTodaydate());
			fw.write("\n");
			fw.write("\n");
			fw.write("ERROR DESCRIPTION");
			fw.write("\n");
			fw.write(data);
			fw.write("\n");
			fw.write("----------------------------------------------------------------------");
			fw.write("\n");
			fw.close();
		}
		catch (IOException e) {
			//logger.fatal("Unable to open file.", e);
			e.printStackTrace();
		}
	}
}