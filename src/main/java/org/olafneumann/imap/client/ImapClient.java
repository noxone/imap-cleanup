package org.olafneumann.imap.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.imap.IMAPSClient;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.olafneumann.imap.client.ImapCommands.ThrowingImapCommand;

public class ImapClient implements AutoCloseable {
	private static IMAPClient createIMAPClient(final ImapClientConfiguration configuration) {
		IMAPClient client;
		if (configuration.isImaps()) {
			client = new IMAPSClient(configuration.isImplicit());
		} else {
			client = new IMAPClient();
		}
		return client;
	}

	private final IMAPClient imapClient;

	/**
	 * Create a new IMAP client and connect to the configured server.
	 *
	 * @param configuration the configuration to be used for the client.
	 * @throws IOException if something fails while connecting
	 */
	public ImapClient(final ImapClientConfiguration configuration) throws IOException {
		imapClient = createIMAPClient(configuration);
		imapClient.connect(configuration.getHostname(), configuration.getPort());
		final var loggedIn = imapClient.login(configuration.getUsername(), configuration.getPassword());
		if (!loggedIn) {
			throw new ImapClientException("Not logged in");
		}
	}

	/**
	 * Close the underlying IMAP client.
	 */
	@Override
	public void close() throws Exception {
		imapClient.disconnect();
	}

	private Stream<String> readResponse() {
		final var lines = imapClient.getReplyStrings();
		return Arrays.stream(lines);
	}

	private Stream<String> executeCommandAndReadResponse(final ThrowingImapCommand command) {
		try {
			final var success = command.apply(imapClient);
			if (!success) {
				throw new ImapClientException("Command execution was not successful.");
			}
			return readResponse();
		} catch (final IOException e) {
			throw new ImapClientException("Network error while executing IMAP command.", e);
		}
	}

	private <T> List<@NonNull T> executeCommandAndParse(final ThrowingImapCommand command,
			final Function<String, @Nullable T> parser) {
		return executeCommandAndReadResponse(command)//
				.map(parser)//
				.filter(Objects::nonNull)
				.toList();
	}

	public List<Mailbox> getMailboxes() {
		return executeCommandAndParse(ImapCommands.LIST, Mailbox::parseListResponseLine);
	}

	public boolean select(final Mailbox mailbox) {
		final var result = executeCommandAndReadResponse(ImapCommands.select(mailbox.getFullName())).toList();
		System.out.println(result);
		return false;
	}
}
