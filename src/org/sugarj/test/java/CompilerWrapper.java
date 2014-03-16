package org.sugarj.test.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.sugarj.driver.cli.Main;

public class CompilerWrapper {

	private File srcPath;
	private File binPath;

	public CompilerWrapper(File srcPath, File binPath) {
		super();
		if (!srcPath.exists()) {
			throw new IllegalArgumentException("Src Path does not exist");
		}
		this.srcPath = srcPath.getAbsoluteFile();

		this.binPath = binPath;
	}

	public CompilerWrapper(File srcPath) {
		this(srcPath, new File("target/test-classes"));
	}

	public CompilerWrapper() {
		this(new File("src/"));
	}

	public boolean callCompiler(File packageFolder, List<String> inputFiles) {
		List<String> args = new ArrayList<>();
		args.add("-v");
		args.add("DEBUG");
		args.add("-l");
		args.add("java");
		args.add("--gen-files");
		args.add("--cache");
		args.add(".sugarcache/");
		args.add("--dontTerminateJVM");
		args.add("--sourcepath");
		args.add(this.srcPath.getAbsolutePath());
		args.add("-d");
		args.add(this.binPath.getAbsolutePath());
		for (String source : inputFiles) {
			if (packageFolder != null) {
				args.add(packageFolder.getPath() + "/" + source);
			} else {
				args.add(source);
			}
		}
		try {
			Main.main(args.toArray(new String[args.size()]));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean callCompiler(File packageFolder) {
		// Collect sugj files
		File folder;
		if (packageFolder != null) {
			folder = new File(this.srcPath, packageFolder.getPath());
		} else {
			folder = this.srcPath;
		}
		List<String> sugjFiles = new ArrayList<>();
		collectFiles(folder, "", sugjFiles);
		System.out.println(sugjFiles);
		return this.callCompiler(packageFolder, sugjFiles);
	}

	private static void collectFiles(File folder, String relativeFolder,
			List<String> collectedFiles) {
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				collectFiles(f, relativeFolder + f.getName() + "/",
						collectedFiles);
			} else if (f.getName().endsWith(".sugj")) {
				collectedFiles.add(relativeFolder + f.getName());
			}
		}
	}

	public boolean callCompiledStaticVoidMethod(String className,
			String methodName) {
		return callCompiledStaticMethod(className, methodName, new Class[0],
				(Object[]) null);
	}

	public boolean callCompiledStaticMethod(String className,
			String methodName, Class<?>[] params, Object[] args) {
		try {
			Class<?> c = Class.forName(className);
			c.getMethod(methodName, params).invoke(null, args);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
