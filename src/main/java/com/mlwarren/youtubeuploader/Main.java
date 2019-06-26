package com.mlwarren.youtubeuploader;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.*;
import java.security.GeneralSecurityException;

@SpringBootApplication
public class Main {

    @Autowired
    YoutubeUploader youtubeUploader;

    @Autowired
    UserInputManager userInputManager;

    @Value("${developerKeyPath}")
    public String DEVELOPER_KEY_PATH;

    public static void main(String[] args){
        if(args.length!=1){
            System.out.println("Specify video file path");
            System.exit(1);
        }
        System.out.println("Attempting to upload video with path:" + args[0]);
        ApplicationContext appContext = SpringApplication.run(Main.class, args);
        Main main = appContext.getBean(Main.class);
        try{
            String developerKey = main.getDeveloperKeyFromFile();
            main.gatherUserInputForVideo();
            main.uploadVideo(args[0], developerKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public String getDeveloperKeyFromFile(){
        String developerKey="";
        try {
            BufferedReader developerKeyFileReader = new BufferedReader(new FileReader(new File(DEVELOPER_KEY_PATH)));
            developerKey = developerKeyFileReader.readLine();
        }
        catch(Exception e){
            System.err.println("Please check that developer key file is in config directory.");
            e.printStackTrace();
            System.exit(1);
        }

        return developerKey;
    }

    public void gatherUserInputForVideo(){
        if(!userInputManager.getUserInputFromCommandLine()){
            System.err.println("Please rerun program with correct input");
            System.exit(2);
        }
    }

    public void uploadVideo(String videoPath, String developerKey) throws IOException, GeneralSecurityException {

        YouTube youtubeService = youtubeUploader.getService();

        // Define the Video object, which will be uploaded as the request body.
        Video video = new Video();

        // Add the snippet object property to the Video object.
        VideoSnippet snippet = new VideoSnippet();
        snippet.setCategoryId(userInputManager.getCategoryID());
        snippet.setDescription(userInputManager.getDescription());
        snippet.setTitle(userInputManager.getTitle());
        video.setSnippet(snippet);

        // Add the status object property to the Video object.
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus(userInputManager.getPrivacyStatus());
        video.setStatus(status);

        File mediaFile = new File(videoPath);
        InputStreamContent mediaContent =
                new InputStreamContent("video/*",
                        new BufferedInputStream(new FileInputStream(mediaFile)));
        mediaContent.setLength(mediaFile.length());

        // Define and execute the API request
        YouTube.Videos.Insert request = youtubeService.videos()
                .insert("snippet,status", video, mediaContent).setKey(developerKey);
        Video response = request.execute();
        System.out.println(response);
    }
}
