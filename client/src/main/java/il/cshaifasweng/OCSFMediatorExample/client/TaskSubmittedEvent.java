package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;

// this is solely for when submitting a task
public class TaskSubmittedEvent {
    // Add the new task as an attribute
    private Task newTask;
    // Constructor to initialize the event with the new task
    public TaskSubmittedEvent(Task newTask) {
        this.newTask = newTask;
    }

    // Getter method to retrieve the new task
    public Task getNewTask() {
        return newTask;
    }

}

