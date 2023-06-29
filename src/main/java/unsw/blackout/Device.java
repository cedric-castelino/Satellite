package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import unsw.utils.Angle;

public class Device {
    private String deviceId;
    private String type;
    private Angle position;
    private List<File> fileList = new ArrayList<>();
    private int range;

    public Device(String deviceId, String type, Angle position) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;

        // Set the different ranges for each device type
        if (type.equals("HandheldDevice")) {
            range = 50000;
        } else if (type.equals("LaptopDevice")) {
            range = 100000;
        } else if (type.equals("DesktopDevice")) {
            range = 200000;
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getType() {
        return type;
    }

    public Angle getPosition() {
        return position;
    }

    public void addFile(File newFile) {
        fileList.add(newFile);
    }

    public int getRange() {
        return range;
    }

    public List<File> getFileList() {
        return fileList;
    }

}
