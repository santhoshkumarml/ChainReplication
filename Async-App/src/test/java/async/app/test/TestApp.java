package async.app.test;

import java.io.File;

import org.testng.annotations.Test;

import async.chainreplication.app.main.AppStarter;

public class TestApp {
	@Test
	public void testApp() {
		String args[] = new String[1];
		args[0] = new File("." + File.separatorChar + "src"
				+ File.separatorChar + "test" + File.separatorChar
				+ "resources" + File.separatorChar + "t1.json")
				.getAbsolutePath();
		AppStarter.main(args);
	}
}
