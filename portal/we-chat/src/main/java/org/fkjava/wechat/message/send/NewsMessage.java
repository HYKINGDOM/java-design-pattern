package org.fkjava.wechat.message.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class NewsMessage extends OutMessage {

    private News news;

    public NewsMessage() {
        super.setMsgType("news");
    }

    /**
     * 只能设置一个图文消息
     *
     * @param article 把图文消息的内容放入消息体里面，后面设置进去的图文消息，会把前面设置的图文消息覆盖。
     */
    public void setArticle(Article article) {
        news = new News();
        news.setArticles(new LinkedList<>());
        news.getArticles().add(article);
    }

    @Getter
    @Setter
    private static class News {
        private List<Article> articles;
    }

    @Getter
    @Setter
    public static class Article {
        private String title;
        private String description;
        private String url;
        @JsonProperty("picurl")
        private String picUrl;
    }
}
