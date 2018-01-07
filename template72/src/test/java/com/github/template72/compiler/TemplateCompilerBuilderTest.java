package com.github.template72.compiler;

import org.junit.Assert;
import org.junit.Test;

import com.github.template72.compiler.preprocessor.TemplatePreprocessor;
import com.github.template72.data.DataMap;
import com.github.template72.exceptions.UnknownCommandException;

public class TemplateCompilerBuilderTest {

	@Test(expected = IllegalArgumentException.class)
	public void withSyntax_null() {
		new TemplateCompilerBuilder().withSyntax(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void withLoader_null() {
		new TemplateCompilerBuilder().withLoader(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void withPreprocessor_null() {
		new TemplateCompilerBuilder().withPreprocessor(null);
	}

	@Test
	public void withPreprocessor() {
		TemplatePreprocessor aSimplePreprocessor = new TemplatePreprocessor() {
			@Override
			public String render(String template) {
				return template.replace("\\{{", "{{start}}");
			}
		};
		TemplateCompiler compiler = new TemplateCompilerBuilder().withPreprocessor(aSimplePreprocessor).build();
		String html = compiler.compile("\\{{1, 2}, 3}").render(new DataMap().put("start", "{{"));
		Assert.assertEquals("{{1, 2}, 3}", html);
	}

	@Test(expected = UnknownCommandException.class)
	public void withoutLoader() {
		new TemplateCompilerBuilder().withoutLoader().build().compile("{{master: master}}A");
	}

	@Test(expected = UnknownCommandException.class)
	public void withoutPreprocessing() {
		new TemplateCompilerBuilder().withoutPreprocessing().build().compile("{{-- comment --}}A");
	}
}
