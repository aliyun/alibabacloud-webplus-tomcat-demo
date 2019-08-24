package com.alibaba.webplus.demo.domain;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author xianbintang
 * @date 2019/07/23
 */

@Slf4j
@Repository
public class TodoDao {


    //@Autowired
    //private JdbcTemplate jdbcTemplate;
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    JdbcTemplate jdbcTemplate = (JdbcTemplate)context.getBean("jdbcTemplate");

    public void createTable() {
        final String sql =
              " create table todo("
            + "     id bigint primary key,"
            + "     title varchar(255),"
            + "     completed boolean"
            + " )";
        try {
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            log.info("table todo already exist.");
        }
    }

    public List<Todo> getAll() {
        final String sql="SELECT * FROM todo";
        RowMapper<Todo> rowMapper=new BeanPropertyRowMapper<>(Todo.class);
        return jdbcTemplate.query(sql, rowMapper);
    }


    public Todo getById(long id) {
        final String sql="SELECT id,title,completed FROM todo WHERE id=?";
        RowMapper<Todo> rowMapper=new BeanPropertyRowMapper<>(Todo.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Todo> getByStatus(boolean status) {
        final String sql="SELECT id,title,completed FROM todo WHERE completed=?";
        RowMapper<Todo> rowMapper=new BeanPropertyRowMapper<>(Todo.class);
        return jdbcTemplate.query(sql, rowMapper, status);
    }

    public Todo create(Todo todo) {
        final String sql="INSERT ignore INTO todo(id,title,completed) values (?,?,?)";
        jdbcTemplate.update(sql, todo.getId(), todo.getTitle(), todo.getCompleted());

        return todo;
    }

    public List<Todo> deleteCompleted() {
        final String sql="DELETE FROM todo WHERE completed=?";
        jdbcTemplate.update(sql, true);

        return getAll();
    }

    public void deleteById(long id) {
        final String sql="DELETE FROM todo WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public Todo update(long id, Todo todo) {
        if(todo.getCompleted() == null) {
            final String sql="UPDATE todo SET id=?, title=? WHERE id=?";
            jdbcTemplate.update(sql, todo.getId(), todo.getTitle(), id);
        } else if (todo.getTitle() == null) {
            final String sql="UPDATE todo SET id=?, completed=? WHERE id=?";
            jdbcTemplate.update(sql, todo.getId(), todo.getCompleted(), id);
        }
        return todo;
    }

}
