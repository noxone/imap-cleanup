package org.olafneumann.imap.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.olafneumann.test.TestResources;

public class SelectedMailboxTest {
	@Test
	public void shouldParse01() {
		final var lines = TestResources.readLines("result-select-01.txt");

		final var selectedMailbox = SelectedMailbox.parse(lines);

		assertThat(selectedMailbox).isNotNull();
	}
}
