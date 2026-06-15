package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Ensures that only one instance of the application is running at a time.
 * Uses a ServerSocket bound to localhost on a specific port.
 */
public class SingleInstanceGuard {

    private SingleInstanceGuard() {}

    private static final Logger logger = LoggerFactory.getLogger(SingleInstanceGuard.class);
    private static final int PORT = 49152; // A port in the dynamic/private range

    @SuppressWarnings("unused")
    private static ServerSocket serverSocket;

    /**
     * Checks if another instance is already running by attempting to bind a socket.
     * If successful, keeps the socket open to prevent other instances from starting.
     *
     * @return true if this is the first instance and was successfully locked; false if already running.
     */
    public static boolean checkAndLock() {
        try {
            // Bind to localhost only so we don't expose any network ports to the outside
            serverSocket = new ServerSocket(PORT, 10, InetAddress.getByName("127.0.0.1"));
            logger.info("Instancia única verificada y bloqueada en el puerto {}.", PORT);
            return true;
        } catch (IOException e) {
            logger.warn("No se pudo iniciar porque otra instancia ya está en ejecución (puerto {} ocupado).", PORT);
            return false;
        }
    }
}
