package com.softserve.ita.java442.cityDonut.scheduling;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Scope("singleton")
@Component
public class ScheduledTasksPool {

    private final ConcurrentHashMap<TaskPoolKey, ScheduledTaskContainer> emailTaskPool;

    {
        emailTaskPool = new ConcurrentHashMap<>();
    }

    public void clearPool() {
        for (TaskPoolKey key : emailTaskPool.keySet()) {
            ScheduledTaskContainer container = emailTaskPool.get(key);
            if (container != null) {
                if (container.getScheduledFuture() == null
                        || container.getScheduledFuture().isCancelled()
                        || container.getScheduledFuture().isDone()) {
                    emailTaskPool.remove(key);
                }
            }
        }
    }

    public ScheduledTaskContainer getScheduledTask(long userId, long projectId) {
        return emailTaskPool.get(TaskPoolKey.createInstance(userId, projectId));
    }

    public void removeTask(long userId, long projectId) {
        emailTaskPool.remove(TaskPoolKey.createInstance(userId, projectId));
    }

    public void createScheduledTask(
            long userId, long projectId, ScheduledFuture<?> scheduledFuture, String text, List<Long> userList) {
        ScheduledTaskContainer scheduledTaskContainer = new ScheduledTaskContainer(text, userList, scheduledFuture);
        emailTaskPool.put(TaskPoolKey.createInstance(userId, projectId), scheduledTaskContainer);
    }

}
