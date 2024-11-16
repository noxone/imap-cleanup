package org.olafneumann.imap.cleanup;

import java.io.IOException;

import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.smtp.SMTPClient;

@FunctionalInterface
public interface ThrowingImapFunction {
	Boolean apply(IMAPClient client) throws IOException;
}
