package dev.orewaee.version;

import com.google.gson.annotations.SerializedName;

public class LatestRelease {
    @SerializedName("html_url")
    private final String htmlUrl;

    @SerializedName("tag_name")
    private final String tagName;

    public LatestRelease(String htmlUrl, String tagName) {
        this.htmlUrl = htmlUrl;
        this.tagName = tagName;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getTagName() {
        return tagName;
    }
}
