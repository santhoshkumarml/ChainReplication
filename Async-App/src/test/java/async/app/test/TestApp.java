package async.app.test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.activity.InvalidActivityException;

import org.testng.annotations.Test;

import async.chainreplication.app.json.util.JSONUtility;
import async.chainreplication.app.main.AppStarter;
import async.chainreplication.master.models.Server;
import async.chainreplocation.master.ChainChanges;
import async.chainreplocation.master.MasterDataStructure;
import async.common.util.Config;

// TODO: Auto-generated Javadoc
/**
 * The Class TestApp.
 */
public class TestApp {
	
	/**
	 * Test app.
	 */
	@Test
	public void testApp() {
		String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t1.json")
				.getAbsolutePath();
		AppStarter.main(args);
	}
	
	//@Test
	public void testMasterDataStructure() throws Exception {
		String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t1.json")
				.getAbsolutePath();
		Config config = JSONUtility.readConfigFromJSON(args[0]);
		MasterDataStructure masterDs = new MasterDataStructure(
				config.getChains(), config.getMaster(),
				config.getChainToServerMap(), config.getClients());
		
		Set<Server> diedServers = new HashSet<Server>();
		Server server2 = config.getServerToTimeToLive().keySet().iterator().next();
		diedServers.add(server2);
		ChainChanges chainChanges = masterDs.calculateChanges(diedServers);
		System.out.println(chainChanges);
	}
}
