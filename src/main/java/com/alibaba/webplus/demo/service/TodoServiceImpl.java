package com.alibaba.webplus.demo.service;

import java.util.List;

import com.alibaba.webplus.demo.domain.Todo;
import com.alibaba.webplus.demo.domain.TodoDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 白寂
 * @date 2019/07/23
 */

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoDao dao;

    @Override
    public List<Todo> get(Boolean completed) {
        return dao.getByStatus(completed);
    }

    @Override
    public Todo get(long id) {
        return dao.getById(id);
    }

    @Override
    public List<Todo> get() {
        return dao.getAll();
    }


    @Override
    public Todo create(Todo todo) {
        return dao.create(todo);
    }

    @Override
    public List<Todo> deleteCompleted() {
        dao.deleteCompleted();
        return dao.getAll();
    }

    @Override
    public List<Todo> delete(long id) {
        dao.deleteById(id);
        return dao.getAll();
    }

    @Override
    public Todo update(long id, Todo todo) {
        return dao.update(id, todo);
    }
}
