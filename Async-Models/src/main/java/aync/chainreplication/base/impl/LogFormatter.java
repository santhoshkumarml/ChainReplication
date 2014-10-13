package aync.chainreplication.base.impl;

import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LogFormatter extends SimpleFormatter {
	@Override
	public String format(LogRecord record){
		//if(record.getLevel() == Level.INFO){
		return new Date(record.getMillis())+":"+record.getMessage() + "\r\n";
	}

}
