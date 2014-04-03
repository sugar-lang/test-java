package org.sugarj.test.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompilerWrapper {

	private Path srcPath;
	private Path binPath;
	
	private static  final  Path cacheFolder = Paths.get("./.sugarcache");

	private static final String compilerClassPath;

	static {
		try {
			compilerClassPath = ClasspathHelper.fixClasspathForNativebundle();
		} catch (IOException e) {
			throw new RuntimeException(
					"Unable to create classpath for SugarJ compiler", e);
		}
	}

	public CompilerWrapper(File srcPath, File binPath) {
		super();
		if (!srcPath.exists()) {
			throw new IllegalArgumentException("Src Path does not exist");
		}
		this.srcPath = srcPath.toPath().toAbsolutePath().normalize();
		this.binPath = binPath.toPath().toAbsolutePath().normalize();
		/*try {
			ClasspathHelper.emptyDirectory(cacheFolder);
		} catch (IOException e) {
			throw new RuntimeException("Failed to empty cache", e);
		}*/
	}

	public CompilerWrapper(File srcPath) {
		this(srcPath, new File("target/test-classes"));
	}

	public CompilerWrapper() {
		this(new File("src/"));
	}

	public void callCompiler(File packageFolder, List<String> inputFiles) {
		List<String> args = new ArrayList<>();
		args.add("java");
		args.add("-cp");
		args.add(compilerClassPath);
		args.add("org.sugarj.driver.cli.Main");
		args.add("-v");
		args.add("DEBUG");
		args.add("-v");
		args.add("DETAIL");
		args.add("-l");
		args.add("java");
		args.add("--gen-files");
		args.add("--cache");
		args.add(cacheFolder.toString());
		//args.add("--dontTerminateJVM");
		args.add("--sourcepath");
		args.add(this.srcPath.toString());
		args.add("-d");
		args.add(this.binPath.toString());
		for (String source : inputFiles) {
			if (packageFolder != null) {
				args.add(packageFolder.getPath() + "/" + source);
			} else {
				args.add(source);
			}
		}
		/*
		 * try {
		 * 
		 * Main.main(args.toArray(new String[args.size()])); } catch (Throwable
		 * e) { e.printStackTrace(); return false; }
		 */
		try {
			System.out.println("Call SugarJ using");
			for (String s : args) {
				System.out.print(s);
				System.out.print(" ");
			}
			System.out.println();
			ProcessBuilder builder = new ProcessBuilder(args);
			builder.inheritIO();
			Process compilerProcess = builder.start();
			int exitCode = compilerProcess.waitFor();
			System.out.println("SugarJ exited with code " + exitCode);
			if (exitCode != 0) {
				throw new RuntimeException("SugarJ not successfully terminated with code "+ exitCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Calling SugarJ Compiler failed!", e);
		}
	}

	public void callCompiler(File packageFolder, String ... files) {
		// Collect sugj files
		File folder;
		if (packageFolder != null) {
			folder = new File(this.srcPath.toFile(), packageFolder.getPath());
		} else {
			folder = this.srcPath.toFile();
		}
		List<String> sugjFiles = new ArrayList<>();
		if (files.length == 0) {
		collectFiles(folder, "", sugjFiles);
		} else {
			sugjFiles.addAll(Arrays.asList(files));
		}
		System.out.println(sugjFiles);
		this.callCompiler(packageFolder, sugjFiles);
	}
	
	public void callCompiler(String ... files) {
		this.callCompiler(null, files);
	}

	private static void collectFiles(File folder, String relativeFolder,
			List<String> collectedFiles) {
		File[] files = folder.listFiles();
		Arrays.sort(files);
		for (File f : files) {
			if (f.isDirectory()) {
				collectFiles(f, relativeFolder + f.getName() + "/",
						collectedFiles);
			} else if (f.getName().endsWith(".sugj")) {
				collectedFiles.add(relativeFolder + f.getName());
			}
		}
	}

	public void callCompiledStaticVoidMethod(String className,
			String methodName) {
		this.callCompiledStaticMethod(className, methodName, new Class[0],
				(Object[]) null);
	}
	
	public void callMainMethod(String className) {
		this.callCompiledStaticMethod(className,"main", new Class[]{String[].class}, new Object[]{null});
	}

	public void callCompiledStaticMethod(String className,
			String methodName, Class<?>[] params, Object[] args) {
		try {
			Class<?> c = Class.forName(className);
			c.getMethod(methodName, params).invoke(null, args);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Calling method of SugarJ compiled class failed!", e);
		}
	}

}
