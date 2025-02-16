package org.olafneumann.imap.client;

public class ImapClientException extends RuntimeException {
	public ImapClientException(final String message) {
		super(message);
	}

	public ImapClientException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
