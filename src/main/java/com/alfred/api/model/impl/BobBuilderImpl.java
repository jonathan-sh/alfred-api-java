package com.alfred.api.model.impl;

import com.alfred.api.model.Build;
import com.alfred.api.model.face.BobBuilder;
import com.alfred.api.util.constants.BuildStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;

@Component
public class BobBuilderImpl implements BobBuilder {
    private static Logger log = LoggerFactory.getLogger(BobBuilderImpl.class);
    private Build buildInProcess;

    @Override
    public void build(Build buildInProcess) {
        this.buildInProcess = buildInProcess;
        try
        {
            startBuild();
            validBuild();

        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    private void startBuild() throws IOException {
        this.executeCommand("/solinftec/bin/make-build.sh " + this.buildInProcess.application + " " + this.buildInProcess.branch);
    }

    private void validBuild() {
        if (success)
        {
            this.buildInProcess.updateStatus(BuildStatus.SUCESS);
        }
        if (fail)
        {
            this.buildInProcess.updateStatus(BuildStatus.FAIL);
        }
    }

    private boolean finish = false;
    private boolean success = false;
    private boolean fail = false;

    private void executeCommand(final String command) throws IOException {

        final ArrayList<String> commands = new ArrayList<String>();
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(command);

        BufferedReader br = null;

        try
        {
            final ProcessBuilder p = new ProcessBuilder(commands);
            final Process process = p.start();
            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null)
            {
                log.info(line);

                if (!finish)
                {

                    if (line.contains("[INFO] BUILD SUCCESS"))
                    {
                        finish = true;
                        success = true;
                    }
                    if (line.contains("[INFO] BUILD FAILURE"))
                    {
                        finish = true;
                        fail = true;
                    }
                }
            }


        }
        catch (IOException ioe)
        {
            log.error("Erro ao executar comando shell" + ioe.getMessage());
            throw ioe;
        }
        finally
        {
            secureClose(br);
        }
    }

    private void secureClose(final Closeable resource) {
        try
        {
            if (resource != null)
            {
                resource.close();
            }
        }
        catch (IOException ex)
        {
            log.error("Erro = " + ex.getMessage());
        }
    }

}
