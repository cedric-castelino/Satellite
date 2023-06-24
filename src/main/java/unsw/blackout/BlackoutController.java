package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

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
        Satellite satellite = new Satellite(satelliteId, type, height, position);
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

    public void simulate() {
        // TODO: Task 2a)
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

    public List<String> communicableEntitiesInRange(String id) {
        // TODO: Task 2 b)
        return new ArrayList<>();
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO: Task 2 c)
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
