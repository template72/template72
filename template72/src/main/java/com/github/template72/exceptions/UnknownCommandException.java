package com.github.template72.exceptions;

public class UnknownCommandException extends RuntimeException {

	public UnknownCommandException(String cmd) {
		super("Unknown command '" + cmd + "'!");
	}
}
