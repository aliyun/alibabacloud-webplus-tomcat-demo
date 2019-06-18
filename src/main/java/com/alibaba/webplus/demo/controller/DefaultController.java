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

import com.alibaba.webplus.demo.model.MustacheLocalizationLambda;
import com.alibaba.webplus.demo.service.NewsService;
import com.samskivert.mustache.Mustache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class DefaultController {

    private static final int ENVS_INITIAL_CAPACITY = 10;

    private static final String APP_REGION_ID_KEY = "WP_APP_REGION_ID";
    private static final String APP_ID_KEY = "WP_APP_ID";
    private static final String APP_NAME_KEY = "WP_APP_NAME";
    private static final String ENV_ID_KEY = "WP_ENV_ID";
    private static final String ENV_NAME_KEY = "WP_ENV_NAME";
    private static final String CHANGE_TRIGGER_FROM_KEY = "WP_CHANGE_TRIGGER_FROM";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NewsService newsService;

    @Value("${site.id}")
    private String siteId;

    @Value("${quickstart.doc.url}")
    private String quickstartDocUrl;

    @Value("${quickstart.repo.name}")
    private String quickstartRepoName;

    @Value("${quickstart.repo.url}")
    private String quickstartRepoUrl;

    @Value("${app.url}")
    private String appUrl;

    @Value("${env.url}")
    private String envUrl;

    @Value("${next.step.show:false}")
    private boolean showNextSteps;

    @Value("${next.step.package.url}")
    private String nextStepPackageUrl;

    @Value("${webplus.console.url}")
    private String consoleUrl;

    private Mustache.Lambda i18n;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        this.i18n = new MustacheLocalizationLambda(this.messageSource);
    }

    @GetMapping(value = "/")
    public ModelAndView index(Map<String, Object> model, HttpServletResponse response) throws IOException {
        model.put(MustacheLocalizationLambda.DEFAULT_MODEL_KEY, i18n);

        model.put("news", this.newsService.fetchNews());
        model.put("siteId", this.siteId);
        model.put("quickstartDocUrl", this.quickstartDocUrl);
        model.put("quickstartRepoName", this.quickstartRepoName);
        model.put("quickstartRepoUrl", this.quickstartRepoUrl);
        model.put("appUrl", this.appUrl);
        model.put("envUrl", this.envUrl);
        model.put("nextStep", this.showNextSteps);
        model.put("nextStepPackageUrl", this.nextStepPackageUrl);
        model.put("consoleUrl", this.consoleUrl);
        model.put("envs", this.getEnvs());

        return new ModelAndView("index", model);
    }

    private Map<String, Object> getEnvs() {
        Map<String, Object> envs = new HashMap<>(ENVS_INITIAL_CAPACITY);
        envs.put("appRegionId", System.getenv(APP_REGION_ID_KEY));
        envs.put("appId", System.getenv(APP_ID_KEY));
        envs.put("appName", System.getenv(APP_NAME_KEY));
        envs.put("envId", System.getenv(ENV_ID_KEY));
        envs.put("envName", System.getenv(ENV_NAME_KEY));
        envs.put("fromCLI", "CLI".equals(System.getenv(CHANGE_TRIGGER_FROM_KEY)));
        envs.put("fromConsole", "Console".equals(System.getenv(CHANGE_TRIGGER_FROM_KEY)));
        log.debug("envs = {}", envs);
        return envs;
    }

}
