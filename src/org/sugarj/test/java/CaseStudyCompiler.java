package org.sugarj.test.java;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CaseStudyCompiler {

	public static boolean compileAndTestCaseStudy(final CaseStudyProject project) {
		System.out.println(" === Compiling case study \"" + project.getName()
				+ "\" ===");
		System.out.println("Src: " + project.getSrcPath());
		System.out.println("Bin: " + project.getBinPath());
		// Initialize Compiler and get files to compile
		CompilerWrapper wrapper = new CompilerWrapper(project.getSrcPath(),
				project.getBinPath(), project.getName());

		List<Path> testFiles;
		try {
			testFiles = project.getTestSugJFiles();
		} catch (IOException e) {
			throw new RuntimeException("Files to get Test files of case study "
					+ project.getName(), e);
		}
		System.out.println("Files to compile: " + testFiles.toString());
		// Convert files to compiler arguments
		for (Path file : testFiles) {
			String fileString = project.getSrcPath().relativize(file).toString();
			// Compile
			System.out.println("Test classes to compile: " + fileString);
			System.out.println("Invoking compiler ...");
			try {
				int exitValue = wrapper.callCompiler(fileString);
				int expectedValue = project.getExpectedExitValue(fileString);
				if (exitValue != expectedValue) {
					System.out.println("Unexpected exit value " + exitValue + " instead of " + expectedValue);
					return false;
				}
			} catch (Throwable e) {
				System.out.println("Compiling failed");
				e.printStackTrace();
				return false;
			}
		}
		
		System.out.println("Compiling successful.");

		return true;
	}

	public static void main(String[] args) {
		CaseStudyProject p = new CaseStudyProject(args[0]);
		for (int i = 1; i < args.length; i=i+2) {
			String fileName = args[i];
			int exitValue = Integer.parseInt(args[i+1]);
			p.setExpectedExitValue(fileName, exitValue);
		}
		if (!compileAndTestCaseStudy(p)) {
			System.exit(1);
		}
	}

}
