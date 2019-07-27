package cc.moq.gc.creator.jdbc;

public class DBConnProperty {

	private String ip;
	private String port;
	private String type;
	private String sid;
	private String username;
	private String password;
	
	
	public DBConnProperty(String ip, String port, String sid, String username,
			String password) {
		super();
		this.ip = ip;
		this.port = port;
		this.sid = sid;
		this.username = username;
		this.password = password;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
