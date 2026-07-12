package com.github.template72.exceptions;

public class MissingContentException extends RuntimeException {

	public MissingContentException(String name) {
		super("Missing content: " + name);
	}
	
	public MissingContentException(String name, String expectedClassName, Class<?> clazz) {
        super("The variable '" + name + "' is not of type " + expectedClassName + ", but has the unexpected type " + clazz.getSimpleName() + ".");
	}
}
