package com.alibaba.webplus.demo.domain;

public class Todo {

    private long id;
    private String title;
    private Boolean completed;

    public Todo() {
    }

    public Todo(String title) {
        this.title = title;
    }

    public Todo(long id, String title, Boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Todo todo = (Todo) o;

        if (id != todo.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int)(id) * title.hashCode();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Todo merge(Todo newTodo) {
        return new Todo(id,
            nonNull(newTodo.title, title),
            nonNull(newTodo.completed, completed));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private <T> T nonNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
