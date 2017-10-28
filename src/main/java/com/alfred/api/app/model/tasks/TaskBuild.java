package com.alfred.api.app.model.tasks;

import com.alfred.api.app.model.interfaces.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskBuild {


    private static Logger logger = LoggerFactory.getLogger(TaskBuild.class);
    private Processor processor;

    @Autowired
    public TaskBuild(Processor processor) {
        this.processor = processor;
    }

    /**
     * Variával que controla o intervalo de tempo, aonde 60000 equivale a um minuto.
     */
    private final String INTERVALO_DE_TEMPO = "60000";

    /**
     * Rotina de verificação para fechamneto de jornada;
     */
    @Scheduled(fixedRateString = INTERVALO_DE_TEMPO)
    public void task() {
        logger.info("Inicio da tarefa");
        this.processor.process();
        logger.info("Fim da tarefa");
    }


}


