package com.github.template72.syntax;

import java.util.regex.Pattern;

public class TemplateSyntaxBuilder {
	private final String start;
	private final String end;
	private String fieldSep = ".";
	private String varPattern = null;

	private String ifPattern = null;
	private String notPrefix = "not ";
	private String elseifPattern = null;
	private String elseTag = "else";
	private String endIf = "/if";
	
	private String eachPattern = null;
	private String endEach = "/each";
	
	private String masterPattern = null;
	private String content = null;

	private String includePattern = null;
	
	private String commentStart = null;
	private String commentEnd = null;
	
	public TemplateSyntaxBuilder() {
		this("{{", "}}");
	}
	
	public TemplateSyntaxBuilder(String start, String end) {
		if (start == null || start.trim().isEmpty()) {
			throw new IllegalArgumentException("start must not be empty");
		} else if (end == null || end.trim().isEmpty()) {
			throw new IllegalArgumentException("end must not be empty");
		} else if (start.contains(end) || end.contains(start)) {
			throw new IllegalArgumentException("start must not contain end and vice versa");
		}
		this.start = start;
		this.end = end;
	}
	
	public TemplateSyntaxBuilder withFieldSeparator(String v) {
		if (v == null || (v.trim().isEmpty() && !" ".equals(v))) { // " " as separator is allowed
			throw new IllegalArgumentException("fieldSep must not be empty");
		} else if (start.contains(v) || end.contains(v)) {
			throw new IllegalArgumentException("fieldSep must not be contained in start or end");
		}
		fieldSep = v;
		return this;
	}

	public TemplateSyntaxBuilder withVarPattern(String v) {
		varPattern = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withIfPattern(String v) {
		ifPattern = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withNotPrefix(String v) {
		if (v == null || v.trim().isEmpty()) {
			throw new IllegalArgumentException("notPrefix must not be empty");
		}
		notPrefix = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withElseifPattern(String v) {
		elseifPattern = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withElse(String v) {
		if (v == null || v.trim().isEmpty()) {
			throw new IllegalArgumentException("else must not be empty");
		}
		elseTag = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withEndIf(String v) {
		if (v == null || v.trim().isEmpty()) {
			throw new IllegalArgumentException("endIf must not be empty");
		}
		endIf = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withEachPattern(String v) {
		eachPattern = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withEndEach(String v) {
		if (v == null || v.trim().isEmpty()) {
			throw new IllegalArgumentException("endEach must not be empty");
		}
		endEach = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withMasterPattern(String v) {
		masterPattern = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withContent(String v) {
		content = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withIncludePattern(String v) {
		includePattern = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withCommentStart(String v) {
		commentStart = v;
		return this;
	}
	
	public TemplateSyntaxBuilder withCommentEnd(String v) {
		commentEnd = v;
		return this;
	}
	
	public TemplateSyntax build() {
		setActualValues();
		return new StaticTemplateSyntax(start, end, fieldSep, Pattern.compile(varPattern),
				Pattern.compile(ifPattern), notPrefix, elseTag, Pattern.compile(elseifPattern), endIf,
				Pattern.compile(eachPattern), endEach,
				Pattern.compile(masterPattern), content,
				Pattern.compile(includePattern),
				commentStart, commentEnd);
	}
	
	protected void setActualValues() {
		final String objectNotation = "[a-zA-Z0-9_" + Pattern.quote(fieldSep) + "]+";
		final String ifNotation = "((" + Pattern.quote(notPrefix) + ")?[a-zA-Z0-9_" + Pattern.quote(fieldSep) + "]+)";
		final String fieldNameNotation = "([a-zA-Z0-9_]+)"; // must not contain fieldSep !!
		final String filenameNotation = "([a-zA-Z0-9_][a-zA-Z0-9_ \\.\\-/]*)";
		
		// Without start and end
		if (varPattern == null) {
			varPattern = objectNotation;
		}
		if (ifPattern == null) { // "if C"
			ifPattern = Pattern.quote("if ") + ifNotation;
		}
		if (elseifPattern == null) { // "else if C"
			elseifPattern = Pattern.quote("else if ") + ifNotation;
		}
		if (eachPattern == null) { // "each V in L"
			eachPattern = Pattern.quote("each ") + fieldNameNotation + Pattern.quote(" in ") + "(" + objectNotation + ")";
		}
		
		// With start and/or end = standard preprocessor tags
		if (masterPattern == null) { // "{{master F}}" + CRLF (in first line)
			masterPattern = Pattern.quote(start + "master: ") + filenameNotation + Pattern.quote(end) + "(\r\n|\n|\r)";
		}
		if (content == null) { // "{{content}}"
			content = start + "content" + end;
		}
		if (includePattern == null) { // "{{include F}}"
			includePattern = Pattern.quote(start + "include: ") + filenameNotation + Pattern.quote(end);
		}
		if (commentStart == null) { // "{{-- "
			commentStart = start + "-- ";
		}
		if (commentEnd == null) { // " --}}"
			commentEnd = " --" + end;
		}
	}
}
