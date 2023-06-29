package unsw.blackout;

public class File {
    private String fileName;
    private String content;
    private boolean isSending;

    public File(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        isSending = false;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

}
