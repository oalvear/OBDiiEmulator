package es.upv.grc.bluetooth;

import es.upv.grc.obdemulator.connection.BluetoothConnection;
import es.upv.grc.obdemulator.connection.BluetoothConnectionHandler;
import es.upv.grc.obdemulator.elm.ECUVehicle;
import java.io.IOException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class GPSBTEmulator extends BluetoothConnection {
    String stringUuid = "0000110100001000800000805F9BFFFF";

    public GPSBTEmulator(ECUVehicle data, BluetoothConnectionHandler handler) {
        super(data, handler, "GPSEmulator");
    }

    @Override
    protected void waitForConnection() {
        LocalDevice local;
        StreamConnectionNotifier notifier;
        StreamConnection connection;
        // setup the server to listen for connection
        try {
            local = LocalDevice.getLocalDevice();
            //local.setDiscoverable(DiscoveryAgent.GIAC);
            UUID uuid = new UUID(stringUuid, false);
            String url = "btspp://localhost:" + uuid.toString() + ";name=" + name;
            notifier = (StreamConnectionNotifier) Connector.open(url);
        } catch (IOException e) {
            return;
        }
        // waiting for connection
        while (running) {
            try {
                handler.updateLog(name + " waiting for connections...");
                connection = notifier.acceptAndOpen();
                processRequest(connection.openInputStream(), connection.openOutputStream());
            } catch (Exception e) {
                handler.updateLog(name + " disconnected");
            }
        }
    }

}
