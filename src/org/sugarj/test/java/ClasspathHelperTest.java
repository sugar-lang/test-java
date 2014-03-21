package org.sugarj.test.java;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

public class ClasspathHelperTest {
	
	@Test
	public  void testClasspath() throws IOException {
		System.out.println(ClasspathHelper.fixClasspathForNativebundle());
	}

	
}
