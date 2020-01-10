/*
 * MIT License
 *
 * Copyright (c) 2019-present Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.alibaba.webplus.demo.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.webplus.demo.dao.TodoDao;
import com.alibaba.webplus.demo.model.Todo;

import com.alibaba.webplus.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Baiji
 * @author Aomo
 */
@Service
public class TodoServiceImpl implements TodoService {

    @Autowired(required = false)
    private TodoDao dao;

    @Override
    public Todo getById(long id) {
        return this.dao.getById(id);
    }

    @Override
    public List<Todo> getAll() {
        return this.dao.getAll();
    }

    @Override
    public List<Todo> getByStatus(boolean completed) {
        return this.dao.getByStatus(completed);
    }

    @Override
    public void create(Todo item) {
        this.dao.create(item);
    }

    @Override
    public void update(long id, Todo item) {
        dao.update(id, item);
    }

    @Override
    public void removeById(long id) {
        this.dao.removeById(id);
    }

    @Override
    public void removeByStatus(boolean completed) {
        this.dao.removeByStatus(completed);
    }

    @Override
    public Map<String, Integer> count() {
        return this.dao.count();
    }

}
