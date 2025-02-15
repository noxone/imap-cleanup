package org.olafneumann.imap.config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.olafneumann.imap.client.ImapClientConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurationReader {
	private final ObjectMapper objectMapper;

	public ConfigurationReader() {
		this(new ObjectMapper());
	}

	public ConfigurationReader(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public ImapClientConfiguration readConfiguration(final Path path) {
		try (var reader = Files.newBufferedReader(path)) {
			return objectMapper.readerFor(ImapClientConfiguration.class).readValue(reader);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
