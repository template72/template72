package com.github.template72.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.template72.exceptions.TemplateLoadException;

public class ResourceTemplateLoader implements TemplateLoader {
	private static final AtomicInteger loadOperations = new AtomicInteger();
	private final String prefix;
	private final String postfix;

	/**
	 * Default constructor with prefix "/templates/" and postfix ".html".
	 */
	public ResourceTemplateLoader() {
		this("/templates/", ".html");
	}

	/**
	 * @param prefix e.g. "/templates/"
	 * <br>Should usually end with "/".
	 * <br>Should usually not be empty; at least "/".
	 * @param postfix filename extension, e.g. ".html"
	 */
	public ResourceTemplateLoader(String prefix, String postfix) {
		if (prefix == null) {
			throw new IllegalArgumentException("prefix must not be null");
		} else if (postfix == null) {
			throw new IllegalArgumentException("postfix must not be null");
		}
		this.prefix = prefix;
		this.postfix = postfix;
	}
	
	@Override
	public String loadTemplate(final String resourceFileName) {
		return loadResource(getClass(), prefix + resourceFileName + postfix, charsetName());
	}
	
	/**
	 * @return null for default charset, otherwise a valid name of a charset, e.g. "UTF-8"
	 */
	public String charsetName() {
		return null;
	}
	
	public static String loadResource(final Class<?> clazz, final String resourceFileName, String charsetName) {
		URL resource = clazz.getResource(resourceFileName);
		if (resource == null) {
			throw new TemplateLoadException("Template can not be loaded. Resource file not found: " + resourceFileName);
		}
		try (Scanner s = (charsetName == null ? new Scanner(resource.openStream()) : new Scanner(resource.openStream(), charsetName))) {
			String str = s.useDelimiter("\\A").hasNext() ? s.next() : "";
			loadOperations.incrementAndGet();
			return str;
		} catch (IOException e) {
			throw new TemplateLoadException("Template can not be loaded. Error loading resource file: " + resourceFileName, e);
		}
	}
	
	public static int getLoadOperations() {
		return loadOperations.get();
	}

	@Override
	public void setParentLoader(TemplateLoader parentLoader) {
	}
}
