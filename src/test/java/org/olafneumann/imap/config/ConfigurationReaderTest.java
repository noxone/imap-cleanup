package org.olafneumann.imap.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.google.common.io.Resources;

public class ConfigurationReaderTest {
	@Test
	public void shouldLoadConfig() throws URISyntaxException {
		final var configReader = new ConfigurationReader();

		final var actual = configReader.readConfiguration(
				Paths.get(Resources.getResource("ConfigurationReader-ImapClienConfiguration.json").toURI()));

		assertThat(actual.getHostname()).isEqualTo("aaa");
	}
}
