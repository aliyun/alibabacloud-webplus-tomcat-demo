package com.alibaba.webplus.demo.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.alibaba.webplus.demo.Resource;
import com.alibaba.webplus.demo.domain.Todo;
import com.alibaba.webplus.demo.domain.TodoDao;
import com.alibaba.webplus.demo.model.MustacheLocalizationLambda;
import com.alibaba.webplus.demo.service.TodoService;

import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author 白寂
 */
@RestController
@RequestMapping(value = "/todos")
public class TodosController {

    private final String SUPPORTED_RDS_ENGINE = "MySQL";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoDao todoDao;

    @Value("${site.id}")
    private String siteId;

    @Value("${quickstart.repo.url}")
    private String quickstartRepoUrl;

    @Value("${webplus.console.url}")
    private String consoleUrl;

    private Mustache.Lambda i18n;


    @PostConstruct
    public void init() {
        this.i18n = new MustacheLocalizationLambda(this.messageSource);
    }

    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(Map<String, Object> model) throws IOException {
        model.put(MustacheLocalizationLambda.DEFAULT_MODEL_KEY, i18n);

        model.put("siteId", this.siteId);
        model.put("quickstartRepoUrl", this.quickstartRepoUrl);
        model.put("consoleUrl", this.consoleUrl);

        String rds_engine = System.getenv("WP_RDS_ENGINE");
        //rds_engine = "MySQL";
        if(SUPPORTED_RDS_ENGINE.equals(rds_engine)) {
            todoDao.createTable();
            return new ModelAndView("todos", model);
        } else {
            return new ModelAndView("redirect:/", model);
        }
    }

    /**
     * 查询所有已完成或未完成的Todo
     * @param completed
     * @return
     */
    @RequestMapping(method = GET)
    public HttpEntity<Collection<Resource>> getTodo(
        @RequestParam(value = "completed", required = false) Boolean completed) {
        List<Resource> resources;
        List<Todo> todos;

        if(null != completed) {
            todos = todoService.get(completed);
        } else {
            todos = todoService.get();
            todos = todoService.get();
        }

        resources = todos.stream().map(
            todo -> toResource(todo)).collect(Collectors.toList());
        return new ResponseEntity<>(resources, OK);
    }

    /**
     * 查询指定的Todo
     * @param id
     * @return
     */
    @RequestMapping(value = "/{todo-id}", method = GET)
    public HttpEntity<Resource> getTodo(@PathVariable("todo-id") long id) {
        Todo todo = todoService.get(id);

        if (null == todo) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        return respondWithResource(todo, OK);
    }

    /**
     * 新增一条Todo
     * @param todo
     * @return
     */
    @RequestMapping(method = POST,  headers = {"Content-type=application/json"})
    public HttpEntity<Resource> createTodo(@RequestBody Todo todo) {
        return respondWithResource(todoService.create(todo), CREATED);
    }

    /**
     * 删除所有已完成的Todo
     * @return
     */
    @RequestMapping(method = DELETE)
    public HttpEntity<Collection<Resource>> deleteCompletedTodo() {
        List<Todo> todos = todoService.deleteCompleted();
        List<Resource> resources = todos.stream().map(
                todo -> toResource(todo)).collect(Collectors.toList());
        return new ResponseEntity<>(resources, OK);
    }


    /**
     * 删除指定的Todo
     * @param id
     * @return
     */
    @RequestMapping(value = "/{todo-id}", method = DELETE)
    public HttpEntity<Collection<Resource>> deleteTodo(@PathVariable("todo-id") long id) {
        List<Todo> todos = todoService.delete(id);

        List<Resource> resources = todos.stream().map(
            todo -> toResource(todo)).collect(Collectors.toList());
        return new ResponseEntity<>(resources, OK);
    }


    /**
     * 更新一条Todo
     * @param id
     * @param newTodo
     * @return
     */
    @RequestMapping(value = "/{todo-id}", method = PATCH, headers = {"Content-type=application/json"})
    public HttpEntity<Resource> updateTodo(@PathVariable("todo-id") long id, @RequestBody Todo newTodo) {
        Todo todo = todoService.update(id, newTodo);

        return respondWithResource(todo, OK);
    }

    private Resource toResource(Todo todo) {
        return new Resource(todo);
    }

    private HttpEntity<Resource> respondWithResource(Todo todo, HttpStatus statusCode) {
        Resource resource = toResource(todo);

        return new ResponseEntity<>(resource, statusCode);
    }

}
