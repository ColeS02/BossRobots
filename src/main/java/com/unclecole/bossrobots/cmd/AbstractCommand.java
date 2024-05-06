package com.unclecole.bossrobots.cmd;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements Executable {

    @Getter
    public List<String> alias = new ArrayList<>();
    @Getter
    private String label;
    @Getter
    private boolean playerRequired;

    public AbstractCommand(String label, boolean playerRequired, String... alias) {
        this.alias.addAll(Arrays.asList(alias));
        this.label = label;
        this.playerRequired = playerRequired;
    }
}