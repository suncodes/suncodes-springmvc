package pojo;

import org.springframework.web.multipart.MultipartFile;

public class UploadBO {
    private MultipartFile file;
    private String param;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
