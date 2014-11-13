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
	//@Test
	public void testApp1() {
		final String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t1.json")
		.getAbsolutePath();
		AppStarter.main(args);
	}
	
	//@Test
	/**
	 * Test app2.
	 */
	public void testApp2() {
		final String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t2.json")
		.getAbsolutePath();
		AppStarter.main(args);
	}
	
	/**
	 * Test app3.
	 */
	@Test
	public void testApp3() {
		final String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t3.json")
		.getAbsolutePath();
		AppStarter.main(args);
	}
	
	
	//@Test
	/**
	 * Test app4.
	 */
	public void testApp4() {
		final String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t4.json")
		.getAbsolutePath();
		AppStarter.main(args);
	}

	//@Test
	/**
	 * Test app5.
	 */
	public void testApp5() {
		final String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t5.json")
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
