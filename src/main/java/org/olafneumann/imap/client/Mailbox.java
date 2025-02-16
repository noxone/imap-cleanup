package org.olafneumann.imap.client;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mailbox {
	private static Logger logger = LoggerFactory.getLogger(Mailbox.class);

	private static final String LIST_LINE_REGEX_STRING
			= "^\\* LIST \\((?<flags>.*?)\\) \\\"(?<parent>.*?)\\\" \\\"(?<name>.*?)\\\"$";

	private static final Pattern LIST_LINE_REGEX = Pattern.compile(LIST_LINE_REGEX_STRING, Pattern.CASE_INSENSITIVE);

	static @Nullable Mailbox parseListResponseLine(final String line) {
		final var matcher = LIST_LINE_REGEX.matcher(line);
		if (!matcher.matches()) {
			logger.info("Descard line: {}", line);
			return null;
		}

		final var name = matcher.group("name");
		final var parent = matcher.group("parent");
		final var flags = matcher.group("flags");

		Objects.requireNonNull(name, "'name' must not be null.");
		Objects.requireNonNull(parent, "'parent' must not be null.");
		Objects.requireNonNull(flags, "'flags' must not be null.");

		logger.info("Extracted name<{}> parent<{}> flags<{}>", name, parent, flags);
		return new Mailbox(name.split("/", 0));
	}

	private final String[] nameParts;

	private final String fullName;

	public Mailbox(final String[] nameParts) {
		this.nameParts = nameParts;
		this.fullName = String.join("/", Arrays.asList(nameParts));
	}

	public String getLastName() {
		return nameParts[nameParts.length - 1];
	}

	public String getFullName() {
		return fullName;
	}

	@Override
	public String toString() {
		return String.format("Mailbox %s", Arrays.deepToString(nameParts));
	}
}
