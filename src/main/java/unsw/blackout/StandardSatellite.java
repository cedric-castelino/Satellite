package unsw.blackout;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
    private int maxFiles;
    private int maxBytes;
    private int receiveLimit;
    private int sendLimit;

    public StandardSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);

        // Sets the satellite-specific attributes
        setSpeed(2500);
        setRange(150000);
        maxFiles = 3;
        maxBytes = 80;
        receiveLimit = 1;
        sendLimit = 1;
    }

}
