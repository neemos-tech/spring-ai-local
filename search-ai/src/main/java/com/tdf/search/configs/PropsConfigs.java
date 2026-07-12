package com.tdf.search.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropsConfigs {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void test() {
        System.out.println(environment.getProperty("rag-resource.names"));
    }
}
