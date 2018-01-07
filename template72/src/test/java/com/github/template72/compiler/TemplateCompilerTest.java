package com.github.template72.compiler;

import org.junit.Test;

public class TemplateCompilerTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullArg() {
		new TemplateCompiler(null);
	}
}
