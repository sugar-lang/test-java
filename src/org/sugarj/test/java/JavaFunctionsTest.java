package org.sugarj.test.java;

import java.nio.file.Paths;

import org.junit.Test;

public class JavaFunctionsTest {

	@Test
	public void testCompile() {
		CompilerWrapper wrapper = new CompilerWrapper();
		wrapper.callCompiler(Paths.get("org/sugartest/java/function"));
		wrapper.callCompiledStaticVoidMethod(
				"org.sugartest.java.function.TestFunctions", "test");
	}

}