package com.github.template72.loader;

/**
 * This interface is used for loading template files.
 */
public interface TemplateLoader {

	/**
	 * @param filename name of the to be loaded template file
	 * @return content of the loaded template file
	 */
	String loadTemplate(String filename);
	
	/**
	 * The implementation must not support a parentLoader.
	 * 
	 * @param parentLoader the loader that should be called if the implementation itself can not load the file
	 */
	void setParentLoader(TemplateLoader parentLoader);
}
