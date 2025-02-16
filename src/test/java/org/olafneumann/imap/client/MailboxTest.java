package org.olafneumann.imap.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.olafneumann.test.TestResources.readLines;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MailboxTest {
	@Test
	public void shouldCreateMailboxInstances_whanCallingParse_givenValidInput() {
		final var givenLines = readLines("result-list-01.txt");

		final var actualMailboxes = givenLines.stream()//
				.map(Mailbox::parseListResponseLine)//
				.filter(Objects::nonNull)//
				.toList();
		final var actualLastNames = actualMailboxes.stream().map(Mailbox::getLastName).toList();

		assertThat(actualMailboxes).hasSize(17);
		assertThat(actualLastNames).containsExactlyInAnyOrder("Archive",
				"3 Versicherungen",
				"Bank",
				"Junk",
				"2 Rechnungen",
				"INBOX",
				"Auto",
				"Versicherungen",
				"Elster",
				"Haus",
				"Deleted Messages",
				"Sent Messages",
				"Drafts",
				"1 read",
				"TED",
				"Urlaub",
				"9 Info");
	}
}
