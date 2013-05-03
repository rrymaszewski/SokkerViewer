package pl.pronux.sokker.utils.security;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {
	private String password;

	public Password(String password) {
		this.password = password;
	}
	
	public Password() {
		this.password = generatePassword();
	}
	
	private String generatePassword() {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder();
		do {
			String pass = Crypto.encodeBase64(String.valueOf(random.nextLong()).getBytes());
			pass = pass.replaceAll("=", "");  
			int digitPlace = random.nextInt(10);
			for(int i = 0; i < 9; i++) {
				if(i == digitPlace) {
					password.append(random.nextInt(10));
					continue;
				}
				password.append(pass.charAt(random.nextInt(pass.length())));
			}
		} while (!checkPassword(password.toString()));
		
		return password.toString();
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Double getStrength() {
		return getStrength(password);
	}
	
	public Double getStrength(String password) {
		Matcher matcher;
		Pattern pattern;
		
		Double counter = 0.0;
		
		if(password.length() >= 8) {
			counter += (password.length() + ((password.length() - 7) * 2.5) + 5 * 1.5) ;
		} else if(password.length() >=  5 && password.length() < 10) {
			counter += password.length() + password.length() * 1.5;
		} else {
			counter += password.length();
		}
		
		pattern = Pattern.compile("[A-Z]"); 
		matcher = pattern.matcher(password);
		
		if(matcher.find()) {
			counter += 2;
		}
		
		pattern = Pattern.compile("[a-z]"); 
		matcher = pattern.matcher(password);
		if(matcher.find()) {
			counter += 2;
		}
		
		pattern = Pattern.compile("[0-9]"); 
		matcher = pattern.matcher(password);
		if(matcher.find()) {
			counter += 2;
		}
		
		pattern = Pattern.compile("[^A-Za-z0-9]"); 
		matcher = pattern.matcher(password);
		if(matcher.find()) {
			counter += 2;
		}
		
		return counter;
	}
	
	public boolean checkPassword(String password) {
		Matcher matcher;
		Pattern pattern;
		
		pattern = Pattern.compile("[A-Z]"); 
		matcher = pattern.matcher(password);
		
		if(!matcher.find()) {
			return false;
		}
		
		pattern = Pattern.compile("[a-z]"); 
		matcher = pattern.matcher(password);
		if(!matcher.find()) {
			return false;
		}
		
		pattern = Pattern.compile("[0-9]"); 
		matcher = pattern.matcher(password);
		
		if(!matcher.find()) {
			return false;
		}
		
		return true;
	}
	
}
