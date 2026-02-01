package vtc.xueqing.flower.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class FileUploadUtils {

    @Value("${file.upload.path:./uploads/images/}")
    private String uploadPath;

    private Path rootLocation;

    @PostConstruct
    private void init() throws IOException {
        rootLocation = Paths.get(uploadPath);
        Files.createDirectories(rootLocation);
    }

    /**
     * Upload file and return the saved file path
     */
    public String uploadFile(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        validateFileType(file);

        // Create subdirectory if needed
        Path uploadDir = rootLocation;
        if (subDir != null && !subDir.trim().isEmpty()) {
            uploadDir = rootLocation.resolve(subDir);
            Files.createDirectories(uploadDir);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = generateUniqueFilename(extension);
        Path destinationFile = uploadDir.resolve(uniqueFilename);

        // Save file
        file.transferTo(destinationFile.toAbsolutePath());

        // Return relative path with /images/ prefix for web access
        String relativePath = destinationFile.toString().substring(rootLocation.toString().length());
        if (relativePath.startsWith(File.separator)) {
            relativePath = relativePath.substring(1);
        }
        return "/images/" + relativePath.replace(File.separator, "/");
    }

    /**
     * Validate file type (only allow image files)
     */
    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("Invalid file type");
        }

        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Additional check based on file extension
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String lowerCaseFileName = fileName.toLowerCase();
            if (!(lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg") ||
                  lowerCaseFileName.endsWith(".png") || lowerCaseFileName.endsWith(".gif"))) {
                throw new IllegalArgumentException("Only JPG, JPEG, PNG, and GIF files are allowed");
            }
        }
    }

    /**
     * Generate unique filename using timestamp and UUID
     */
    private String generateUniqueFilename(String extension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return dateStr + "_" + uuid + extension;
    }
}