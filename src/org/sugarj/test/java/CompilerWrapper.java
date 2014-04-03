package org.sugarj.test.java;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompilerWrapper {

	private Path srcPath;
	private Path binPath;

	private static final Path cacheFolder = Paths.get("./.sugarcache");

	private static final String compilerClassPath;

	public boolean useDifferentProcess = true;

	static {
		try {
			compilerClassPath = ClasspathHelper.fixClasspathForNativebundle();
		} catch (IOException e) {
			throw new RuntimeException(
					"Unable to create classpath for SugarJ compiler", e);
		}
	}

	public CompilerWrapper(Path srcPath, Path binPath) {
		super();
		if (!Files.exists(srcPath)) {
			throw new IllegalArgumentException("Src Path does not exist");
		}
		this.srcPath = srcPath.toAbsolutePath().normalize();
		this.binPath = binPath.toAbsolutePath().normalize();
		/*
		 * try { ClasspathHelper.emptyDirectory(cacheFolder); } catch
		 * (IOException e) { throw new RuntimeException("Failed to empty cache",
		 * e); }
		 */
	}

	public CompilerWrapper(Path srcPath) {
		this(srcPath, Paths.get("target/test-classes"));
	}

	public CompilerWrapper() {
		this(Paths.get("src/"));
	}

	public void callCompiler(Path packageFolder, List<String> inputFiles) {
		List<String> args = new ArrayList<>();
		if (this.useDifferentProcess) {
			args.add("java");
			args.add("-cp");
			args.add(compilerClassPath);
			args.add("org.sugarj.driver.cli.Main");
		}
		args.add("-v");
		args.add("DEBUG");
		args.add("-v");
		args.add("DETAIL");
		args.add("-l");
		args.add("java");
		args.add("--gen-files");
		args.add("--cache");
		args.add(cacheFolder.toString());
		if (!this.useDifferentProcess) {
			args.add("--dontTerminateJVM");
		}
		args.add("--sourcepath");
		args.add(this.srcPath.toString());
		args.add("-d");
		args.add(this.binPath.toString());
		for (String source : inputFiles) {
			if (packageFolder != null) {
				args.add(packageFolder.toString() + "/" + source);
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
			if (this.useDifferentProcess) {
				ProcessBuilder builder = new ProcessBuilder(args);
				builder.inheritIO();
				Process compilerProcess = builder.start();
				int exitCode = compilerProcess.waitFor();
				System.out.println("SugarJ exited with code " + exitCode);
				if (exitCode != 0) {
					throw new RuntimeException(
							"SugarJ not successfully terminated with code "
									+ exitCode);
				}
			} else {
				org.sugarj.driver.cli.Main.main(args.toArray(new String[0]));
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException("Calling SugarJ Compiler failed!", e);
		}
	}

	public void callCompiler(Path packageFolder, String... files) {
		// Collect sugj files
		Path folder;
		if (packageFolder != null) {
			folder = this.srcPath.resolve(packageFolder);
		} else {
			folder = this.srcPath;
		}
		List<String> sugjFiles = new ArrayList<>();
		if (files.length == 0) {
			try {
				collectFiles(folder, "", sugjFiles);
			} catch (IOException e) {
				throw new RuntimeException("Collecting files failes ", e);
			}
		} else {
			sugjFiles.addAll(Arrays.asList(files));
		}
		this.callCompiler(packageFolder, sugjFiles);
	}

	public void callCompiler(String... files) {
		this.callCompiler(null, files);
	}

	private static void collectFiles(final Path folder, String relativeFolder,
			final List<String> collectedFiles) throws IOException {
		Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				if (file.getFileName().toString().endsWith(".sugj")) {
					collectedFiles.add(folder.relativize(file).toString());
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public void callCompiledStaticVoidMethod(String className, String methodName) {
		this.callCompiledStaticMethod(className, methodName, new Class[0],
				(Object[]) null);
	}

	public void callMainMethod(String className) {
		this.callCompiledStaticMethod(className, "main",
				new Class[] { String[].class }, new Object[] { null });
	}

	public void callCompiledStaticMethod(String className, String methodName,
			Class<?>[] params, Object[] args) {
		try {
			Class<?> c = Class.forName(className);
			c.getMethod(methodName, params).invoke(null, args);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Calling method of SugarJ compiled class failed!", e);
		}
	}

}
