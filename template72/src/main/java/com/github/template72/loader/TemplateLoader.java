package com.github.template72.loader;

public interface TemplateLoader {

	/**
	 * @param filename name of the to be loaded template file
	 * @return content of the loaded template file
	 */
	String loadTemplate(String filename);
	
	/**
	 * The implementation must not support a parentLoader.
	 * @param parentLoader
	 */
	void setParentLoader(TemplateLoader parentLoader);
}
