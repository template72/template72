package com.github.template72.exceptions;

public class TemplateLoadException extends RuntimeException {

	public TemplateLoadException(String msg) {
		super(msg);
	}

	public TemplateLoadException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
