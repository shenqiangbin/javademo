import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CmdUtil.class);

    public static String execCmdSync(String cmd, CmdExecResult callback) throws IOException, InterruptedException {
        LOG.debug("Running command:", cmd);

        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmd);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        StringBuffer stdout = new StringBuffer();
        StringBuffer errout = new StringBuffer();

        // read the output from the command
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            stdout.append(s);
        }

        // read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            errout.append(s);
        }

        if (callback == null) {
            return stdInput.toString();
        }

        int exitVal = proc.waitFor();
        callback.onComplete(exitVal == 0, exitVal, errout.toString(), stdout.toString(), cmd);

        return stdInput.toString();
    }

    public interface CmdExecResult{
        void onComplete(boolean success, int exitVal, String error, String output, String originalCmd) throws IOException;
    }
}
