package redistest.model;

import java.io.Serializable;

public class UserModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String passwd;
	private int age;
	
	public UserModel(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "UserModel [username=" + username + ", passwd=" + passwd + ", age=" + age + "]";
	}
	
	
	

}
