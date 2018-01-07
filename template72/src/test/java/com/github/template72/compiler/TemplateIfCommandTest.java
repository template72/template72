package com.github.template72.compiler;

import org.junit.Test;

public class TemplateIfCommandTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullArgs1() {
		new TemplateIfCommand(null, "not ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullArgs2() {
		new TemplateIfCommand("cond", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyArgs2() {
		new TemplateIfCommand("cond", " ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyArgsd2() {
		new TemplateIfCommand("cond", "not ").addCondition(null);
	}
}
