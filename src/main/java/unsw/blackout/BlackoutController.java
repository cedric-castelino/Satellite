package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

public class BlackoutController {
    private List<Device> deviceList = new ArrayList<>();
    private List<Satellite> satelliteList = new ArrayList<>();

    /**
     * Creates a new device and stores it
     *
     * @param deviceId
     * @param type
     * @param position
     */
    public void createDevice(String deviceId, String type, Angle position) {
        Device device = new Device(deviceId, type, position);
        deviceList.add(device);
    }

    /**
     * Removes a selected device
     *
     * @param deviceId
     */
    public void removeDevice(String deviceId) {
        for (Device device : deviceList) {
            if (device.getDeviceId().equals(deviceId)) {
                deviceList.remove(device);
                break;
            }
        }
    }

    /**
     * Creates a new satellite and stores it
     *
     * @param satelliteId
     * @param type
     * @param height
     * @param position
     */
    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite satellite;

        // Check type to initialise satellite-specific attributes
        if (type.equals("StandardSatellite")) {
            satellite = new StandardSatellite(satelliteId, type, height, position);
        } else if (type.equals("TeleportingSatellite")) {
            satellite = new TeleportingSatellite(satelliteId, type, height, position);
        } else {
            satellite = new RelaySatellite(satelliteId, type, height, position);
        }
        satelliteList.add(satellite);
    }

    /**
     * Removes a selected satellite
     *
     * @param satelliteId
     */
    public void removeSatellite(String satelliteId) {
        for (Satellite satellite : satelliteList) {
            if (satellite.getSatelliteId().equals(satelliteId)) {
                satelliteList.remove(satellite);
                break;
            }
        }
    }

    /**
     * @return a list of all stored device ids
     */
    public List<String> listDeviceIds() {
        List<String> deviceIds = new ArrayList<>();
        for (Device device : deviceList) {
            deviceIds.add(device.getDeviceId());
        }
        return deviceIds;
    }

    /**
     * @return a list of all stored satellite ids
     */
    public List<String> listSatelliteIds() {
        List<String> satelliteIds = new ArrayList<>();
        for (Satellite satellite : satelliteList) {
            satelliteIds.add(satellite.getSatelliteId());
        }
        return satelliteIds;
    }

    /**
     * adds a file and its content to a selected device
     *
     * @param deviceId
     * @param filename
     * @param content
     */
    public void addFileToDevice(String deviceId, String filename, String content) {
        File newFile = new File(filename, content);
        for (Device device : deviceList) {
            if (device.getDeviceId().equals(deviceId)) {
                device.addFile(newFile);
            }
        }
    }

    /**
     * Gets the information of an entity matching the inputted id
     *
     * @param id
     * @return a EntityInfoResponse containing the info requested
     */
    public EntityInfoResponse getInfo(String id) {
        // Loop through all devices
        for (Device device : deviceList) {
            if (device.getDeviceId().equals(id)) {
                // If found, create a file map
                List<File> fileList = device.getFileList();
                Map<String, FileInfoResponse> files = createFilesInfo(fileList);
                return new EntityInfoResponse(id, device.getPosition(), RADIUS_OF_JUPITER, device.getType(), files);
            }
        }

        // Loop through all satellites
        for (Satellite satellite : satelliteList) {
            if (satellite.getSatelliteId().equals(id)) {
                // If found, create a file map
                List<File> fileList = satellite.getFileList();
                Map<String, FileInfoResponse> files = createFilesInfo(fileList);
                return new EntityInfoResponse(id, satellite.getPosition(), satellite.getHeight(), satellite.getType(),
                        files);
            }
        }

        return null;
    }

    /**
     * Creates a list of existing files in a map format
     *
     * @param fileList
     * @return a list containing the file name and info of each file
     */
    public Map<String, FileInfoResponse> createFilesInfo(List<File> fileList) {
        Map<String, FileInfoResponse> files = new HashMap<>();

        // Loop through all files
        for (File file : fileList) {
            FileInfoResponse fileInfo = new FileInfoResponse(file.getFileName(), file.getContent(),
                    file.getContent().length(), true);
            files.put(file.getFileName(), fileInfo);
        }
        return files;
    }

    /**
     * Moves the satellite, simulating 1 minute of time
     */
    public void simulate() {
        for (Satellite satellite : satelliteList) {
            satellite.move();
        }
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    /**
     * Seaches for all devices and sattelites in range and visible
     *
     * @param id
     * @return a list containing the ids of all communicable entiities in range
     */
    public List<String> communicableEntitiesInRange(String id) {
        boolean isSatellite = listSatelliteIds().contains(id);
        // Check if id is a satellite or device
        if (isSatellite) {
            Satellite chosenSatellite = satelliteList.get(0);
            for (Satellite satellite : satelliteList) {
                if (satellite.getSatelliteId().equals(id)) {
                    chosenSatellite = satellite;
                }
            }

            return communicableEntitiesToSatellite(chosenSatellite);
        } else {
            Device chosenDevice = deviceList.get(0);
            for (Device device : deviceList) {
                if (device.getDeviceId().equals(id)) {
                    chosenDevice = device;
                }
            }

            return communicableEntitiesToDevice(chosenDevice);
        }
    }

    /**
     * Checks each satellite to see if it is visible and in range
     *
     * @param satellite
     * @return a list of ids for all communicable satellites in range
     */
    public List<String> communicableEntitiesToSatellite(Satellite satellite) {
        List<String> result = new ArrayList<>();
        // Loop through all satellites
        for (Satellite currSatellite : satelliteList) {
            // Check for same satellite
            if (satellite.getSatelliteId().equals(currSatellite.getSatelliteId())) {
                continue;
            }

            // Check if satellite is visible and in range
            if (MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), currSatellite.getHeight(),
                    currSatellite.getPosition())
                    && MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(),
                            currSatellite.getHeight(), currSatellite.getPosition()) <= satellite.getRange()) {
                result.add(currSatellite.getSatelliteId());
                continue;
            }

            // Check if satellite can be reached through relays
            if (checkRelaySatellite(satellite, currSatellite)) {
                result.add(currSatellite.getSatelliteId());
            }
        }

        // Loop through all devices
        for (Device currDevice : deviceList) {
            // Check for desktop and standard satellite case
            if (satellite.getType().equals("StandardSatellite") && currDevice.getType().equals("DesktopDevice")) {
                continue;
            }

            // Check if device is visible and in range
            if (MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), currDevice.getPosition())
                    && MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(),
                            currDevice.getPosition()) <= satellite.getRange()) {
                result.add(currDevice.getDeviceId());
                continue;
            }

            // Check if device can be reached through relays
            if (checkRelayDevice(satellite, currDevice, false)) {
                result.add(currDevice.getDeviceId());
            }
        }
        return result;
    }

    /**
     * Checks each device to see if it is visible and in range
     *
     * @param device
     * @return a list of ids for all communicable device in range
     */
    public List<String> communicableEntitiesToDevice(Device device) {
        List<String> result = new ArrayList<>();
        // Loop through all satellites
        for (Satellite satellite : satelliteList) {
            // Check for desktop and standard satellite case
            if (satellite.getType().equals("StandardSatellite") && device.getType().equals("DesktopDevice")) {
                continue;
            }

            // Check if satellite is visible and in range
            if (MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), device.getPosition())
                    && MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(),
                            device.getPosition()) <= device.getRange()) {
                result.add(satellite.getSatelliteId());
                continue;
            }

            // Check if device can be reached through relays
            if (checkRelayDevice(satellite, device, true)) {
                result.add(satellite.getSatelliteId());
            }
        }
        return result;
    }

    /**
     * Checks if a satellite can be connected to another through relays
     *
     * @param original
     * @param currSatellite
     * @return true is reachale through a relay connection, false otherwise
     */
    public boolean checkRelaySatellite(Satellite original, Satellite currSatellite) {
        // Checks if current original and satellite are communicable
        if (MathsHelper.isVisible(original.getHeight(), original.getPosition(), currSatellite.getHeight(),
                currSatellite.getPosition())
                && MathsHelper.getDistance(original.getHeight(), original.getPosition(), currSatellite.getHeight(),
                        currSatellite.getPosition()) <= original.getRange()) {
            return true;
        }

        // Loop through all satellites
        for (Satellite relay : satelliteList) {
            // Checks for relays and duplicates
            if (relay.getType().equals("RelaySatellite") && !relay.equals(original) && !relay.equals(currSatellite)) {
                // Checks validity of new satellite
                if (MathsHelper.isVisible(original.getHeight(), original.getPosition(), relay.getHeight(),
                        relay.getPosition())
                        && MathsHelper.getDistance(original.getHeight(), original.getPosition(), relay.getHeight(),
                                relay.getPosition()) <= original.getRange()) {
                    // Recursively sets new relay as original to find a path to current
                    if (checkRelaySatellite(relay, currSatellite)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if a satellite can be connected to a device through relays
     *
     * @param original
     * @param device
     * @param isDevice
     * @return true is reachale through a relay connection, false otherwise
     */
    public boolean checkRelayDevice(Satellite original, Device device, boolean isDevice) {
        int range;
        // Sets the range based on case of device
        if (isDevice) {
            range = device.getRange();
        } else {
            range = original.getRange();
        }

        // Checks if current original and satellite are communicable
        if (MathsHelper.isVisible(original.getHeight(), original.getPosition(), device.getPosition()) && MathsHelper
                .getDistance(original.getHeight(), original.getPosition(), device.getPosition()) <= range) {
            return true;
        }

        // Loop through all satellites
        for (Satellite relay : satelliteList) {
            // Checks for relays and duplicates
            if (relay.getType().equals("RelaySatellite") && !relay.equals(original)) {
                if (MathsHelper.isVisible(original.getHeight(), original.getPosition(), relay.getHeight(),
                        relay.getPosition())
                        && MathsHelper.getDistance(original.getHeight(), original.getPosition(),
                                device.getPosition()) <= range) {
                    // Recursively sets new relay as original to find a path to current
                    if (checkRelayDevice(relay, device, false)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {

        if (isRelay(toId)) {
            throw new FileTransferException.VirtualFileNotFoundException("Cannot send a file to a relay sattelite.");
        }
    }

    public boolean isRelay(String id) {
        if (!listSatelliteIds().contains(id)) {
            return false;
        }

        for (Satellite satellite : satelliteList) {
            if (satellite.getSatelliteId().equals(id)) {
                if (satellite.getType().equals("RelaySatellite")) {
                    return true;
                }
            }
        }

        return false;
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }
}
