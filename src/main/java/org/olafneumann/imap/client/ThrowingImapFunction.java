package org.olafneumann.imap.client;

import java.io.IOException;

import org.apache.commons.net.imap.IMAPClient;

@FunctionalInterface
interface ThrowingImapFunction {
	Boolean apply(IMAPClient client) throws IOException;
}
