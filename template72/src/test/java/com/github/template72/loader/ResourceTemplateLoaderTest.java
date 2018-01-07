package com.github.template72.loader;

import org.junit.Assert;
import org.junit.Test;

import com.github.template72.exceptions.TemplateLoadException;

public class ResourceTemplateLoaderTest {

	@Test(expected = IllegalArgumentException.class)
	public void firstArgNull() {
		new ResourceTemplateLoader(null, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void secondArgNull() {
		new ResourceTemplateLoader("/", null);
	}

	@Test(expected = TemplateLoadException.class)
	public void resourceNotFound() {
		new ResourceTemplateLoader().loadTemplate("NOT-EXISTS");
	}

	@Test
	public void setParentLoader() {
		new ResourceTemplateLoader().setParentLoader(null);
	}

	@Test
	public void loadResourceFromProject() {
		ResourceTemplateLoader.loadResource(getClass(), "/templates/page1.html");
	}

	@Test
	public void loadResourceFromJAR() {
		// The test file testjar.jar is a Zip file that just contains 'templates/dummy.html'.
		Assert.assertEquals("test file for loader", ResourceTemplateLoader.loadResource(getClass(), "/templates/dummy.html"));
	}
}
