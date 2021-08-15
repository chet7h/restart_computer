package hackathon.restart.computer.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class main {

	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode("123456");
	    System.out.println(encodedPassword);

	}

}
