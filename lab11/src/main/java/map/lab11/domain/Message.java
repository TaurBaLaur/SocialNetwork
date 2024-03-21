package map.lab11.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private Long from;
    private Long to;
    private String content;
    private LocalDateTime data;
    private Long replyId;

    public Message(Long from, Long to, String content, LocalDateTime data) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.data = data;
        this.replyId = null;
    }
    public Message(Long from, Long to, String content, LocalDateTime data, Long replyId) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.data = data;
        this.replyId = replyId;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", content='" + content + '\'' +
                ", data=" + data +
                ", replyId=" + replyId +
                '}';
    }
}
