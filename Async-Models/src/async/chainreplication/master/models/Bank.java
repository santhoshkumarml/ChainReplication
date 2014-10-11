package async.chainreplication.master.models;

import java.io.Serializable;


public class Bank implements Serializable{
	String bankName;
	Server head;
	Server tail;
}
