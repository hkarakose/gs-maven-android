# Getting Started: Building Android Projects with Maven

What you'll build
-----------------

This guide walks you through using Maven to build a simple Android project.

What you'll need
----------------

 - About 15 minutes
 - A favorite text editor or IDE
 - [Android SDK][sdk]
 - An Android device or Emulator

[sdk]: http://developer.android.com/sdk/index.html

## {!include#how-to-complete-this-guide}

<a name="scratch"></a>
Set up the project
------------------

First, you will need to set up an Android project for Maven to build.  To keep the focus on Maven, make the project as simple as possible for now. If this is your first time working with Android projects, refer to [Getting Started with Android](../gs-android/README.md) to help configure your development environment.

{!include#create-directory-structure-org-hello}

{!include#create-android-manifest}

    {!include:complete/AndroidManifest.xml}

### Create a String Resource

    {!include:complete/res/values/strings.xml}

### Create a Layout

    {!include:complete/res/layout/hello_layout.xml}

### Create Java classes

Within the `src/main/java/org/hello` directory, you can create any Java classes you want. To maintain consistency with the rest of this guide, create the following class:

```java
package org.hello;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("Hello world!");
    }

}

```

### Install Maven

Now that you have a project that is ready to be built with Maven, the next step is to install Maven.

Maven is downloadable as a zip file at http://maven.apache.org/download.cgi. Only the binaries are required, so look for the link to apache-maven-_{version}_-bin.zip or apache-maven-_{version}_-bin.tar.gz.

Once you have downloaded the zip file, unzip it to your computer. Then add the _bin_ folder to your path.

To test the Maven installation, run `mvn` from the command-line like this:

```sh
$ mvn -v
```

If all goes well, you should be presented with some information about the Maven installation. It will look similar to the following:

```sh
Apache Maven 3.0.5 (r01de14724cdef164cd33c7c8c2fe155faf9602da; 2013-02-19 08:51:28-0500)
Maven home: /usr/local/apache-maven/apache-maven-3.0.5
Java version: 1.7.0_21, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.7.0_25.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.8.3", arch: "x86_64", family: "mac"
```

Congratulations! You now have Maven installed.


Define a simple Maven build
-----------------------------
Now that Maven is installed, you need to create a Maven project definition. Maven projects are defined with an XML file named _pom.xml_. Among other things, this file gives the project's name, version, and dependencies that it has on external libraries.

Create a file named _pom.xml_ at the root of the project and give it the following contents:

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.hello</groupId>
    <artifactId>gs-maven-android</artifactId>
    <version>0.1.0</version>
    <packaging>apk</packaging>

    <dependencies>
        <dependency>
            <groupId>com.google.android</groupId>
            <artifactId>android</artifactId>
            <version>4.1.1.4</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
            <plugin>
                <groupId>com.jayway.maven.plugins.android.generation2</groupId>
                <artifactId>android-maven-plugin</artifactId>
                <version>3.5.3</version>
                <configuration>
                    <sdk>
                        <platform>17</platform>
                    </sdk>
                    <deleteConflictingFiles>true</deleteConflictingFiles>
                    <undeployBeforeDeploy>true</undeployBeforeDeploy>
                </configuration>
                <extensions>true</extensions>
            </plugin>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.6</source>
                    <target>1.6</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

You will note that the `<packaging>` element specifies an *apk*. This is the simplest possible *pom.xml* file necessary to build an Android project. It includes the following details of the project configuration:

* `<modelVersion>` - The POM model version (always 4.0.0).
* `<groupId>` - The group or organization that the project belongs to. Often expressed as an inverted domain name.
* `<artifactId>` - The name to be given to the project's library artifact (e.g., the name of its APK file).
* `<version>` - The version of the project that is being built.
* `<packaging>` - How the project should be packaged, in this case as an Android APK.

The `<dependencies>` section declares a list of dependencies for our project. Specifically, it declares a single dependency for the Android library. Within the `<dependency>` element, the dependency coordinates are defined by three subelements:

* `<groupId>` - The group or organization that the dependency belongs to.
* `<artifactId>` - The library that is required.
* `<version>` - The specific version of the library that is required.

By default, all dependencies are scoped as `compile` dependencies. That is, they should be available at compile-time. In this case, you may note that we have specified a `<scope>` element with a value of `provided`. Dependencies of this type are required for compiling the project code, but will be provided at runtime by a container running the code. For example, the Android API's will always be available when running an Android application.

The `<build>` section declares additional configuration for building an application. Within the build section is a `<plugins>` section, which contains a list of plugins that add additional functionality to the build process. This is where we define the configuration for the [Android Maven Plugin]. As with dependencies, plugins also have `<groupId>`, `<artifactId>`, and `<version>` elements, and they behave as previously described. We also have the following new elements in our plugin declaration:

* `<configuration>` - The plugin specific configuration. Here we specify which Android Platform SDK to use in the build.
* `<extensions>` - The combination of specifying a value of `true` and `apk` for `<packaging>` directs the [Android Maven Plugin] to become involved in the build process


At this point we have a minimal, yet capable Maven project defined.


## Build Android code

Maven is now ready to build the project. You can execute several build lifecycle goals with Maven now, including goals to compile the project's code, create a library package (such as a JAR file), and install the library in the local Maven dependency repository.

To try out the build, issue the following at the command line:

```sh
$ mvn compile
```

This will run Maven, telling it to execute the _compile_ goal. When it's finished, you should find the compiled _.class_ files in the _target/classes_ directory.

Since it's unlikely that you'll want to distribute or work with _.class_ files directly, you'll probably want to run the _package_ goal instead:

```sh
$ mvn package
```

The *package* goal will compile your Java code, run any tests, and finish by packaging the code up in a JAR file within the *target* directory. The name of the JAR file will be based on the project's `<artifactId>` and `<version>`. For example, given the minimal *pom.xml* file from before, the JAR file will be named *gs-maven-android-0.1.0.jar*.

Because we changed the value of `<packaging>` from "jar" to "apk", the result will be an APK file within the *target* directory in addition to the JAR file. This APK file is now a packaged Android application ready to be deployed to a device or emulator.

The Android Maven Plugin provides several more Maven goals that can be used to initiate the various phases of the build process, or interact with the device and emulator. You can see a list of all the available goals by running the following command:

```sh
$ mvn android:help
```


## Declaring Dependencies

Our simple Hello World sample is completely self-contained and does not depend on any additional libraries. Most application, however, depend on external libraries to handle common and/or complex functionality.

For example, suppose we want our application to print the current date and time. While we could use the date and time facilities in the native Java libraries, we can make things more interesting by using the Joda Time libraries.

To do this, modify HelloActivity.java to look like this:

{!include:complete/src/main/java/org/hello/HelloActivity.java}

In this example, we are using Joda Time's `LocalTime` class to retrieve and display the current time. 

If we were to run `mvn package` to build our project now, the build would fail because we have not declared Joda Time as a compile dependency in our build. We can fix that by adding the following lines to `<dependencies>` section of the *pom.xml*:

```xml
<dependency>
    <groupId>joda-time</groupId>
    <artifactId>joda-time</artifactId>
    <version>2.2</version>
</dependency>
```

Similar to the Android dependency we discussed earlier, this block of XML declares a new dependency for our project, specifically the Joda Time library.

Now if you run `mvn compile` or `mvn package`, Maven should resolve the Joda Time dependency from the Maven Central repository and the build will be successful.

Here's the completed pom.xml file:

    {!include:complete/pom.xml}


## Next Steps

Congratulations! You have now created a very simple, yet effective Maven project definition for building Java projects.


[Android Maven Plugin]:https://code.google.com/p/maven-android-plugin
[Android Manifest]:http://developer.android.com/guide/topics/manifest/manifest-intro.html
