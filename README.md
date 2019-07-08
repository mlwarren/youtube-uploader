# Youtube-Uploader

Command line tool to allow uploading of video files from Linux or Windows command line to Youtube.

### Prerequisites

OpenJDK 6+

### Installing

In project root directory run Maven to build project .zip and .tar packages:

```
mvn clean package
```

The resulting build will output files:

```
youtube-uploader-v1-package.zip
youtube-uploader-v1-package.tar.gz
```

Unzipping the packages will yield a .jar file and a config directory. The config directory has placeholder files for:
```
client_secret.json
developer.key
```

Before running the .jar, these files must be replaced with the appropriate configuration files from Amazon. You can learn more about the client_secret.json and obtaining a developer key [here](https://developers.google.com/youtube/registering_an_application).

You may run the executable jar file with the following syntax:
```
java -jar youtube-uploader-v1.jar [video path]
```


