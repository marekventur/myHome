package de.wi08e.myhome.usermanager;

import java.security.SecureRandom;

/**
 * Generates an userToken. Generating session tokens requires a secure random
 * number generator. Initializing SecureRandom is expensive, so this is done on
 * first usage and kept for further use.
 * 
 * A userToken contains 32 symbols from 0 to 9 and a to z.
 * 
 * Token generator adapted from
 * http://stackoverflow.com/questions/41107/how-to-generate
 * -a-random-alpha-numeric-string-in-java
 * 
 * Singleton pattern adapted from
 * http://en.wikipedia.org/wiki/Singleton_pattern#
 * ENUM_Type_Approach_to_Singleton_in_Java_5.0_and_higher
 * 
 * @author Marek
 * 
 */
public enum SessionUserToken {
	INSTANCE;

	// An array of symbols holding
	private static final char[] symbols = new char[36];
	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	// Length of the token
	private static final int length = 32;

	// A secure random generator 
	private final SecureRandom random = new SecureRandom();

	// A fixed length char buffer
	private final char[] buf = new char[length];

	public String generate() {
		for (int i = 0; i < symbols.length; ++i)
			buf[i] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}

}
