package com.github.template72.exceptions;

public class MissingContentException extends RuntimeException {

	public MissingContentException(String name) {
		super("Missing content: " + name);
	}
}
