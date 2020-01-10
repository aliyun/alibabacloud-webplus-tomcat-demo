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

package com.alibaba.webplus.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.webplus.demo.helper.DbHelper;
import com.alibaba.webplus.demo.model.Todo;
import com.alibaba.webplus.demo.model.InternationalizationLambda;
import com.alibaba.webplus.demo.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Baiji
 * @author Aomo
 */
@RestController
@RequestMapping("/todolist")
public class TodoListController extends BaseController {

    @Autowired(required = false)
    private TodoService todoService;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView index(Map<String, Object> model) throws IOException {
        model.put(InternationalizationLambda.DEFAULT_MODEL_KEY, this.getI18nLambda());
        model.putAll(this.getCommonModel());

        if (DbHelper.isMysqlDatabasePresent()) {
            return new ModelAndView("todolist", model);
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping(value = "/api/{id}")
    public Todo findById(@PathVariable("id") Long id) {
        return this.todoService.getById(id);
    }

    @GetMapping(value = "/api")
    public List<Todo> list(@RequestParam(value = "completed", required = false) Boolean completed) {
        if (completed == null) {
            return this.todoService.getAll();
        }
        return this.todoService.getByStatus(completed);
    }

    @PostMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody Todo item) {
        this.todoService.create(item);
    }

    @PutMapping(value = "/api/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable("id") Long id, @RequestBody Todo item) {
        this.todoService.update(id, item);
    }

    @DeleteMapping("/api/{id}")
    public void removeById(@PathVariable("id") Long id) {
        this.todoService.removeById(id);
    }

    @DeleteMapping("/api")
    public void removeByStatus(@RequestParam("completed") Boolean completed) {
        this.todoService.removeByStatus(completed);
    }

    @GetMapping("/api/count")
    public Map<String, Integer> count() {
        return this.todoService.count();
    }

}
