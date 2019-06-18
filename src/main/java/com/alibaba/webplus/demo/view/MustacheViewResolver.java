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

package com.alibaba.webplus.demo.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * @author 奥陌
 */
@Component
public class MustacheViewResolver extends AbstractTemplateViewResolver {

    @Autowired
    private MustacheTemplateFactory templateFactory;

    public MustacheViewResolver() {
        setViewClass(MustacheView.class);
        setPrefix("/WEB-INF/views/");
        setSuffix(".mustache");
        setCache(true);
    }

    @Override
    protected Class<?> requiredViewClass() {
        return MustacheView.class;
    }

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        MustacheView view = (MustacheView)super.buildView(viewName);
        MustacheTemplate template = this.templateFactory.getTemplate(view.getUrl());
        view.setTemplate(template);
        return view;
    }

}
