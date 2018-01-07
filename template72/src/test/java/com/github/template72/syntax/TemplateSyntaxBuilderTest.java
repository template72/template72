package com.github.template72.syntax;

import org.junit.Test;

public class TemplateSyntaxBuilderTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullArgs1() {
		new TemplateSyntaxBuilder(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyArgs1() {
		new TemplateSyntaxBuilder(" ", " ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullArgs2() {
		new TemplateSyntaxBuilder("{{", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyArgs2() {
		new TemplateSyntaxBuilder("{{", " ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalTags() {
		new TemplateSyntaxBuilder("|@", "@");
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalTags2() {
		new TemplateSyntaxBuilder("@", "@.");
	}

	@Test(expected = IllegalArgumentException.class)
	public void withFieldSeparator_null() {
		new TemplateSyntaxBuilder().withFieldSeparator(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalFieldSeparator1() {
		new TemplateSyntaxBuilder().withFieldSeparator("{{");
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalFieldSeparator2() {
		new TemplateSyntaxBuilder().withFieldSeparator("}}");
	}
}
