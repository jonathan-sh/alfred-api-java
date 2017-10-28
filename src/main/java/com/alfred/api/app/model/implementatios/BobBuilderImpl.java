package com.alfred.api.app.model.implementatios;

import com.alfred.api.app.dto.Bob;
import com.alfred.api.app.model.Build;
import com.alfred.api.app.model.interfaces.BobBuilder;
import com.alfred.api.util.mongo.MongoHelper;
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
            throwBuild();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }


    private static final String SSH = "sudo ssh -i /home/ubuntu/.ssh/key.pem ubuntu@";
    private static final String SOLINFUP = "~/scripts-infra/solinfup/make-updade.sh";
    private void throwBuild() throws IOException {


        StringBuilder command = new StringBuilder();
        command
                .append(SSH)
                .append(this.buildInProcess.machine.ip)
                .append(" '")
                .append(this.SOLINFUP)
                .append(" ")
                .append(this.buildInProcess.application.name)
                .append(" ")
                .append(this.buildInProcess.branch)
                .append(" ")
                .append(this.buildInProcess.application.type)
                .append(" ")
                .append(MongoHelper.treatsId(this.buildInProcess._id))
                .append("' ");

        new Bob(buildInProcess.start,command.toString()).save();

        this.executeCommand(command.toString());
    }

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
