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
		wrapper.callCompiler("pair/Pair.sugj", "pair/Test.sugj");
		wrapper.callCompiler("pair/singleton/Pair.sugj");
		wrapper.callCompiler("tuples/Test.sugj" ,  "pair/singleton/Test.sugj", "pair/concrete/Test.sugj");
		wrapper.callMainMethod("pair.Test");
	}
	
	@Test
	public void testClosures() {
		CompilerWrapper wrapper = new CompilerWrapper(new File("../case-studies/closures/src/"));
		wrapper.callCompiler("javaclosure/Closure.sugj", "javaclosure/Syntax.sugj", "javaclosure/ToRefType.sugj");
		wrapper.callCompiler("javaclosure/Analysis.sugj", "javaclosure/Transformation.sugj");
		wrapper.callCompiler("javaclosure/Test.sugj");
		wrapper.callMainMethod("javaclosure.Test");
		wrapper.callCompiler("javaclosure/alternative/Arrows.sugj");
		wrapper.callCompiler("javaclosure/alternative/ArrowTest.sugj");
		wrapper.callMainMethod("javaclosure.alternative.ArrowTest");
	}
	

}
