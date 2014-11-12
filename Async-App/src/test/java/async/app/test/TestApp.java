package async.app.test;

import java.io.File;

import org.testng.annotations.Test;

import async.chainreplication.app.main.AppStarter;

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
		final String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t1.json")
		.getAbsolutePath();
		AppStarter.main(args);
	}

	// @Test
	/**
	 * Test master data structure.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public void testMasterDataStructure() throws Exception {
		/*
		 * String args[] = new String[1]; args[0] = new File("." +
		 * File.separatorChar + "src" + File.separatorChar + "test" +
		 * File.separatorChar + "resources" + File.separatorChar + "t1.json")
		 * .getAbsolutePath(); Config config =
		 * JSONUtility.readConfigFromJSON(args[0]); MasterDataStructure masterDs
		 * = new MasterDataStructure( config.getChains(), config.getMaster(),
		 * config.getChainToServerMap(), config.getClients());
		 * 
		 * Set<Server> diedServers = new HashSet<Server>(); Server server2 =
		 * config.getServerToTimeToLive().keySet().iterator().next();
		 * diedServers.add(server2); ChainChanges chainChanges =
		 * masterDs.calculateChanges(diedServers);
		 * System.out.println(chainChanges);
		 */
	}
}
