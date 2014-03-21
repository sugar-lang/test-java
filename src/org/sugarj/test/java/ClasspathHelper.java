package org.sugarj.test.java;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClasspathHelper {

	private static final List<String> problematicJars = Arrays.asList(
			"org.sugarj.stdlib", "org.strategoxt.imp.nativebundle");

	public static String fixClasspathForNativebundle() throws IOException {
		String classpath = System.getProperty("java.class.path");
		List<String> classpathEntries = new ArrayList<>(Arrays.asList(classpath
				.split(File.pathSeparator)));

		for (String problemJar : problematicJars) {
			// Get the classpath for the bundle
			String problemJarPath = getClassPathEntry(classpathEntries,
					problemJar);
			System.out.println("Maven dependency for " + problemJar + ": "
					+ problemJarPath);
			if (problemJarPath != null && isJarFile(problemJarPath)) {
				System.out.println("is packaged as JAR, need to extract it");
				// Bundle is not allowed to be a jar
				classpathEntries.remove(problemJarPath);
				// Extract the jar file to a folder and use this as path
				problemJarPath = extractJarFile(problemJarPath);
				System.out.println("New folder of nativebundle: "
						+ problemJarPath);
				classpathEntries.add(problemJarPath);
			}

		}
		classpath = "";
		while (classpathEntries.size() > 1) {
			classpath += classpathEntries.remove(0) + File.pathSeparator;
		}
		classpath += classpathEntries.remove(0);
		return classpath;
	}

	private static String getClassPathEntry(Iterable<String> entries,
			String identifier) {
		for (String entry : entries) {
			if (entry.contains(identifier)) {
				return entry;
			}
		}
		return null;
	}

	private static boolean isJarFile(String file) {
		return file.endsWith(".jar");
	}

	public static String extractJarFile(String jarFile) throws IOException {
		// System.out.println(org.strategoxt.imp.nativebundle.Activator.getInstance().getContext().getBundle().getResource("native/"));
		Path nativeDirPath = Paths.get("./tmp/", Paths.get(jarFile).getFileName().toString());
		emptyDirectory(nativeDirPath);
		unzip(jarFile, nativeDirPath.toString());
		return nativeDirPath.toAbsolutePath().toString();
	}

	public static void emptyDirectory(Path path) throws IOException {
		if (!Files.exists(path)) {
			return;
		}
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private static void unzip(String zipFilePath, String destDir)
			throws IOException {
		// create output directory if it doesn't exist
		Files.createDirectories(Paths.get(destDir));
		FileInputStream fis;
		// buffer for read and write data to file
		fis = new FileInputStream(zipFilePath);
		ZipInputStream zis = new ZipInputStream(fis);
		ZipEntry ze = zis.getNextEntry();
		while (ze != null) {
			if (ze.isDirectory()) {
				Files.createDirectories(Paths.get(destDir, ze.getName()));
			} else {
				Files.copy(zis, Paths.get(destDir, ze.getName()));
			}
			zis.closeEntry();
			ze = zis.getNextEntry();
		}
		// close last ZipEntry
		zis.closeEntry();
		zis.close();
		fis.close();
	}

}
