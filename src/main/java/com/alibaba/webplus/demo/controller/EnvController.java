package com.alibaba.webplus.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.alibaba.webplus.demo.model.MustacheLocalizationLambda;

import com.samskivert.mustache.Mustache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 白寂
 */
@Slf4j
@RestController
public class EnvController {

    private static final int ENVS_INITIAL_CAPACITY = 100;
    private static final String WP_ENV_PREFIX = "WP_";

    private static final List<String> envList = new ArrayList<>(ENVS_INITIAL_CAPACITY);
    static{
        for(EnvListEnum w : EnvListEnum.values()){
            envList.add(w.toString());
        }
    }

    @Autowired
    private MessageSource messageSource;

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

    @GetMapping("/env")
    public ModelAndView index(Map<String, Object> model) throws IOException {
        model.put(MustacheLocalizationLambda.DEFAULT_MODEL_KEY, i18n);

        model.put("siteId", this.siteId);
        model.put("quickstartRepoUrl", this.quickstartRepoUrl);
        model.put("consoleUrl", this.consoleUrl);
        model.put("envs", this.getEnvs());

        return new ModelAndView("env", model);
    }

    private Map<String, String> getEnvs() {
        Map<String, String> wpEnvs = new HashMap<>(ENVS_INITIAL_CAPACITY);

        Map<String, String> sysEnvs = System.getenv();
        for (Map.Entry<String, String> entry : sysEnvs.entrySet()) {
            String entryKey = entry.getKey();
            if (entryKey.startsWith(WP_ENV_PREFIX)) {
                if (envList.contains(entryKey)) {
                    String value = System.getenv(entryKey);
                    wpEnvs.put(entryKey, value);
                } else {
                    log.info("webplus environment variable: " + entryKey +
                        " is not in the envList. you may want to update the list.");
                }
            }

        }

        //log.debug("all system env "+ System.getenv());
        return wpEnvs;
    }

}

