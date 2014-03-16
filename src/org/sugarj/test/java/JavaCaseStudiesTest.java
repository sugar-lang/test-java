package org.sugarj.test.java;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class JavaCaseStudiesTest {

	@Test
	public void testPairs() {
		CompilerWrapper wrapper = new CompilerWrapper(new File("../case-studies/pairs/src/"));
		assertTrue(wrapper.callCompiler(null));
		assertTrue(wrapper.callCompiledStaticMethod("pair.Test", "main", new Class[]{String[].class}, new Object[]{null}));
	}
	
}
