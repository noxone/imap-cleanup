package org.olafneumann.imap.client;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.imap.IMAPSClient;

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

	public ImapClient(final ImapClientConfiguration configuration) throws IOException {
		imapClient = createIMAPClient(configuration);
		imapClient.connect(configuration.getHostname(), configuration.getPort());
	}

	@Override
	public void close() throws Exception {
		imapClient.disconnect();
	}

	private Stream<String> readResponse() {
		final var lines = imapClient.getReplyStrings();
		return Arrays.stream(lines);
	}

	private Stream<String> executeCommandAndReadResponse(final ThrowingImapFunction command) {
		try {
			command.apply(imapClient);
			return readResponse();
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private <T> List<T> executeCommandAndParse(final ThrowingImapFunction command, final Function<String, T> parser) {
		return executeCommandAndReadResponse(command)//
				.map(parser)//
				.filter(Objects::nonNull)
				.toList();
	}

	public List<Mailbox> getMailboxes() {
		return executeCommandAndParse(c -> c.list("", "*"), line -> Mailbox.parseListResponseLine(this, line));
	}
}
