package org.sugarj.test.java;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class CaseStudyProject {

	private String name;
	private Path path;
	private Path srcPath;
	private Path binPath;

	public CaseStudyProject(String name) {
		super();
		this.name = name;
		this.path = Paths.get("../case-studies", name).toAbsolutePath().normalize();
		this.srcPath = this.path.resolve("src");
		this.binPath = this.path.resolve("bin");
	}

	public String getName() {
		return name;
	}

	public Path getPath() {
		return path;
	}

	public Path getSrcPath() {
		return srcPath;
	}
	
	public Path getBinPath() {
		return binPath;
	}

	private List<Path> collectFiles(final Pattern filter) throws IOException {
		final List<Path> files = new LinkedList<>();
		Files.walkFileTree(this.srcPath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				if (filter.matcher(file.getFileName().toString()).matches()) {
					files.add(file);
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return files;
	}

	public List<Path> getAllSugJFiles() throws IOException {
		return this.collectFiles(Pattern.compile(".*\\.sugj"));
	}

	public List<Path> getTestSugJFiles() throws IOException {
		return this.collectFiles(Pattern
				.compile(".*Test\\.sugj|Test.*\\.sugj"));
	}

}
