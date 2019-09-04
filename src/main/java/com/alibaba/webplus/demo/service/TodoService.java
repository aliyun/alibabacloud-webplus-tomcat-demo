package com.alibaba.webplus.demo.service;

import java.util.List;

import com.alibaba.webplus.demo.domain.Todo;

/**
 * @author 白寂
 * @date 2019/07/23
 */
public interface TodoService {

    /**
     * 查询已完成或未完成的所有Todo
     * @param completed
     * @return
     */
    public List<Todo> get(Boolean completed);

    /**
     * 查询所有Todo
     * @return
     */
    public List<Todo> get();

    /**
     * 查询指定Todo
     * @param id
     * @return
     */
    public Todo get(long id);

    /**
     * 新增一条Todo
     * @param todo
     * @return
     */
    public Todo create(Todo todo);

    /**
     * 删除所有已完成Todo
     * @return
     */
    public List<Todo> deleteCompleted();

    /**
     * 删除指定Todo
     * @param id
     * @return
     */
    public List<Todo> delete(long id);

    /**
     * 更新一条Todo
     * @param id
     * @param todo
     * @return
     */
    public Todo update(long id, Todo todo);
}
