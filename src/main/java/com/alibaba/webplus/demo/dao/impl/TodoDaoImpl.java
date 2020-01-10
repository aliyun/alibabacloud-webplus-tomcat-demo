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

package com.alibaba.webplus.demo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.webplus.demo.model.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

/**
 * @author Baiji
 * @author Aomo
 */
@Slf4j
@Repository
public class TodoDaoImpl implements com.alibaba.webplus.demo.dao.TodoDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public Todo getById(long id) {
        String sql = "SELECT id, title, completed FROM todo WHERE id = ?";
        RowMapper<Todo> mapper = new BeanPropertyRowMapper<>(Todo.class);
        return this.jdbcTemplate.queryForObject(sql, mapper, id);
    }

    @Override
    public List<Todo> getAll() {
        String sql = "SELECT * FROM todo";
        RowMapper<Todo> mapper = new BeanPropertyRowMapper<>(Todo.class);
        return this.jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<Todo> getByStatus(boolean completed) {
        String sql = "SELECT id, title, completed FROM todo WHERE completed = ?";
        RowMapper<Todo> mapper = new BeanPropertyRowMapper<>(Todo.class);
        return this.jdbcTemplate.query(sql, mapper, completed);
    }

    @Override
    public void create(Todo item) {
        String sql = "INSERT IGNORE INTO todo(id, title, completed) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(sql, item.getId(), item.getTitle(), item.getCompleted());
    }

    @Override
    public void update(long id, Todo item) {
        if (item.getCompleted() == null) {
            String sql = "UPDATE todo SET title = ? WHERE id = ?";
            this.jdbcTemplate.update(sql, item.getTitle(), id);
        } else if (item.getTitle() == null) {
            String sql = "UPDATE todo SET completed = ? WHERE id = ?";
            this.jdbcTemplate.update(sql, item.getCompleted(), id);
        }
    }

    @Override
    public void removeById(long id) {
        String sql = "DELETE FROM todo WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    @Override
    public void removeByStatus(boolean completed) {
        String sql = "DELETE FROM todo WHERE completed = ?";
        this.jdbcTemplate.update(sql, completed);
    }

    @Override
    public Map<String, Integer> count() {
        String sql = "(SELECT 'total' AS k, COUNT(*) AS v FROM todo)"
            + " UNION"
            + " (SELECT 'completed' AS k, COUNT(*) AS v FROM todo WHERE completed = true)";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(sql);
        Map<String, Integer> result = new HashMap<>(2);
        while (rowSet.next()) {
            result.put(rowSet.getString(1), rowSet.getInt(2));
        }
        return result;
    }

}
