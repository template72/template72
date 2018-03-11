package com.github.template72.loader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
		return loadResource(getClass(), prefix + resourceFileName + postfix, charsetName());
	}
	
	/**
	 * @return null for default charset, otherwise a valid name of a charset, e.g. "UTF-8"
	 */
	public String charsetName() {
		return null;
	}
	
	public static String loadResource(final Class<?> clazz, final String resourceFileName, String charsetName) {
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
					return getContent(fs.getPath(filenameParts[1]), charsetName);
				}
			} else {
				return getContent(Paths.get(resourceURI), charsetName);
			}
		} catch (IOException | URISyntaxException e) {
			throw new TemplateLoadException("Template can not be loaded. Error loading resource file: " + resourceFileName, e);
		}
	}

	private static String getContent(Path path, String charsetName) throws IOException, UnsupportedEncodingException {
		loadOperations.incrementAndGet();
		byte[] bytes = Files.readAllBytes(path);
		if (charsetName == null) {
			return new String(bytes);
		} else {
			return new String(bytes, charsetName);
		}
	}
	
	public static int getLoadOperations() {
		return loadOperations.get();
	}

	@Override
	public void setParentLoader(TemplateLoader parentLoader) {
	}
}
