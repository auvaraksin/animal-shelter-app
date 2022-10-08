package pro.sky.animalshelterapp.userTest;

import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelterapp.model.Animal;
import pro.sky.animalshelterapp.model.ChatUser;
import pro.sky.animalshelterapp.model.Report;
import pro.sky.animalshelterapp.repository.ChatUserRepository;
import pro.sky.animalshelterapp.service.serviceImpl.AnimalServiceImpl;
import pro.sky.animalshelterapp.service.serviceImpl.ChatUserServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pro.sky.animalshelterapp.DataTest.*;


@ExtendWith(MockitoExtension.class)
public class ChatUserServiceTest {

    private ChatUser chatUser1;
    private ChatUser chatUser2;
    private Animal animal;

    @Mock
    private ChatUserRepository chatUserRepositoryMock;

    @Mock
    private AnimalServiceImpl animalServiceMock;

    @InjectMocks
    private ChatUserServiceImpl out;

    @BeforeEach
    public void setUp() {
        chatUser1 = new ChatUser(chatUSER_NAME_1, chatUSER_PHONE_1, chatUSER_EMAIL_1);
        chatUser1 = new ChatUser(chatUSER_NAME_2, chatUSER_PHONE_2, chatUSER_EMAIL_2);
        animal = new Animal(ANIMAL_ID, TYPE);
        out = new ChatUserServiceImpl(chatUserRepositoryMock, animalServiceMock);
    }

    @Test
    public void testShouldSaveChatUser() {
        when(chatUserRepositoryMock.save(chatUser1)).thenReturn(chatUser1);
        assertEquals(chatUser1, out.save(chatUser1));
        verify(chatUserRepositoryMock, times(1)).save(chatUser1);
    }

    @Test
    public void testShouldEditChatUser() {
        chatUser2.setId(chatUSER_ID_2);
        chatUser2.setChatId(chatUSER_CHAT_ID_2);
        chatUser2.setStatus(chatUSER_STATUS_2);
        chatUser2.setAnimal(animal);
        when(chatUserRepositoryMock.save(chatUser2)).thenReturn(chatUser2);
        assertEquals(chatUser2, out.edit(chatUSER_ID_2, chatUSER_CHAT_ID_2, chatUser2, chatUSER_STATUS_2, TYPE));
    }

    @Test
    public void testShouldCreateChatUserByVolunteer() {
        chatUser1.setAnimal(animal);
        when(chatUserRepositoryMock.save(chatUser1)).thenReturn(chatUser1);
        assertEquals(chatUser1, out.createChatUserByVolunteer(chatUser1, TYPE));
    }

    @Test
    public void testShouldGetCorrectResultOfParsingChatUserMessageWithContacts() {
        List<String> expectedResult = List.of(
                chatUser1.getName(),
                chatUser1.getPhoneNumber(),
                chatUser1.getEmail());
        List<String> parseResult = List.of(
                (out.parse(chatUSER_MESSAGE_1, chatUSER_CHAT_ID_1).get().getName()),
                (out.parse(chatUSER_MESSAGE_1, chatUSER_CHAT_ID_1).get().getPhoneNumber()),
                (out.parse(chatUSER_MESSAGE_1, chatUSER_CHAT_ID_1).get().getEmail()));

        assertTrue((out.parse(chatUSER_MESSAGE_1, chatUSER_CHAT_ID_1)).isPresent());
        assertEquals(expectedResult, parseResult);
    }

    @Test
    public void testShouldImplementRegistrationChatUser() {
        chatUser1.setId(chatUSER_ID_1);
        chatUser1.setChatId(chatUSER_CHAT_ID_1);
        chatUser1.setStatus(chatUSER_STATUS_1);
        chatUser1.setAnimal(animal);
        Optional<ChatUser> parseResult = out.parse(chatUSER_MESSAGE_1, chatUSER_CHAT_ID_1);
        when(chatUserRepositoryMock.save(parseResult.get())).thenReturn(chatUser1);
        assertEquals(chatUser1, out.edit(chatUSER_ID_1, chatUSER_CHAT_ID_1, parseResult.get(), chatUSER_STATUS_1, TYPE));
    }

