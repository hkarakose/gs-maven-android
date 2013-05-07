# Building Android Projects with Maven

This Getting Started guide will walk you through a few basic steps of setting up and using Maven to build an Android application.

## Downloads

Within this repository are two directories, [`complete`] and [`start`]. [`complete`] contains the full working copy of the code being demonstrated in this guide. [`start`] contains the initial project setup, but otherwise empty code files. You may use the [`start`] folder as a way to quickly begin working through the implementation steps.

Using [git], clone the repository with the following command:

```sh
$ git clone https://github.com/springframework-meta/gs-maven-android.git
```

> Note: if you are unfamiliar with [git], then you may want to try the [GitHub for Mac] or [GitHub for Windows] clients.


## Installing the Android Development Environment

Building Android applications requires the installation of the [Android SDK].

### Install the Android SDK

1. Download the correct version of the [Android SDK] for your operating system from the Android web site.

2. Unzip the archive and place it in a location of your choosing. For example on Linux or Mac, you may want to place it in the root of your user directory. See the [Android Developers] web site for additional installation details.

3. To use the [Android Maven Plugin] we need to configure the `ANDROID_HOME` environment variable based on the location where you installed the Android SDK. Additionally, you should consider adding `ANDROID_HOME/tools` and `ANDROID_HOME/platform-tools` to your PATH.

	Mac OS X:

	```sh
	$ export ANDROID_HOME=/<installation location>/android-sdk-macosx
    $ export PATH=${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
    ```
    
    Linux:
    
    ```sh
    $ export ANDROID_HOME=/<installation location>/android-sdk-linux
    $ export PATH=${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
    ```
	    
    Windows:
    
    ```sh
    set ANDROID_HOME=C:\<installation location>\android-sdk-windows
    set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools
    ```

4. Once the SDK is installed, we need to add the relevant [Platforms and Packages]. We are using Android 4.2.2 (API 17) in this guide.

### Install Android SDK Platforms and Packages

The Android SDK download does not include any specific Android platform SDKs. In order to run the sample code you need to download and install the latest SDK Platform. You accomplish this by using the Android SDK and AVD Manager that was installed from the previous step.

1. Open the Android SDK Manager window:

	```sh
	$ android
	```

	> Note: if this command does not open the Android SDK Manager, then your path is not configured correctly.
	
2. Select the checkbox for *Tools*

3. Select the checkbox for the latest Android SDK, "Android 4.2.2 (API 17)" as of this writing

4. Select the checkbox for the *Android Support Library* from the *Extras* folder

5. Click the **Install packages...** button to complete the download and installation.

	> Note: you may want to simply install all the available updates, but be aware it will take longer, as each SDK level is a sizable download.



## Installing Maven

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
Java home: /Library/Java/JavaVirtualMachines/jdk1.7.0_21.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.8.3", arch: "x86_64", family: "mac"
```

Congratulations! You now have Maven installed.


## Defining a Simple Maven Build

Now that Maven is installed, we need to create a Maven project definition. Maven projects are defined with an XML file named *pom.xml*. Among other things, this file expresses the project's name, version, and any dependencies that it has on external libraries.

To get started, create a file named _pom.xml_ at the root of the project and give it the following contents:

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>org.springframework.gs</groupId>
	<artifactId>gs-maven-android-start</artifactId>
	<version>1.0.0</version>
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
				<version>3.5.3</version>
				<configuration>
					<sdk>
						<platform>17</platform>
					</sdk>
				</configuration>
				<extensions>true</extensions>
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


At this point we have a minimal, but capable Maven project defined.


## Defining a Simple Android Manifest

Every Android application must have an AndroidManifest.xml file. The [Android Manifest] contains all the information required to run an Android application, and it cannot build without one.

Create an AndroidManifest.xml file at the root of the project and add the following contents:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.hello"
    android:versionCode="1"
    android:versionName="1.0.0" >

</manifest>
```

This represents the simplest possible manifest, which is useful for illustrating the Maven integration, however it contains no declarations for any Android components typically found within applications.


## Building Android Code

Maven is now ready to build our project. There are several build lifecycle goals we can execute against it now, including goals to compile our code, create a library package (e.g., a JAR file), and install the library in the local Maven dependency repository.

To try out the build, issue the following at the command line:

```sh
$ mvn compile
```

This will run Maven, telling it to execute the _compile_ goal. When it's finished, you should find the compiled _.class_ files in the _target/classes_ directory.

Since it's unlikely that you'll want to distribute or work with _.class_ files directly, you'll probably want to run the _package_ goal instead:

```sh
$ mvn package
```

The *package* goal will compile your Java code, run any tests, and finish by packaging the code up in a JAR file within the *target* directory. The name of the JAR file will be based on the project's `<artifactId>` and `<version>`. For example, given the minimal *pom.xml* file from before, the JAR file will be named *gs-maven-android-start-1.0.0.jar*.

Because we changed the value of `<packaging>` from "jar" to "apk", the result will be an APK file within the *target* directory in addition to the JAR file. This APK file is now a packaged Android application ready to be deployed to a device or emulator.

The Android Maven Plugin provides several more Maven goals that can be used to initiate the various phases of the build process, or interact with the device and emulator. You can see a list of all the available goals by running the following command:

```sh
$ mvn android:help
```


## Conclusions

Congratulations! You have now created a very simple, yet effective Maven project definition for building Android projects. Even though the Android app we built has no functionality, we have demonstrated the capabilities of the [Android Maven Plugin] when developing Android applications.


## Next Steps

There's much more to building projects with Maven. For continued exploration of Maven, you may want to have a look at the following Getting Started guides:

* Setting Maven properties

And for an alternate approach to building projects, your may want to look at [Building Android Projects with Gradle].


[`complete`]:complete/
[`start`]:start/
[git]:http://git-scm.com
[GitHub for Mac]:http://mac.github.com
[GitHub for Windows]:http://windows.github.com
[Android SDK]: http://developer.android.com/sdk/index.html
[Android Developers]:http://developer.android.com/sdk/installing/index.html
[Platforms and Packages]:http://developer.android.com/sdk/installing/adding-packages.html
[Android Maven Plugin]:https://code.google.com/p/maven-android-plugin
[Android Manifest]:http://developer.android.com/guide/topics/manifest/manifest-intro.html
[Building Android Projects with Gradle]:https://github.com/springframework-meta/gs-gradle-android