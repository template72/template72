package com.github.template72.syntax;

import java.util.regex.Matcher;

public interface TemplateSyntax {
	
	TemplateSyntax DEFAULT = new TemplateSyntaxBuilder().build();

	String start();

	String end();
	
	String fieldSep();
	
	String notPrefix();
	
	boolean isElse(String cmd);
	
	boolean isEndIf(String cmd);
	
	boolean isEndEach(String cmd);

	Matcher varPattern(String cmd);
	
	Matcher ifPattern(String cmd);
	
	Matcher elseifPattern(String cmd);
	
	Matcher eachPattern(String cmd);
	
	Matcher includePattern(String template);
	
	Matcher masterPattern(String template);
	
	String content();

	String internalFieldSep = ".";

	String split(String name);
	
	String commentStart();
	
	String commentEnd();
}
