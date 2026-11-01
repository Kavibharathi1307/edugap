package com.edutech.knowledgegap.dto;

public class QuestionOptionResponse {

    private Long id;
    private String optionKey;
    private String optionText;

    public QuestionOptionResponse() {}

    public QuestionOptionResponse(Long id, String optionKey, String optionText) {
        this.id = id;
        this.optionKey = optionKey;
        this.optionText = optionText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionKey() {
        return optionKey;
    }

    public void setOptionKey(String optionKey) {
        this.optionKey = optionKey;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}
