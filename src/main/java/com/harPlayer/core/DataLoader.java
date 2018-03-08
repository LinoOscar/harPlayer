package com.harPlayer.core;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {}
}
