package com.github.template72.loader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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
		return loadResource(getClass(), prefix + resourceFileName + postfix);
	}
	
	public static String loadResource(final Class<?> clazz, final String resourceFileName) {
		try {
			URL resource = clazz.getResource(resourceFileName);
			if (resource == null) {
				throw new TemplateLoadException("Template can not be loaded. Resource file not found: " + resourceFileName);
			}
	
			URI resourceURI = resource.toURI();
			String r = resourceURI.toString();
			if (r.contains("!")) {
				String[] filenameParts = r.split("!"); // 0: JAR file, 1: file within
				try (FileSystem fs = FileSystems.newFileSystem(URI.create(filenameParts[0]), new HashMap<>())) {
					Path path = fs.getPath(filenameParts[1]);
					String content = new String(Files.readAllBytes(path));
					loadOperations.incrementAndGet();
					return content;
				}
			} else {
				Path path = Paths.get(resourceURI);
				String content = new String(Files.readAllBytes(path));
				loadOperations.incrementAndGet();
				return content;
			}
		} catch (IOException | URISyntaxException e) {
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
