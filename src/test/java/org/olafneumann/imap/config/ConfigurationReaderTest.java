package org.olafneumann.imap.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.olafneumann.test.TestResources;

public class ConfigurationReaderTest {
	@Test
	public void shouldLoadConfig() throws URISyntaxException {
		final var configReader = new ConfigurationReader();

		final var actual = configReader.readConfiguration(
				Paths.get(TestResources.getResourceUrl("ConfigurationReader-ImapClienConfiguration.json").toURI()));

		assertThat(actual.getHostname()).isEqualTo("aaa");
	}
}
