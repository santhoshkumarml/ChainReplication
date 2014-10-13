package async.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

public class ConfigUtil {
	public static String convertToStringBytes(Config config) {
		String result = null;
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o;
		try {
			o = new ObjectOutputStream(b);
			o.writeObject(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			result = new String(b.toByteArray(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static Config convertToConfig(String string) {
		byte[] bytes =  null;
		try {
			bytes = string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Config config = null;
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o;
		try {
			o = new ObjectInputStream(b);
			config = (Config)o.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}

}
