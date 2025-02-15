package org.olafneumann.imap.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

public class ImapClientTest {
	@RegisterExtension
	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.IMAP);

	@Test
	public void test1() throws Exception {
		final var imapServer = greenMail.getImap();
		greenMail.setUser("abc", "def");

		final var config = new ImapClientConfiguration();
		config.setImaps(false);
		config.setHostname(imapServer.getBindTo());
		config.setPort(imapServer.getPort());
		config.setUsername("abc");
		config.setPassword("def");
		final var client = new ImapClient(config);

		final var mailboxes = client.getMailboxes();
		client.close();

		assertThat(mailboxes).isNotEmpty();
	}
}
