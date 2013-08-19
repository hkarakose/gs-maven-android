This guide walks you through the process of building a simple Android project with Maven.

What you'll build
-----------------

You'll create an Android application that gives you the time of day, and then build it with Maven.

What you'll need
----------------

 - About 15 minutes
 - A favorite text editor or IDE
 - [Android SDK][sdk]
 - An Android device or Emulator

[sdk]: http://developer.android.com/sdk/index.html

How to complete this guide
--------------------------

Like all Spring's [Getting Started guides](/guides/gs), you can start from scratch and complete each step, or you can bypass basic setup steps that are already familiar to you. Either way, you end up with working code.

To **start from scratch**, move on to [Set up the project](#scratch).

To **skip the basics**, do the following:

 - [Download][zip] and unzip the source repository for this guide, or clone it using [Git][u-git]:
`git clone https://github.com/springframework-meta/gs-maven-android.git`
 - cd into `gs-maven-android/initial`.
 - Jump ahead to [Install Maven](#initial).

**When you're finished**, you can check your results against the code in `gs-maven-android/complete`.
[zip]: https://github.com/springframework-meta/gs-maven-android/archive/master.zip
[u-git]: /understanding/Git


<a name="scratch"></a>
Set up the project
------------------

First, you set up an Android project for Maven to build.  To keep the focus on Maven, make the project as simple as possible for now. If this is your first time working with Android projects, refer to [Installing the Android Development Environment][gs-android] to help configure your development environment.

### Create the directory structure

In a project directory of your choosing, create the following subdirectory structure; for example, with the following command on Mac or Linux:

```sh
$ mkdir -p src/main/java/org/hello
```

    └── src
        └── main
            └── java
                └── org
                    └── hello

### Create an Android manifest

The [Android Manifest] contains all the information required to run an Android application, and it cannot build without one.

[Android Manifest]: http://developer.android.com/guide/topics/manifest/manifest-intro.html

`AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.hello"
    android:versionCode="1"
    android:versionName="1.0.0" >

    <application android:label="@string/app_name" >
        <activity
            android:name=".HelloActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### Create a string resource
Add a text string. Text strings can be referenced from the application or from other resource files.

`res/values/strings.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Android Maven</string>
</resources>
```

### Create a layout
Here you define the visual structure for the user interface of your application.

`res/layout/hello_layout.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    >
<TextView  
    android:id="@+id/text_view"
    android:layout_width="fill_parent" 
    android:layout_height="wrap_content" 
    />
</LinearLayout>
```

### Create Java classes

Within the `src/main/java/org/hello` directory, you can create any Java classes you want. To maintain consistency with the rest of this guide, create the following class:

`src/main/java/org/hello/HelloActivity.java`
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


<a name="initial"></a>
Install Maven
-------------

Now you have a project that you can build with Maven. The next step is to install Maven.

Maven is downloadable as a zip file at http://maven.apache.org/download.cgi. Only the binaries are required, so look for the link to apache-maven-{version}-bin.zip or apache-maven-{version}-bin.tar.gz.

Download and unzip the file, then add the _bin_ folder to your path.

To test the Maven installation, run `mvn` from the command-line:

```sh
$ mvn -v
```

If all goes well, you should see installation information like this:

```sh
Apache Maven 3.0.5 (r01de14724cdef164cd33c7c8c2fe155faf9602da; 2013-02-19 08:51:28-0500)
Maven home: /usr/local/apache-maven/apache-maven-3.0.5
Java version: 1.7.0_21, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.7.0_25.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.8.3", arch: "x86_64", family: "mac"
```

You now have Maven installed.


Define a simple Maven build
---------------------------

Now that Maven is installed, you need to create a Maven project definition. You define Maven projects with an XML file named _pom.xml_. Among other things, this file gives the project's name, version, and dependencies that it has on external libraries.

Create a file named pom.xml at the root of the project and give it the following contents:

`pom.xml`
```xml
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
        <plugins>
            <plugin>
                <groupId>com.jayway.maven.plugins.android.generation2</groupId>
                <artifactId>android-maven-plugin</artifactId>
                <version>3.6.1</version>
                <configuration>
                    <sdk>
                        <platform>18</platform>
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

The `<packaging>` element specifies an *apk*. This is the simplest possible *pom.xml* file necessary to build an Android project. It includes the following details of the project configuration:

* `<modelVersion>`. POM model version (always 4.0.0).
* `<groupId>`. Group or organization that the project belongs to. Often expressed as an inverted domain name.
* `<artifactId>`. Name to be given to the project's library artifact (for example, the name of its APK file).
* `<version>`. Version of the project that is being built.
* `<packaging>`. How the project should be packaged, in this case as an Android APK.

The `<dependencies>` section declares a list of dependencies for the project. Specifically, it declares a single dependency for the Android library. Within the `<dependency>` element, the dependency coordinates are defined by three subelements:

* `<groupId>`. Group or organization that the dependency belongs to.
* `<artifactId>`. Library that is required.
* `<version>`. Specific version of the library that is required.
* `<scope>`. Scoped as `compile` dependencies by default. That is, all dependencies should be available at compile-time.

In this case, the `<scope>` element has a value of `provided`. Dependencies of this type are required for compiling the project code, but will be provided at runtime by a container running the code. For example, the Android APIs are always available when an Android application is running.

The `<build>` section declares additional configuration for building an application. Within the build section is a `<plugins>` section, which contains a list of plugins that add additional functionality to the build process. This is where you define the configuration for the [Android Maven Plugin]. As with dependencies, plugins also have `<groupId>`, `<artifactId>`, and `<version>` elements, and they behave as previously described. The plugin declaration also has these elements:

* `<configuration>`. Plugin-specific configuration. Here you specify which Android Platform SDK to use in the build.
* `<extensions>`. Combination of specifying a value of `true` and `apk` for `<packaging>` directs the [Android Maven Plugin] to become involved in the build process.


At this point you have defined a minimal yet capable Maven project.


Build Android code
------------------

Maven is now ready to build the project. You can execute several build lifecycle goals with Maven now, including goals to compile the project's code, create a library package (such as a JAR file), and install the library in the local Maven dependency repository.

Try out the build:

```sh
$ mvn compile
```

This command runs Maven, telling it to execute the _compile_ goal. When it's finished, you should find the compiled _.class_ files in the _target/classes_ directory.

Because it's unlikely that you'll want to distribute or work with .class files directly, you'll probably want to run the _package_ goal instead:

```sh
$ mvn package
```

The package goal compiles your Java code, runs any tests, and packages the code in a JAR file within the *target* directory. The name of the JAR file is based on the project's `<artifactId>` and `<version>`. For example, given the minimal pom.xml file shown earlier, the JAR file will be named gs-maven-android-0.1.0.jar.

Because you set the value of `<packaging>` to "apk", the result will be an APK file within the *target* directory in addition to the JAR file. This APK file is now a packaged Android application ready to be deployed to a device or emulator.

The Android Maven plugin provides several more Maven goals that you can use to initiate the various phases of the build process, or interact with the device and emulator. You can see a list of all the available goals by running the following command:

```sh
$ mvn android:help
```


Declare dependencies
--------------------

The simple Hello World sample is completely self-contained and does not depend on any additional libraries. Most applications, however, depend on external libraries to handle common and/or complex functionality.

For example, suppose you want your application to print the current date and time. Although you could use the date and time facilities in the native Java libraries, you can make things more interesting by using the Joda Time libraries.

To do this, modify HelloActivity.java to look like this:

`src/main/java/org/hello/HelloActivity.java`
```java
package org.hello;

import org.joda.time.LocalTime;

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
        LocalTime currentTime = new LocalTime();
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("The current local time is: " + currentTime);
    }

}
```

In this example, you use Joda Time's `LocalTime` class to retrieve and display the current time. 

If you were to run `mvn package` to build the project now, the build would fail because you have not declared Joda Time as a compile dependency in the build. You can fix that by adding the following lines to `<dependencies>` section of the *pom.xml*:

```xml
<dependency>
    <groupId>joda-time</groupId>
    <artifactId>joda-time</artifactId>
    <version>2.2</version>
</dependency>
```

Similar to the Android dependency discussed earlier, this block of XML declares a new dependency for the project, specifically the Joda Time library.

Rebuild Android code with dependencies
--------------------------------------

Now if you run `mvn compile` or `mvn package`, Maven should resolve the Joda Time dependency from the Maven Central repository and the build will be successful.

Here's the completed pom.xml file:

`pom.xml`
```xml
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
        <plugins>
            <plugin>
                <groupId>com.jayway.maven.plugins.android.generation2</groupId>
                <artifactId>android-maven-plugin</artifactId>
                <version>3.6.1</version>
                <configuration>
                    <sdk>
                        <platform>18</platform>
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


Summary
-------

Congratulations! You have created a simple yet effective Maven project definition for building Java projects.


[Android Maven Plugin]:https://code.google.com/p/maven-android-plugin
[Android Manifest]:http://developer.android.com/guide/topics/manifest/manifest-intro.html
[gs-android]: /guides/gs/android