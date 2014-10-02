package com.ricston.statistics.main;

import org.junit.Test;

public class MainTestCase {
	
	/**
	 * Just make sure application starts up fine
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMainClass() throws InterruptedException{
		Main.main(null);
		Thread.sleep(500);
	}

}
