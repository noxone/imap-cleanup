package org.olafneumann.imap.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import jakarta.mail.Folder;

@SuppressWarnings("resource") // TODO: Remove this warning
public class ImapClientTest {
	@RegisterExtension
	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.IMAP);

	private static final String USERNAME = "abc";

	private static final String PASSWORD = "def";

	private static final TestFolder FOLDER_STRUCTURE = //
			new TestFolder("INBOX", //
					new TestFolder("Rechnungen", //
							new TestFolder("Comp1"), //
							new TestFolder("Comp2"), //
							new TestFolder("Comp3")//
					), //
					new TestFolder("Newsletter", //
							new TestFolder("Comp1"), //
							new TestFolder("Comp4"), //
							new TestFolder("Comp7")//
					), //
					new TestFolder("Stuff", //
							new TestFolder("A"), //
							new TestFolder("B"), //
							new TestFolder("C")//
					), //
					new TestFolder("Vorzug")//
			);

	private static void createFolder(final Object parent, final TestFolder folder) throws Exception {
		final var createFolder = callGetFolder(parent, folder.name);
		if (!createFolder.exists()) {
			createFolder.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
		}
		for (final TestFolder subfolder : folder.subfolders) {
			createFolder(createFolder, subfolder);
		}
	}

	private static Folder callGetFolder(final Object object, final String name) throws Exception {
		return (Folder) object.getClass().getMethod("getFolder", String.class).invoke(object, name);
	}

	private ImapClient createClient() throws IOException {
		final var imapServer = greenMail.getImap();
		final var config = new ImapClientConfiguration();
		config.setImaps(false);
		config.setHostname(imapServer.getBindTo());
		config.setPort(imapServer.getPort());
		config.setUsername(USERNAME);
		config.setPassword(PASSWORD);
		return new ImapClient(config);
	}

	@BeforeEach
	public void initAll() throws Exception {
		greenMail.setUser(USERNAME, PASSWORD);
		final var session = greenMail.getImap().createSession();
		final var store = session.getStore("imap");
		store.connect(USERNAME, PASSWORD);
		createFolder(store, FOLDER_STRUCTURE);
	}

	@Test
	public void test1() throws Exception {
		final var client = createClient();

		final var mailboxes = client.getMailboxes();
		client.close();

		assertThat(mailboxes).isNotEmpty();
		assertThat(mailboxes).size().isEqualTo(14);
		assertThat(mailboxes.stream().map(Mailbox::getFullName).toList())
				.contains("INBOX", "INBOX.Rechnungen.Comp2", "INBOX.Stuff.A");
	}

	@Test
	public void test2() throws Exception {
		final var client = createClient();

		final var mailboxes = client.getMailboxes();
		client.select(mailboxes.get(2));

		assertThat(true).isFalse();
	}

	private static class TestFolder {
		public final String name;

		public final List<TestFolder> subfolders;

		public TestFolder(final String name, final TestFolder... subfolders) {
			this.name = name;
			this.subfolders = Arrays.asList(subfolders);
		}
	}
}
