package org.sugarj.test.java;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.*;

public class JavaFunctionsTest {

	@Test
	public void testCompile() {
		CompilerWrapper wrapper = new CompilerWrapper();
		assertTrue(wrapper.callCompiler(new File("org/sugartest/java/function")));
		assertTrue(wrapper.callCompiledStaticVoidMethod("org.sugartest.java.function.TestFunctions", "test"));
	}

}