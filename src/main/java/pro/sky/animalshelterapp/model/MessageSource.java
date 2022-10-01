package pro.sky.animalshelterapp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class MessageSource {
 @Id
 private String digest;
 private String responseMessage;

   public MessageSource(){}

    public MessageSource(String digest, String responseMessage) {
        this.digest = digest;
        this.responseMessage = responseMessage;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageSource that = (MessageSource) o;
        return Objects.equals(digest, that.digest) && Objects.equals(responseMessage, that.responseMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digest, responseMessage);
    }

    @Override
    public String toString() {
        return "MessageSource{" +
                "digest='" + digest + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                '}';
    }
}
