package pro.sky.animalshelterapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Report {
    @Id
    @GeneratedValue
    private Long id;
    private Long chatId;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private String text;
    private boolean status;

    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return fileSize == report.fileSize && status == report.status && Objects.equals(id, report.id)
                && Objects.equals(chatId, report.chatId) && Objects.equals(filePath, report.filePath) && Objects.equals(mediaType, report.mediaType)
                && Objects.equals(text, report.text) && Objects.equals(date,report.date);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, chatId, filePath, fileSize, mediaType, text, status, date);
        return result;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", text='" + text + '\'' +
                ", status=" + status +
                '}';
    }
}
