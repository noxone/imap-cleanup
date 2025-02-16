package org.olafneumann.imap.client;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.commons.net.imap.IMAPClient;

public final class ImapCommands {
	private ImapCommands() {
		// no instances please
		throw new RuntimeException("No instances please.");
	}

	public static final ThrowingImapCommand LIST = client -> client.list("", "*");

	public static ThrowingImapCommand select(final String mailboxName) {
		return client -> client.select(mailboxName);
	}

	/**
	 * Interface for actions on the {@link IMAPClient}
	 */
	@FunctionalInterface
	public interface ThrowingImapCommand {
		/**
		 * Executes an action on an {@link IMAPClient}
		 *
		 * @param client the client to perform the action on
		 * @return <code>true</code> if the action was successful
		 * @throws IOException if any exception occurred
		 */
		Boolean apply(IMAPClient client) throws IOException;

		default Boolean applyThrowingSilently(final IMAPClient client) {
			try {
				return apply(client);
			} catch (final IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}
}
