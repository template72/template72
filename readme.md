[![Release](https://jitpack.io/v/template72/template72.svg)](https://jitpack.io/#template72/template72)

# template72 Template Engine

template72 is an open-source Java 8 template engine. It needs no external dependencies.
template72 template files contain nearly no logic. There are only 4 commands: if, each, include, master.
All the logic must be in the Java files. Reflection is not used. Logic calculations or functions call are not possible. Even no implicit toString() calls. template72 can only take these data types: String, boolean, list and map. The template files must be in the '/templates/' folder and must have the file extension '.html'. But you can customize everything; see wiki for details. The syntax is very strict and case-sensitive.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Building](#building)
- [Contributing](#contributing)
- [License](#license)

## Installation

Download [the latest JAR](http://jitpack.io/com/github/template72/template72/0.1.0/template72-0.1.0.jar) or grab via Gradle:

```gradle
dependencies {
	compile 'com.github.template72:template72:0.1.0'
}

repositories {
	...
	maven { url 'https://jitpack.io' }
}
```

or Maven:

```xml
<dependency>
	<groupId>com.github.template72</groupId>
	<artifactId>template72</artifactId>
	<version>0.1.0</version>
</dependency>

<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

See [jitpack.io](https://jitpack.io/#template72/template72) for other build tools or other versions.
template72 requires at minimum Java 8.


## Usage

### Hello world example

The template file /templates/index.html looks like:

```html
<html><body>{{message}}</body></html>
```

```java
String html = Template.createFromFile("index").put("message", "Hello world").render();
```

After executing the code above html contains:

```html
<html><body>Hello world</body></html>
```

### Using lists and maps

Imagine a list of Person objects. Each person has got a name and an address. The template /templates/person-list.html would look like this:

```html
<ul>
	{{each p in persons}}
		<li>{{p.name}}, address: {{p.address}}</li>
	{{/each}}
</ul>
```

```java
Template template = Template.createFromFile("person-list");
DataList persons = template.list("persons");
personList.forEach(p -> {
	DataMap map = persons.add();
	map.put("name", p.getName());
	map.put("address", p.getAddress());
});
String html = template.render();
```

### Conditional output

Here's a little example for using the if command.

```html
{{if hasNoPersons}}
	<p>There are no persons.</p>
{{else}}
	<ul>
		{{each p in persons}}
			<li>{{p.name}}, address: {{p.address}}</li>
		{{/each}}
	</ul>
{{/if}}
```

```java
template.put("hasNoPersons", personList.isEmpty());
```

### DataMap

With the Template class you can easily load a template file, add data and output the result.

But you are not obliged to use the Template class. The data can also be provided via the IDataMap interface. An IDataMap contains name/IDataItem values. DataMap is the default implementation.

template72 provides only 4 IDataItem types: DataValue for String, DataCondition for boolean, DataList for list, and DataMap for object or map.

A Map&lt;String, String&gt; Object can be added to a DataMap using the putAll() method.

### Real world

Use the CompiledTemplates class for real world scenarios. Read more about it in the Wiki.

## Building

Checkout the files and build with Gradle 3.3 on Windows using `gradlew.bat build` or on Linux/Mac using `./gradlew build`.
There's also a Windows-specific Ant wrapper file build.xml that you can easily use in the Eclipse Ant view.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## License

MIT
