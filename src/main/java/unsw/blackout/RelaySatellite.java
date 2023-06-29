package unsw.blackout;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
    private String rotation;
    private boolean withinBounds;

    public RelaySatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);

        // Sets the satellite-specific attributes
        setSpeed(1500);
        setRange(300000);
        withinBounds = false;
    }

    /**
     * Simulates the movement for one minute of time and includes functionality for
     * the relay satellite
     */
    @Override
    public void move() {
        double radians = this.getPosition().toRadians();
        double angularVelocity = getSpeed() / getHeight();
        double degrees = this.getPosition().toDegrees();

        // Check for [140, 190] positioning
        if (withinBounds) {
            if (rotation.equals("anticlockwise")) {
                radians += angularVelocity;
                this.setPosition(Angle.fromRadians(radians));
                double antiDegrees = Angle.fromRadians(radians).toDegrees();

                // Check for end of bounds
                if (antiDegrees > 190) {
                    rotation = "clockwise";
                }
            } else if (rotation.equals("clockwise")) {
                radians -= angularVelocity;
                this.setPosition(Angle.fromRadians(radians));
                double antiDegrees = Angle.fromRadians(radians).toDegrees();

                // Check for end of bounds
                if (antiDegrees < 140) {
                    rotation = "anticlockwise";
                }
            }
        } else {
            // Sets original rotation
            if (degrees > 140 && degrees < 345) {
                rotation = "clockwise";
                radians -= angularVelocity;
            } else {
                rotation = "anticlockwise";
                radians += angularVelocity;
            }
            setPosition(Angle.fromRadians(radians));
            double newDegrees = Angle.fromRadians(radians).toDegrees();

            // Checks if satellite is in bounds
            if (newDegrees <= 190 && newDegrees >= 140) {
                withinBounds = true;
            }
        }

    }
}
