package com.github.template72.syntax;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StaticTemplateSyntax implements TemplateSyntax {
	private final String start, end, fieldSep, notPrefix, elseTag, endIf, endEach, content, commentStart, commentEnd;
	private final Pattern varPattern, ifPattern, elseifPattern, eachPattern, masterPattern, includePattern;
	
	StaticTemplateSyntax(String start, String end, String fieldSep, Pattern varPattern,
			Pattern ifPattern, String notPrefix, String elseTag, Pattern elseifPattern, String endIf,
			Pattern eachPattern, String endEach,
			Pattern masterPattern, String content,
			Pattern includePattern,
			String commentStart, String commentEnd) {
		this.start = start;
		this.end = end;
		this.fieldSep = fieldSep;
		this.varPattern = varPattern;
		
		this.ifPattern = ifPattern;
		this.notPrefix = notPrefix;
		this.elseifPattern = elseifPattern;
		this.elseTag = elseTag;
		this.endIf = endIf;
		
		this.eachPattern = eachPattern;
		this.endEach = endEach;
		
		this.masterPattern = masterPattern;
		this.content = content;
		
		this.includePattern = includePattern;

		this.commentStart = commentStart;
		this.commentEnd = commentEnd;
	}

	@Override
	public String start() {
		return start;
	}

	@Override
	public String end() {
		return end;
	}

	@Override
	public String fieldSep() {
		return fieldSep;
	}

	@Override
	public String notPrefix() {
		return notPrefix;
	}

	@Override
	public boolean isElse(String cmd) {
		return elseTag.equals(cmd);
	}

	@Override
	public boolean isEndIf(String cmd) {
		return endIf.equals(cmd);
	}

	@Override
	public boolean isEndEach(String cmd) {
		return endEach.equals(cmd);
	}

	@Override
	public Matcher varPattern(String cmd) {
		return varPattern.matcher(cmd);
	}

	@Override
	public Matcher ifPattern(String cmd) {
		return ifPattern.matcher(cmd);
	}

	@Override
	public Matcher elseifPattern(String cmd) {
		return elseifPattern.matcher(cmd);
	}

	@Override
	public Matcher eachPattern(String cmd) {
		return eachPattern.matcher(cmd);
	}

	@Override
	public Matcher includePattern(String template) {
		return includePattern.matcher(template);
	}

	@Override
	public Matcher masterPattern(String template) {
		return masterPattern.matcher(template);
	}

	@Override
	public String content() {
		return content;
	}

	@Override
	public String split(String name) {
		if (fieldSep.equals(internalFieldSep)) {
			return name;
		}
		return Arrays.asList(name.split(fieldSep)).stream().collect(Collectors.joining(internalFieldSep));
	}

	@Override
	public String commentStart() {
		return commentStart;
	}

	@Override
	public String commentEnd() {
		return commentEnd;
	}
}
