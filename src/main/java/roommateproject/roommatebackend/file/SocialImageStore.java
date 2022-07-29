package roommateproject.roommatebackend.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class SocialImageStore {

    @Value("${spring.image.represent}")
    private String fileDir;

    public String getFullPath(String filename){
        String[] fileSplit = filename.split(".Kakao");
        return fileDir + fileSplit[0] + ".jpg";
    }

    public UserImage storeFile(User user, String downloadURL) throws IOException {
        String originalFileName = "Kakao Download Profile";
        String uuid = UUID.randomUUID().toString();
        String storeFileName = createStoreFileName(originalFileName, uuid);
        String OUTPUT_FILE_PATH = getFullPath(storeFileName);

        try(InputStream in = new URL(downloadURL).openStream()){
            Path imagePath = Paths.get(OUTPUT_FILE_PATH);
            Files.copy(in, imagePath);
        }


        return new UserImage(user, true, originalFileName, storeFileName);
    }


    private String createStoreFileName(String originalFileName, String uuid) {
        String ext = extractExt(originalFileName);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

}
