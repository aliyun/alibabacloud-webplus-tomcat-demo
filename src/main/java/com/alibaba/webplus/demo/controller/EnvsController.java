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

import com.alibaba.webplus.demo.model.InternationalizationLambda;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Baiji
 * @author Aomo
 */
@Slf4j
@RestController
public class EnvsController extends BaseController {

    private static final String WP_ENV_PREFIX = "WP_";

    @Value("${webplus.envs.doc.url.template}")
    private String envsDocUrlTemplate;

    @GetMapping("/envs")
    public ModelAndView index(Map<String, Object> model) throws IOException {
        model.put(InternationalizationLambda.DEFAULT_MODEL_KEY, this.getI18nLambda());
        model.putAll(this.getCommonModel());
        model.put("envsDocUrl", this.getSubstitutor().replace(this.envsDocUrlTemplate));
        model.put("envs", this.getEnvs());
        return new ModelAndView("envs", model);
    }

    private List<ImmutablePair<String, String>> getEnvs() {
        Map<String, String> sysEnvs = System.getenv();
        List<ImmutablePair<String, String>> wpEnvs = new ArrayList<>();
        for (Map.Entry<String, String> entry : sysEnvs.entrySet()) {
            String entryKey = entry.getKey();
            if (entryKey.startsWith(WP_ENV_PREFIX)) {
                wpEnvs.add(new ImmutablePair<>(entryKey, entry.getValue()));
            }
        }
        wpEnvs.sort(Map.Entry.comparingByKey());
        return wpEnvs;
    }

}

