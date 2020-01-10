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
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aomo
 */
public class BaseController {

    private static final int INITIAL_CAPACITY = 10;

    private static final String SITE_NAME_KEY = "WP_SITE_NAME";

    private static final String INTERNATIONAL_SITE = "international";
    private static final String CHINA_SITE_DOC_BASE = "help.aliyun.com/document_detail";
    private static final String INTERNATIONAL_SITE_DOC_BASE = "www.alibabacloud.com/help/doc-detail";
    private static final String CHINA_SITE_DOMAIN_PREFIX = "webplus";
    private static final String INTL_SITE_DOMAIN_PREFIX = "webplus-intl";
    private static final String TEMPLATE_PREFIX = "%{";
    private static final String TEMPLATE_SUFFIX = "}";

    @Value("${webplus.site.id}")
    private String siteId;

    @Value("${webplus.console.url.template}")
    private String consoleUrlTemplate;

    @Value("${webplus.quickstart.doc.url.template}")
    private String quickstartDocUrlTemplate;

    @Value("${webplus.quickstart.repo.url}")
    private String quickstartRepoUrl;

    @Autowired
    private MessageSource messageSource;

    private StringSubstitutor substitutor;

    private InternationalizationLambda i18nLambda;

    public BaseController() {
        Map<String, Object> args = new HashMap<>(INITIAL_CAPACITY);
        args.put("docBase", this.isInternationalSite() ? INTERNATIONAL_SITE_DOC_BASE : CHINA_SITE_DOC_BASE);
        args.put("domainPrefix", this.isInternationalSite() ? INTL_SITE_DOMAIN_PREFIX : CHINA_SITE_DOMAIN_PREFIX);
        this.substitutor = new StringSubstitutor(args, TEMPLATE_PREFIX, TEMPLATE_SUFFIX);
    }

    @PostConstruct
    public void init() {
        this.i18nLambda = new InternationalizationLambda(this.messageSource);
    }

    protected boolean isInternationalSite() {
        return INTERNATIONAL_SITE.equals(System.getenv(SITE_NAME_KEY));
    }

    protected StringSubstitutor getSubstitutor() {
        return this.substitutor;
    }

    protected InternationalizationLambda getI18nLambda() {
        return this.i18nLambda;
    }

    protected Map<String, Object> getCommonModel() {
        Map<String, Object> model = new HashMap<>(INITIAL_CAPACITY);
        model.put("intl", this.isInternationalSite());
        model.put("siteId", this.siteId);
        model.put("consoleUrl", this.substitutor.replace(this.consoleUrlTemplate));
        model.put("quickstartDocUrl", this.getSubstitutor().replace(this.quickstartDocUrlTemplate));
        model.put("quickstartRepoUrl", this.quickstartRepoUrl);
        return model;
    }

}
