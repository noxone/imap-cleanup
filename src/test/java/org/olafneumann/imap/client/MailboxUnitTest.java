package org.olafneumann.imap.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.olafneumann.test.TestResources.readLines;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MailboxUnitTest {
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
