# Scheduled Tasks Manager

---
A Java POC to manage scheduled Repeatable and Interval Tasks.
The package uses a priority queue to hold the jobs ordered by the next executing time and a threads to execute them in the right time.

### Package Features:
- Add a new task in runtime
- Remove a task in runtime
- Support for Repeatable and Interval tasks
- Specify Interval time in seconds
- Specify Repeatable time in seconds
- control the number of reputations for a Repeatable task
### Future Features:

- Use a database to store the tasks
- Use a database to store the tasks history
- Use a database to store the tasks logs
- Use a separate queuing system to manage the tasks
- Control the number of threads to execute the tasks
- Control task execution overlap

