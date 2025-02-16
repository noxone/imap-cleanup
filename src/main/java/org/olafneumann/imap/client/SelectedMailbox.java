package org.olafneumann.imap.client;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectedMailbox {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final Pattern SELECT_PATTERN = Pattern.compile(
			"^\\*\\s+(?:FLAGS \\((?<flags>[^)]+)\\)|(?<count>\\d+)\\s+(?<items>.*)|OK \\[(?<key>\\w+)\\s+(?<value>[^\\]]+)\\].*)$");

	public static @Nullable SelectedMailbox parse(final Collection<String> lines) {
		final var flags = new HashSet<Flag>();
		Integer exists = null;
		Integer recent = null;
		Integer unseen = null;
		Long uidNext = null;
		Long uidValidity = null;

		for (final String line : lines) {
			final var matcher = SELECT_PATTERN.matcher(line);
			if (!matcher.matches()) {
				logger.info("Unable to parse line from SELECT result: " + line);
				continue;
			}
			if (matcher.group("flags") != null) {
				final var flagsString = matcher.group("flags");
				flags.addAll(Arrays.asList(flagsString.split("\\s+", 0)).stream().map(Flag::new).toList());
			} else if (matcher.group("count") != null) {
				final var countString = matcher.group("count");
				final var itemsString = matcher.group("items");

				switch (itemsString.toLowerCase()) {
				case "exists":
					exists = Integer.parseInt(countString);
					break;
				case "recent":
					recent = Integer.parseInt(countString);
					break;
				default:
					logger.warn("Unknown parsed line: " + line);
				}
			} else if (matcher.group("key") != null) {
				final var keyString = matcher.group("key");
				final var valueString = matcher.group("value");

				switch (keyString.toLowerCase()) {
				case "unseen":
					unseen = Integer.parseInt(valueString);
					break;
				case "uidnext":
					uidNext = Long.parseLong(valueString);
					break;
				case "uidvalidity":
					uidValidity = Long.parseLong(valueString);
					break;
				default:
					logger.warn("Unknown parsed line: " + line);
				}
			}
		}

		return new SelectedMailbox(flags, exists, recent, unseen, uidNext, uidValidity);
	}

	public final Set<Flag> flags;

	public final @Nullable Integer exits;

	public final @Nullable Integer recent;

	public final @Nullable Integer unseen;

	public final @Nullable Long uidNext;

	public final @Nullable Long uidValidity;

	public SelectedMailbox(final Collection<Flag> flags,
			final @Nullable Integer exits,
			final @Nullable Integer recent,
			final @Nullable Integer unseen,
			final @Nullable Long uidNext,
			final @Nullable Long uidValidity) {
		this.flags = Collections.unmodifiableSet(new HashSet<>(flags));
		this.exits = exits;
		this.recent = recent;
		this.unseen = unseen;
		this.uidNext = uidNext;
		this.uidValidity = uidValidity;
	}

	private static final class Flag {
		public final String originalString;

		public final String key;

		Flag(final String originalString) {
			this.originalString = originalString;
			this.key = originalString.replaceAll("\\W", "");
		}

		@Override
		public int hashCode() {
			return Objects.hash(key, originalString);
		}

		@Override
		public boolean equals(final @Nullable Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			final var other = (Flag) obj;
			return Objects.equals(key, other.key) && Objects.equals(originalString, other.originalString);
		}

		@Override
		public String toString() {
			return String.format("Flag [%s]", key);
		}
	}
}
