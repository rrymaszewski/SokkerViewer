package pl.pronux.sokker.bean;

public class Cookie {
	private String cookieValue = ""; 

	private String expires = ""; 

	private String path = ""; 

	private String domain = ""; 

	private boolean secure = false;

	public String getCookieValue() {
		return cookieValue;
	}

	/**
	 * this funtion works
	 * 
	 * @param cookieValue
	 *          ble ble ble
	 */
	public void setCookieValue(String cookieValue) {
		this.cookieValue = cookieValue;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}
}
