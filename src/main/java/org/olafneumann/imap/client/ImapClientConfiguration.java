package org.olafneumann.imap.client;

public class ImapClientConfiguration {
	private boolean imaps = true;

	private boolean implicit = true;

	private String hostname = "";

	private int port = 993;

	private String username = "";

	private String password = "";

	public boolean isImplicit() {
		return implicit;
	}

	public void setImplicit(final boolean implicit) {
		this.implicit = implicit;
	}

	public int getPort() {
		return port;
	}

	public boolean isImaps() {
		return imaps;
	}

	public void setImaps(final boolean imaps) {
		this.imaps = imaps;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(final String hostname) {
		this.hostname = hostname;
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
}
