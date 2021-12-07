package com.example.myschedule;

import java.util.List;

public class Image {

    private List<Images> images;

    private ToolTips tooltips;

    public List<Images> getImages() {
        return images;
    }

    public class  Images{
        private String url;
        private String urlbase;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlbase() {
            return urlbase;
        }

        public void setUrlbase(String urlbase) {
            this.urlbase = urlbase;
        }
    }

    public class ToolTips{

    }
}
