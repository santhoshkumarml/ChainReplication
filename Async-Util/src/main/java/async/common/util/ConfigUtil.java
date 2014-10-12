package async.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ConfigUtil {
	public static byte[] convertToBytes(Config config) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o;
		try {
			o = new ObjectOutputStream(b);
			o.writeObject(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b.toByteArray();
	}

	public static Config convertToConfig(String string) {
		byte[] bytes =  string.getBytes();
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
