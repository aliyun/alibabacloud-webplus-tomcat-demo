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

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 奥陌
 */
@Data
public class News {

    private Metadata metadata = new Metadata();

    private String content;

    @Override
    public String toString() {
        return "\n"
            + "Metadata:\n" + this.metadata
            + "Content:\n" + this.content;
    }

    @Data
    public static class Metadata {

        private String title;

        private String author;

        private String date;

        private String excerpt;

        private String permalink;

        private List<String> categories = new ArrayList<>();

        private List<String> tags = new ArrayList<>();

        public Metadata() {
        }

        public Metadata(Map<String, List<String>> data) {
            this.title = this.getString(data, "title");
            this.author = this.getString(data, "author");
            this.date = this.getString(data, "date");
            this.excerpt = this.getString(data, "excerpt");
            this.permalink = this.getString(data, "permalink");
            this.categories = this.getList(data, "categories");
            this.tags = this.getList(data, "tags");
        }

        private String getString(Map<String, List<String>> data, String key) {
            List<String> list = data.get(key);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            return null;
        }

        private List<String> getList(Map<String, List<String>> data, String key) {
            List<String> list = data.get(key);
            if (list != null) {
                return list;
            }
            return new ArrayList<>();
        }

        @Override
        public String toString() {
            return ""
                + String.format("\ttitle: %s\n", this.title)
                + String.format("\tauthor: %s\n", this.author)
                + String.format("\tdate: %s\n", this.date)
                + String.format("\texcerpt: %s\n", this.excerpt)
                + String.format("\tpermalink: %s\n", this.permalink)
                + String.format("\tcategories: %s\n", this.categories)
                + String.format("\ttags: %s\n", this.tags);
        }

    }

}
