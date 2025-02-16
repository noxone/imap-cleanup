package org.olafneumann.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestResources {
	public static List<String> readLines(final String filename) {
		final var inputUrl = getResourceUrl(filename);
		try {
			return Files.readAllLines(Paths.get(inputUrl.toURI()));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static URL getResourceUrl(final String filename) {
		return getClassLoader().getResource(filename);
	}

	public static BufferedInputStream getResourceAsStream(final String filename) {
		return new BufferedInputStream(getClassLoader().getResourceAsStream(filename));
	}

	public static BufferedReader getResourceAsReader(final String filename) {
		return new BufferedReader(new InputStreamReader(getResourceAsStream(filename), StandardCharsets.UTF_8));
	}

	private static ClassLoader getClassLoader() {
		return TestResources.class.getClassLoader();
	}
}
