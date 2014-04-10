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
		// Convert files to compiler arguments
		String[] fileNames = new String[testFiles.size()];
		String files = "";
		int i = 0;
		for (Path file : testFiles) {
			fileNames[i] = project.getSrcPath().relativize(file).toString();
			files += fileNames[i] + " ";
			i++;
		}
		// Compile
		System.out.println("Test classes to compile: " + files);
		System.out.println("Invoking compiler ...");
		try {
			wrapper.callCompiler(fileNames);
		} catch (Throwable e) {
			System.out.println("Compiling failed");
			e.printStackTrace();
			return false;
		}
		System.out.println("Compiling successful.");

		return true;
	}

	public static void main(String[] args) {
		if (!compileAndTestCaseStudy(new CaseStudyProject(args[0]))) {
			System.exit(1);
		}
	}

}
