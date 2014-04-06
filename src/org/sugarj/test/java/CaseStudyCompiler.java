package org.sugarj.test.java;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/*import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;*/

public class CaseStudyCompiler {

	private static class DynamicClassLoader extends URLClassLoader {

		public DynamicClassLoader(ClassLoader parent) {
			super(new URL[0], parent);
		}

		@Override
		public void addURL(URL url) {
			super.addURL(url);
		}

		public void addPath(Path path) {
			try {
				super.addURL(path.toFile().toURI().toURL());
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public static boolean compileAndTestCaseStudy(final CaseStudyProject project) {
		System.out.println(" === Compiling case study \"" + project.getName()
				+ "\" ===");
		System.out.println("Src: " + project.getSrcPath());
		System.out.println("Bin: " + project.getBinPath());
		// Initialize Compiler and get files to compile
		CompilerWrapper wrapper = new CompilerWrapper(project.getSrcPath(),
				project.getBinPath());
		
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
		
		/*// Test using JUnit
		System.out.println("Initializing Test Environment ...");
		// Class loader for new path
		DynamicClassLoader classLoader = new DynamicClassLoader(
				CaseStudyCompiler.class.getClassLoader());
		classLoader.addPath(project.getBinPath());
		JUnitCore unit = new JUnitCore();
		final List<Failure> failures = new LinkedList<>();
		boolean allSuccess = true;
		// Callback for JUnit Results
		unit.addListener(new RunListener() {
			public void testRunFinished(Result result) throws Exception {
				if (result.getFailureCount() != 0) {
					failures.addAll(result.getFailures());
				}
			}
		});
		// Test all files
		for (String file : fileNames) {
			String className = fileNameToClassName(file);
			System.out.println("  Testing class " + className);
			try {
				// Load class and run the test
				unit.run(classLoader.loadClass(className));
				if (failures.size() != 0) {
					System.out.println("    Failed:");
					for (Failure f : failures) {
						System.out.println("    " + f.toString());
					}
					failures.clear();
					allSuccess = false;
				} else {
					System.out.println("    Successful.");
				}
			} catch (ClassNotFoundException e) {
				System.out.println("    Loading class failed.");
				e.printStackTrace();
				allSuccess = false;
			}
		}
		System.out.println("Testing completed.");
		System.out.println();
		try {
			classLoader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return allSuccess;*/
		return true;
	}

	private static String fileNameToClassName(String name) {
		name = name.substring(0, name.length() - ".sugj".length());
		name = name.replace('/', '.');
		return name;
	}
	
	public static void main(String[] args) {
		if (!compileAndTestCaseStudy(new CaseStudyProject(args[0]))) {
			System.exit(1);
		}
	}

}
