package roommateproject.roommatebackend.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class RepresentImageStore {

    @Value("${spring.image.represent}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    public UserImage storeFile(User user, MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String storeFileName = createStoreFileName(originalFileName, uuid);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UserImage(user, true, originalFileName, storeFileName, false);
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
