package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.utils.Angle;

public abstract class Satellite {
    private String satelliteId;
    private String type;
    private double height;
    private Angle position;
    private List<File> fileList = new ArrayList<>();
    private int speed;
    private int range;

    public Satellite(String satelliteId, String type, double height, Angle position) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public String getType() {
        return type;
    }

    public double getHeight() {
        return height;
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Simulates the movement for one minute of time
     */
    public void move() {
        // Calculates the angular velocity
        double radians = this.getPosition().toRadians();
        double angularVelocity = getSpeed() / getHeight();

        // Checks if movement goes below 0 degrees
        if ((radians - angularVelocity) < 0) {
            angularVelocity -= radians;
            // Resets to 360
            radians = Angle.fromDegrees(360).toRadians() - angularVelocity;
        } else {
            radians -= angularVelocity;
        }

        // Moves the satellite to the new position
        setPosition(Angle.fromRadians(radians));

    }

}
