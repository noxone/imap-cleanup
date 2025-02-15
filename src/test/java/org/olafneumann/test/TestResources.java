package org.olafneumann.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.io.Resources;

public class TestResources {
	public static List<String> readLines(final String filename) {
		final var inputUrl = Resources.getResource(filename);
		try {
			return Files.readAllLines(Paths.get(inputUrl.toURI()));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static BufferedReader getBufferedReader(final String filename) {
		try {
			return Files.newBufferedReader(Paths.get(Resources.getResource(filename).toURI()));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
