package com.tdf.search.entities;


public enum PromptKey{
    STORY("story"), HOTEL("hotel"), JAVA("java"), GENERIC("generic"), TECH("tech");

    private final String promptKey;

    PromptKey(String promptId) {
        this.promptKey = promptId;
    }

    public static PromptKey getPromptKey(String promptKey) {
        for (PromptKey key : PromptKey.values()) {
            if (key.promptKey.equals(promptKey)) {
                return key;
            }
        }
        throw new IllegalArgumentException("Unknown prompt key: " + promptKey);
    }
}