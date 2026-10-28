
package main.java.upload;

import java.io.IOException;

public class UploadedFile {

    private String originalName;
    private String contentType;
    private byte[] bytes;
    private String uploadDirectory;

    public UploadedFile(
            String originalName,
            String contentType,
            byte[] bytes,
            String uploadDirectory) {

        this.originalName = originalName;
        this.contentType = contentType;
        this.bytes = bytes;
        this.uploadDirectory = uploadDirectory;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public long getSize() {
        return bytes.length;
    }

    public boolean isEmpty() {
        return bytes.length == 0;
    }

    public String getUploadDirectory() {
        return uploadDirectory;
    }

    public String save() throws IOException {
        return FileStorage.save(this).toString();
    }
}
