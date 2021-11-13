package com.example.tmf;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

@Component
@StepScope
public class Tasklet2 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contoribution, ChunkContext cuhnkcontext) throws Exception{
        System.out.println("tasklet2!!");
        return RepeatStatus.FINISHED;
    }
}
