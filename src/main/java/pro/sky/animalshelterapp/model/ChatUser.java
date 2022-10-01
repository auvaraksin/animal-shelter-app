package pro.sky.animalshelterapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "client")
public class ChatUser {


    public enum ChatUserStatus {

        GUEST,
        ADOPTER_ON_TRIAL,
        ADOPTER_TRIAL_FAILED,
        OWNER

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    private ChatUserStatus status;

    @ManyToOne
    @JoinColumn(name = "animal_type", referencedColumnName = "type")
    private Animal animal;

    @OneToMany(mappedBy = "chatUser", cascade = CascadeType.ALL)
    private List<Report> reportList;

    @Column(name = "start_trial_date")
    private LocalDate startTrialDate;

    @Column(name = "end_trial_date")
    private LocalDate endTrialDate;

    public ChatUser() {
    }

    public ChatUser(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public ChatUser(String name, String phoneNumber, String email, Animal animal) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.animal = animal;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public ChatUserStatus getStatus() {
        return status;
    }

    public void setStatus(ChatUserStatus status) {
        this.status = status;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartTrialDate() {
        return startTrialDate;
    }

    public void setStartTrialDate(LocalDate startTrialDate) {
        this.startTrialDate = startTrialDate;
    }

    public LocalDate getEndTrialDate() {
        return endTrialDate;
    }

    public void setEndTrialDate(LocalDate endTrialDate) {
        this.endTrialDate = endTrialDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatUser)) return false;
        ChatUser chatUser = (ChatUser) o;
        return getId().equals(chatUser.getId()) && getChatId().equals(chatUser.getChatId())
                && getName().equals(chatUser.getName()) && getPhoneNumber().equals(chatUser.getPhoneNumber())
                && getEmail().equals(chatUser.getEmail()) && getStatus() == chatUser.getStatus()
                && getAnimal().equals(chatUser.getAnimal()) && reportList.equals(chatUser.reportList)
                && getStartTrialDate().equals(chatUser.getStartTrialDate())
                && getEndTrialDate().equals(chatUser.getEndTrialDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getChatId(), getName(), getPhoneNumber(), getEmail(), getStatus(),
                getAnimal(), reportList, getStartTrialDate(), getEndTrialDate());
    }

    @Override
    public String toString() {
        return "ChatUser{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", animal=" + animal +
                ", startTrialDate=" + startTrialDate +
                ", endTrialDate=" + endTrialDate +
                '}';
    }
}
