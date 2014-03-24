package org.sugarj.test.java;

import java.io.File;

import org.junit.Test;

public class JavaFunctionsTest {

	@Test
	public void testCompile() {
		CompilerWrapper wrapper = new CompilerWrapper();
		wrapper.callCompiler(new File("org/sugartest/java/function"));
		wrapper.callCompiledStaticVoidMethod(
				"org.sugartest.java.function.TestFunctions", "test");
	}

}