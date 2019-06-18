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

package com.alibaba.webplus.demo.model;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 奥陌
 */
@Slf4j
public class MustacheLocalizationLambda implements Mustache.Lambda {

    public static final String DEFAULT_MODEL_KEY = "i18n";

    private static final Pattern KEY_PATTERN = Pattern.compile("(.*?)[\\s\\[]");
    private static final Pattern ARGS_PATTERN = Pattern.compile("\\[(.*?)\\]");

    private MessageSource messageSource;

    public MustacheLocalizationLambda(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void execute(Template.Fragment fragment, Writer out) throws IOException {
        String frag = fragment.execute();
        String key = this.extractKey(frag);
        List<String> args = this.extractParameters(frag);
        String text = this.messageSource.getMessage(key, args.toArray(), LocaleContextHolder.getLocale());
        out.write(text);
    }

    private String extractKey(String key) {
        Matcher matcher = KEY_PATTERN.matcher(key);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return key;
    }

    private List<String> extractParameters(String key) {
        final Matcher matcher = ARGS_PATTERN.matcher(key);
        final List<String> args = new ArrayList<>();
        while (matcher.find()) {
            args.add(matcher.group(1));
        }
        return args;
    }

}
