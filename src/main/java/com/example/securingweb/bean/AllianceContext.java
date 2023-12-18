package com.example.securingweb.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AllianceContext {
    private static ThreadLocal<List<String>> currentAlliance = new ThreadLocal<>();

    public static List<String> getCurrentAlliance() {
        return currentAlliance.get();
    }

    public static void setCurrentAlliance(List<String> alliance) {
        currentAlliance.set(alliance);
    }

    public static void clear() {
        currentAlliance.remove();
    }

    private AllianceContext() {
        //No Ops
    }

}
