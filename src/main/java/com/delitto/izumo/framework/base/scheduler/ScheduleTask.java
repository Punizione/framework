package com.delitto.izumo.framework.base.scheduler;

import lombok.Data;

@Data
public abstract class ScheduleTask implements Runnable {
    private String taskId;
    public ScheduleTask(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public abstract void run();
}
