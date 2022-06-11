package com.example.avaliacao_2;

public class Photo {
    private Long id;
    private String previewURL;
    private String categories;
    public Photo(Long id, String previewURL, String categories) {
        this.id = id;
        this.previewURL = previewURL;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
