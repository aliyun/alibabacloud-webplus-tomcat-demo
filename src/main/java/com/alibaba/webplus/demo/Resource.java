package com.alibaba.webplus.demo;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@XmlRootElement
public class Resource<T> {

    private T content;

    public Resource() {
    }

    public Resource(T content) {
        this.content = content;
    }

    @JsonUnwrapped
    @XmlAnyElement
    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
