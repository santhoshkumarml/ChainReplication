package async.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ConfigUtil {
	public static String serializeToFile(Config config) {
		OutputStream fileOutputStream = null;
		OutputStream bufferOutputStream = null;
		ObjectOutput outputStream = null;
		String fileName = "";
		//serialize the config
		try {
			File temp = File.createTempFile("temp", Long.toString(System.nanoTime())+".ser");
			fileName = temp.getAbsolutePath();
			fileOutputStream = new FileOutputStream(fileName);
			bufferOutputStream = new BufferedOutputStream(fileOutputStream);
			outputStream = new ObjectOutputStream(bufferOutputStream);
			outputStream.writeObject(config);
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream!=null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileName;
	}

	public static Config deserializeFromFile(String filePath) {
		InputStream fileInputStream = null;
		InputStream bufferInputStream = null;
		ObjectInput objectInput = null;
		Config config = null;
		//deserialize the config
		try {
			fileInputStream = new FileInputStream(filePath);
			bufferInputStream = new BufferedInputStream(fileInputStream);
			objectInput = new ObjectInputStream(bufferInputStream);
			config = (Config) objectInput.readObject(); 
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}  finally {
			if(objectInput != null) {
				try {
					objectInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return config;
	}

}
