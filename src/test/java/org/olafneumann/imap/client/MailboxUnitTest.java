package org.olafneumann.imap.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.io.Resources;

@SuppressWarnings("javadoc")
@ExtendWith(MockitoExtension.class)
public class MailboxUnitTest {
	private static List<String> readLines(final String filename) {
		final var inputUrl = Resources.getResource(filename);
		try {
			return Files.readAllLines(Paths.get(inputUrl.toURI()));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private final ImapClient imapClient = mock(ImapClient.class);

	@Test
	public void shouldCreateMailboxInstances_whanCallingParse_givenValidInput() {
		final var givenLines = readLines("mailbox-01.txt");

		final var actualMailboxes = givenLines.stream()//
				.map(line -> Mailbox.parseListResponseLine(imapClient, line))//
				.filter(Objects::nonNull)//
				.toList();
		final var actualLastNames = actualMailboxes.stream().map(Mailbox::getLastName).toList();

		assertThat(actualMailboxes).hasSize(28);
		assertThat(actualLastNames).containsExactlyInAnyOrder("Archive",
				"13 Versicherungen",
				"Bank",
				"Paypal",
				"Junk",
				"2 Rechnungen",
				"Apple",
				"Vodafone",
				"INBOX",
				"Auto",
				"Versicherungen",
				"Elster",
				"Microsoft",
				"AppStoreConnect",
				"Haus",
				"Investify",
				"Sony",
				"DomainFactory",
				"Deleted Messages",
				"Sent Messages",
				"Haspa",
				"Commerzbank",
				"Drafts",
				"1 read",
				"TED",
				"Urlaub",
				"Amazon",
				"9 Info");
	}
}
