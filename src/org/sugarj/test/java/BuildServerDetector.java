package org.sugarj.test.java;

import java.nio.file.Paths;

public class BuildServerDetector {

	public static boolean isRunningOnBuildserver() {
		return Paths.get(".","test").toAbsolutePath().startsWith(Paths.get("/var/lib", "jenkins"));
	}
	
}
