package com.codesoom.assignment.services;

import com.codesoom.assignment.interfaces.CRUDInterface;
import com.codesoom.assignment.interfaces.IdGenerator;
import com.codesoom.assignment.mappers.TaskMapper;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService implements CRUDInterface<TaskDTO , Task , Long> {
    private final Map<Long , Task> tasks = new ConcurrentHashMap<>();
    private final TaskMapper mapper;
    private final IdGenerator<Long> gen;

    public TaskService(TaskMapper mapper , IdGenerator<Long> gen){
        this.mapper = mapper;
        this.gen = gen;
    }

    @Override
    public Collection<Task> selectAll(){
        return tasks.values();
    }

    @Override
    public Task selectById(Long id) {
        return tasks.get(id);
    }

    @Override
    public Task insert(TaskDTO taskDTO){
        Long nextId = gen.generate();
        Task newTask = mapper.toNewTask(nextId , taskDTO);
        tasks.put(nextId , newTask);
        return newTask;
    }

    @Override
    public Task update(Long id, TaskDTO taskDTO) {
        if(tasks.get(id) == null){
            return null;
        }
        Task newTask = mapper.toNewTask(id , taskDTO);
        tasks.replace(id , newTask);
        return newTask;
    }

    @Override
    public Task delete(Long id) {
        return tasks.remove(id);
    }
}
