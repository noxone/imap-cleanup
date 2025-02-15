package org.olafneumann.imap.cleanup;

import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.imap.IMAPSClient;
import org.olafneumann.imap.client.ImapCommands.ThrowingImapCommand;

import com.google.common.io.Resources;

/**
 * The main class to start the application
 */
@SuppressWarnings({ "MissingCtor", "UncommentedMain" })
public final class ImapCleanup {
	private static Properties readSecretProperties() {
		final var properties = new Properties(4);
		try (var reader = Files.newBufferedReader(Paths.get(Resources.getResource("secret.properties").toURI()))) {
			properties.load(reader);
		} catch (final IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return properties;
	}

	/**
	 * The main entry point to the application.
	 *
	 * @param arguments command line arguments
	 * @throws IOException
	 * @throws SocketException
	 */
	public static void main(final String[] arguments) throws SocketException, IOException {
		System.out.println("Hallo Welt.");

		final var properties = readSecretProperties();
		final var client = new IMAPSClient(true);
		client.setConnectTimeout(50000);
		client.connect(properties.getProperty("server"), Integer.parseInt(properties.getProperty("port")));
		client.login(properties.getProperty("username"), properties.getProperty("password"));
		writeResponse(client);
		executeCommandAndWriteResponse(client, IMAPClient::capability);
		executeCommandAndWriteResponse(client, c -> c.list("", "*"));
		// executeCommandAndWriteResponse(client, c -> c.list("2 Rechnungen", "*"));
		executeCommandAndWriteResponse(client, c -> c.select("INBOX"));
		// executeCommandAndWriteResponse(client, c -> c.list("", "*"));
		// executeCommandAndWriteResponse(client, c -> c.select("/"));
		// executeCommandAndWriteResponse(client, c -> c.list("", "*"));
		// executeCommandAndWriteResponse(client, c -> c.select("2 Rechnungen/Apple"));
		// executeCommandAndWriteResponse(client, c -> c.fetch("1:3",
		// "body[header.fields (from)]"));
		executeCommandAndWriteResponse(client, c -> c.fetch("2:4", "ENVELOPE"));
		executeCommandAndWriteResponse(client, c -> c.fetch("2:4", "BODYSTRUCTURE"));
		executeCommandAndWriteResponse(client, c -> c.fetch("1:*", "UID"));
		executeCommandAndWriteResponse(client, c -> c.fetch("1:*", "RFC822.SIZE"));
		executeCommandAndWriteResponse(client, c -> c.fetch("1:*", "UID RFC822.SIZE"));
		// client.select("INBOX"); client.fetch("1:3", "body[header]");
		// client.fetch("1:3", "body[header.fields (from)]"); client.list("/", "*");
		// client.capability(); for (final String string : client.getReplyStrings()) {
		// System.out.println(string); }
		client.disconnect();
	}

	private static void executeCommandAndWriteResponse(final IMAPClient client, final ThrowingImapCommand action)
			throws IOException {
		action.apply(client);
		writeResponse(client);
	}

	private static void writeResponse(final IMAPClient client) {
		for (final String string : client.getReplyStrings()) {
			System.out.println(string);
		}
	}

	/**
	 * Private constructor to prevent class objects
	 */
	private ImapCleanup() {}
}
