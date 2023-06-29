package unsw.blackout;

import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite {
    private int maxBytes;
    private int receiveLimit;
    private int sendLimit;
    private String rotation;

    public TeleportingSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);

        // Sets the satellite-specific attributes
        setSpeed(1000);
        setRange(200000);
        maxBytes = 200;
        receiveLimit = 15;
        sendLimit = 10;
        rotation = "anticlockwise";
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    /**
     * Simulates the movement for one minute of time and includes functionality for
     * the teleporting satellite
     */
    @Override
    public void move() {
        // Calculates the angular velocity
        double radians = getPosition().toRadians();
        double originalPos = getPosition().toDegrees();
        double angularVelocity = getSpeed() / getHeight();

        // Check for current rotation
        if (rotation.equals("anticlockwise")) {
            radians += angularVelocity;
            setPosition(Angle.fromRadians(radians));
            double antiDegrees = Angle.fromRadians(radians).toDegrees();

            // Check for teleport location reached and case of first movement
            if (antiDegrees >= 180 && originalPos < 180) {
                setPosition(Angle.fromDegrees(360));
                setRotation("clockwise");
            }

            // For case of first movement, resets angle
            if (antiDegrees >= 360) {
                setPosition(Angle.fromDegrees(Angle.fromRadians(radians).toDegrees() - 360));
            }
        } else if (rotation.equals("clockwise")) {
            radians -= angularVelocity;
            setPosition(Angle.fromRadians(radians));
            double degrees = Angle.fromRadians(radians).toDegrees();

            // Check for teleport location reached
            if (degrees <= 180) {
                setPosition(Angle.fromDegrees(0));
                setRotation("anticlockwise");
            }
        }
    }
}
