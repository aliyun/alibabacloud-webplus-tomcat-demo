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

package com.alibaba.webplus.demo.service;

import com.alibaba.webplus.demo.model.News;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 奥陌
 */
@Slf4j
@Service
public class NewsService {

    private static final String LANGUAGE_FILE = "languages";
    private static final String INDEX_FILE = "index";
    private static final String DEFAULT_LANGUAGE = "en";

    private static final List<Extension> EXTENSIONS = Arrays.asList(
        AutolinkExtension.create(),
        YamlFrontMatterExtension.create()
    );
    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().build();

    private static List<News> cachedNews;

    @Value("${news.repository.url}")
    private String newsRepositoryUrl;

    public synchronized List<News> fetchNews() throws IOException {
        if (cachedNews != null) {
            log.debug("Use cached news.");
            return cachedNews;
        }

        log.debug("No cached news, fetching...");
        List<URL> newsFileUrls = this.fetchNewsFileUrls();
        List<News> fileContents = new ArrayList<>(newsFileUrls.size());
        for (URL newsFileUrl : newsFileUrls) {
            try (InputStream is = newsFileUrl.openStream()) {
                String content = IOUtils.toString(is, StandardCharsets.UTF_8.name());
                fileContents.add(parseMarkdown(content));
            }
        }
        cachedNews = fileContents;
        return cachedNews;
    }

    private List<URL> fetchNewsFileUrls() throws IOException {
        URL baseUrl = new URL(this.newsRepositoryUrl);

        URL languageFileURL = new URL(baseUrl, LANGUAGE_FILE);
        Set<String> supportedLanguages = new HashSet<>(readFileLines(languageFileURL));
        log.debug("supportedLanguages={}", supportedLanguages);

        String userLanguage = LocaleContextHolder.getLocale().getLanguage();
        log.debug("userLanguage={}", userLanguage);

        String newsLanguage;
        if (supportedLanguages.contains(userLanguage)) {
            newsLanguage = userLanguage;
        } else {
            newsLanguage = DEFAULT_LANGUAGE;
        }
        log.debug("newsLanguage={}", newsLanguage);

        URL indexFileUrl = new URL(baseUrl, newsLanguage + "/" + INDEX_FILE);
        List<String> newsFileNames = readFileLines(indexFileUrl);
        List<URL> newsFileUrls = new ArrayList<>(newsFileNames.size());
        for (String newsFileName : newsFileNames) {
            newsFileUrls.add(new URL(baseUrl, newsLanguage + "/" + newsFileName));
        }
        return newsFileUrls;
    }

    private static List<String> readFileLines(URL fileUrl) throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream is = fileUrl.openStream()) {
            LineIterator lineIterator = IOUtils.lineIterator(is, StandardCharsets.UTF_8.name());
            while (lineIterator.hasNext()) {
                lines.add(lineIterator.nextLine());
            }
        }
        return lines;
    }

    private static News parseMarkdown(String content) {
        YamlFrontMatterVisitor visitor = new YamlFrontMatterVisitor();
        Node document = PARSER.parse(content);
        document.accept(visitor);

        News news = new News();
        news.setMetadata(new News.Metadata(visitor.getData()));
        news.setContent(RENDERER.render(document));
        return news;
    }

}
