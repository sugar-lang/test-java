package org.sugarj.test.java;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class JavaCaseStudiesTest {

	
	@Test
	public void testPairs() {
		CompilerWrapper wrapper = new CompilerWrapper(new File(
				"../case-studies/pairs/src/"));
		assertTrue(wrapper.callCompiler(null));
		assertTrue(wrapper.callCompiledStaticMethod("pair.Test", "main",
				new Class[] { String[].class }, new Object[] { null }));
	}

}