    @Test
    public void testShouldGetChatUserById() {
        when(chatUserRepositoryMock.findById(chatUSER_ID_1)).thenReturn(Optional.ofNullable(chatUser1));
        assertEquals(chatUser1, out.getChatUserById(chatUSER_ID_1));
    }

    @Test
    public void testShouldGetChatUserByChatId() {
        when(chatUserRepositoryMock.findChatUserByChatId(chatUSER_CHAT_ID_1)).thenReturn(chatUser1);
        assertEquals(chatUser1, out.getChatUserByChatId(chatUSER_CHAT_ID_1));
    }

    @Test
    public void testShouldDeleteChatUserById() {
        doNothing().when(chatUserRepositoryMock).deleteById(chatUSER_CHAT_ID_2);
        assertDoesNotThrow(() -> out.deleteChatUserById(chatUSER_CHAT_ID_2));
    }

    @Test
    public void testShouldGetAll() {
        when(chatUserRepositoryMock.findAll()).thenReturn(
                (List.of(chatUser1, chatUser2)));
        assertTrue(CollectionUtils.isEqualCollection(List.of(chatUser1, chatUser2), out.getAllChatUsers()));
    }

    @Test
    public void testShouldCheckIfAdopterOnTrialExists() {
        chatUser2.setChatId(chatUSER_CHAT_ID_2);
        chatUser2.setStatus(chatUSER_STATUS_3);
        when(chatUserRepositoryMock.findChatUserByChatId(chatUSER_CHAT_ID_2)).thenReturn(chatUser2);
        assertTrue(out.adopterOnTrialExists(chatUSER_CHAT_ID_2));

    }

    @Test
    public void testShouldGetAdoptersWithEndOfTrial() {
        List<ChatUser> users = List.of(chatUser1, chatUser2);
        when(chatUserRepositoryMock.findAdoptersWithEndOfTrial(chatUSER_STATUS_3, TEST_TRIAL_DATE)).thenReturn(users);
        assertEquals(users, out.getAdoptersWithEndOfTrial(chatUSER_STATUS_3, TEST_TRIAL_DATE));
    }

    @Test
    public void testShouldGetAdoptersByReportStatusAndSentDate() {
        List<ChatUser> users = List.of(chatUser1, chatUser2);
        when(chatUserRepositoryMock.findAdoptersByReportStatusAndSentDate(Report.ReportStatus.SENT, SENT_DATE)).thenReturn(users);
        assertEquals(users, out.getAdoptersByReportStatusAndSentDate(Report.ReportStatus.SENT, SENT_DATE));

    }

    @Test
    public void testShouldGetAdoptersByStatusAndReportDate() {
        List<ChatUser> users = List.of(chatUser1, chatUser2);
        when(chatUserRepositoryMock.findAdoptersByStatusAndReportDate(chatUSER_STATUS_1, SENT_DATE)).thenReturn(users);
        assertEquals(users, out.getAdoptersByStatusAndReportDate(chatUSER_STATUS_1, SENT_DATE));
    }

    @Test
    public void testShouldGetAdoptersByStatusAndExtendedTrial() {
        chatUser1.setStartTrialDate(START_TRIAL_DATE);
        chatUser2.setStartTrialDate(START_TRIAL_DATE);
        chatUser1.setStatus(chatUSER_STATUS_3);
        chatUser2.setStatus(chatUSER_STATUS_3);
        List<ChatUser> chatUsers = List.of(chatUser1, chatUser2);
        when(chatUserRepositoryMock.findAllAdopters(chatUSER_STATUS_3)).thenReturn(chatUsers);
        assertEquals(chatUsers, out.getAdoptersByStatusAndExtendedTrial(chatUSER_STATUS_3));
    }

}
