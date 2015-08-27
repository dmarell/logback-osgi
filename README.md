# logback-osgi

  This OSGi bundle implements an OSGi logging backend using logback.

  This code is originating from {{http://code.google.com/p/osgi-logging}}. The version of this module reflects
  the version of its Logback implementation.

## Release notes

* Version 1.1.1_5 - 2015-08-27
  * Moved to github
* Version 1.1.1_4 - 2014-02-09
  * Updated for OSGi R5
  * Updated for new slf4j
* Version 1.1.1_3 - 2014-02-08
  * Based on logback 1.1.1
  * Java 7
  * Changed pom versioning mechanism.
  * Extended site information.
  * Updated versions of dependencies
* Version 1.0.6_1 - 2012-07-01
  * First version. Based on logback 1.0.6.

## Maven usage

```
<dependency>
  <groupId>se.marell</groupId>
  <artifactId>logback-osgi</artifactId>
  <version>1.1.1_5</version>
</dependency>
```

## Usage

This bundle is simple to use:

* Drop the bundle into you OSGi container and start it

* Create a logback configuration file according to the
[logback documentation](http://logback.qos.ch/manual/configuration.html) and put the file in a
location reachable at runtime

* Define the logback.configurationFile property with the location of the configuration file. For example:

```
java -Dlogback.configurationFile=/path/to/logback.xml ...
```
