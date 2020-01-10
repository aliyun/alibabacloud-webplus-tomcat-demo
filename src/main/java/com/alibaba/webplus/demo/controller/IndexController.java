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

import com.alibaba.webplus.demo.helper.DbHelper;
import com.alibaba.webplus.demo.model.InternationalizationLambda;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

/**
 * @author Aomo
 */
@Slf4j
@RestController
public class IndexController extends BaseController {

    private static final String APP_NAME_KEY = "WP_APP_NAME";
    private static final String ENV_NAME_KEY = "WP_ENV_NAME";

    @Value("${webplus.quickstart.repo.name}")
    private String quickstartRepoName;

    @Value("${webplus.app.url.template}")
    private String appUrlTemplate;

    @Value("${webplus.env.url.template}")
    private String envUrlTemplate;

    @GetMapping("/")
    public ModelAndView index(Map<String, Object> model) throws IOException {
        model.put(InternationalizationLambda.DEFAULT_MODEL_KEY, this.getI18nLambda());
        model.putAll(this.getCommonModel());
        model.put("quickstartRepoName", this.quickstartRepoName);
        model.put("appName", System.getenv(APP_NAME_KEY));
        model.put("appUrl", this.getSubstitutor().replace(this.appUrlTemplate));
        model.put("envName", System.getenv(ENV_NAME_KEY));
        model.put("envUrl", this.getSubstitutor().replace(this.envUrlTemplate));
        model.put("showTodoList", DbHelper.isMysqlDatabasePresent());
        return new ModelAndView("index", model);
    }

}
