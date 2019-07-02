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
            main.runUploader(args[0]);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void runUploader(String videoPath) throws Exception{
        String developerKey = youtubeUploader.getDeveloperKeyFromFile();
        youtubeUploader.gatherUserInputForVideo();
        youtubeUploader.uploadVideo(videoPath, developerKey);
    }




}
