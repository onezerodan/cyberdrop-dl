# cyberdrop-dl

Java command-line application to download albums from [cyberdrop.me](https://cyberdrop.me)  
##### cyberdrop-dl  uses multi-threaded download, so photos will be downloaded incredibly fast!

# Requirements
- JDK 17 (or higher)  
- Maven  
- (optional) [GraalVM](https://www.graalvm.org)  

# Building
### Jar file
```bash
mvn clean install
```
Jar file will be stored in `target` directory.  
You should use `cyberdrop-dl-[version]-jar-with-dependencies.jar`

###[GraalVM Native Image](https://www.graalvm.org/22.0/reference-manual/native-image/)
**This solution will create executable file.**  
Note, that GraalVM supports only **java11** or **java17**.

```bash
native-image -jar /path/to/cyberdrop-dl-[version]-jar-with-dependencies.jar <name> --allow-incomplete-classpath --enable-url-protocols=http,https
```
File will be stored in the same directory and specified at the end of `native-image` output.

### Prebuilt binaries
You can just use prebuilt binaries, which are stored in [prebuilt]() folder.


# Usage

You can specify **two** arguments:

- (Required) **URL** to cyberdrop.me album.
- (Optional) **Directory** in which the album will be downloaded.

If **Directory** will not be specified, it will be created automatically.

#### Jar file
```bash
java -jar /path/to/cyberdrop-dl-[version]-jar-with-dependencies.jar <url> <directory>
```

#### GraalVM Native Nmage
```bash
./path/to/geretated/image <url> <directory>
```




