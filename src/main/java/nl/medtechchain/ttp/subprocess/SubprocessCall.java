package nl.medtechchain.ttp.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility class for executing system commands as subprocesses.
 * <p>
 * This class provides a static method to execute system commands and retrieve their output.
 * It encapsulates the complexity of dealing with Java's {@code ProcessBuilder} API, stream
 * handling, and error management.
 */
public class SubprocessCall {
    private static final Logger logger = LoggerFactory.getLogger(SubprocessCall.class);

    /**
     * Executes a system command and returns its output as a string.
     *
     * @param command The system command to execute. This command is passed to {@code /bin/sh -c},
     *                allowing for complex commands and shell features.
     * @return The output produced by the command.
     * @throws IOException If an I/O error occurs or the subprocess exits with an error status code.
     */
    public static String execute(String command) throws IOException {

        ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", command);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null)
            output.append(line);

        try {
            process.waitFor();
        } catch (InterruptedException ignored) {
        }

        if (process.exitValue() != 0) {
            String msg = String.format("Error executing command '%s': Process exited with error status code %d: %s", command, process.exitValue(), output);
            logger.error(msg);
            throw new IOException(msg);
        }

        return output.toString();
    }
}
