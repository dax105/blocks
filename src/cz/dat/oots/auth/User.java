package cz.dat.oots.auth;

public class User {
	private String userName;
	private String password;
	private String token;
	private boolean authenticated;

	public User(String userName, String password) {
		this.setUserName(userName);
		this.setPassword(password);
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		this.authenticated = false;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.authenticated = false;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
		this.authenticated = false;
	}

	public boolean isAuthenticated() {
		return this.authenticated;
	}

	public void setAuthenticated() {
		this.authenticated = true;
	}

	public void setUnauthenticated() {
		this.authenticated = false;
	}
}
