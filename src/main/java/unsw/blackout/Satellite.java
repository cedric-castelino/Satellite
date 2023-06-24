package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.utils.Angle;

public class Satellite {
    private String satelliteId;
    private String type;
    private double height;
    private Angle position;
    private List<File> fileList = new ArrayList<>();

    public Satellite(String satelliteId, String type, double height, Angle position) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Angle getPosition() {
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public void addFile(File newFile) {
        fileList.add(newFile);
    }

    public List<File> getFileList() {
        return fileList;
    }

}
