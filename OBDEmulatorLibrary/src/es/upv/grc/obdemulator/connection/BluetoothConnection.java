package es.upv.grc.obdemulator.connection;

import es.upv.grc.obdemulator.elm.ECUVehicle;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class BluetoothConnection implements Runnable {

    private final ECUVehicle data;
    protected BluetoothConnectionHandler handler;
    protected String name;

    protected boolean running = true;

    public BluetoothConnection(ECUVehicle data, BluetoothConnectionHandler handler, String name) {
        this.data = data;
        this.handler = handler;
        this.name = name;
    }

    @Override
    public synchronized void run() {
        running = true;
        while (running) {
            waitForConnection();
        }
    }

    public void terminate() {
        running = false;
    }

    protected abstract void waitForConnection();

    public void processRequest(InputStream inputStream,
            OutputStream outputStream) throws Exception {
        handler.updateLog(name + " waiting for input");
        while (running) {//TODO: Verificar funcionamiento
            try {
                String cmd = "";
                int c;
                boolean cont = true;
                while (cont && running) {
                    c = inputStream.read();
                    if (c == -1) {
                        throw new Exception(name + " disconnected!!");
                    } else if (c != '\r' && c != 10) {
                        cmd += (char) c;
                    } else {
                        cont = false;
                    }
                }
                handler.updateRequest(cmd);
                if (!cmd.equals("")) {
                    processCommand(cmd, outputStream);
                }
            } catch (Exception e) {
                handler.updateLog("Bluetooth Connection Error : " + e.getMessage());
                throw e;
            }
        }
    }

    void processCommand(String cmd, OutputStream outputStream) throws Exception {
        String response = " ";
        try {
            if (cmd.startsWith("0902")) {
                //response = "49020131464146\r\n49020250333633\r\n49020338595732\r\n49020439313131\r\n49020533000000";
                response = "?\r\n>";
            } else {
                response = data.getCode(cmd);
            }
        } catch (Exception e) {
            handler.updateLog("Code not found:-" + cmd + "-");
        }
        handler.updateResponse(response);
        outputStream.write((response).getBytes());
    }

}
