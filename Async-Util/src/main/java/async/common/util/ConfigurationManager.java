package async.common.util;

import java.io.File;

import async.common.util.exceptions.InvalidConfigException;

/**
 * Configuration Manager which uses {@link ConfigReader} to read the configuration
 * and use that information to setup the whole eco system
 * @author Santhosh Kumar
 *
 */
public class ConfigurationManager {
	public static void main(String args[]) throws InvalidConfigException {
		ConfigReader reader = new ConfigReader();
		if(!new File(args[0]).exists()) {
			throw new InvalidConfigException();
		}
		Config config = reader.readConfigurationFile(args[0]);
	}
}
