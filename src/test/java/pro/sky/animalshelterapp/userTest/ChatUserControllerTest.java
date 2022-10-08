package pro.sky.animalshelterapp.userTest;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.animalshelterapp.controller.ChatUserController;
import pro.sky.animalshelterapp.model.ChatUser;
import pro.sky.animalshelterapp.repository.AnimalRepository;
import pro.sky.animalshelterapp.repository.ChatUserRepository;
import pro.sky.animalshelterapp.service.serviceImpl.AnimalServiceImpl;
import pro.sky.animalshelterapp.service.serviceImpl.ChatUserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pro.sky.animalshelterapp.DataTest.*;


@WebMvcTest(controllers = ChatUserController.class)
public class ChatUserControllerTest {

    private ChatUser chatUser1;
    private ChatUser chatUser2;
    private JSONObject chatUserObject1;
    private JSONObject chatUserObject2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatUserRepository chatUserRepository;

    @MockBean
    private AnimalRepository animalRepository;

    @SpyBean
    private ChatUserServiceImpl chatUserService;

    @SpyBean
    private AnimalServiceImpl animalService;

    @InjectMocks
    private ChatUserController chatUserController;

    @BeforeEach
    public void setUp() throws Exception {
        chatUserObject2 = new JSONObject();
        chatUserObject1 = new JSONObject();
        chatUserObject1.put("id", chatUSER_ID_1);
        chatUserObject1.put("chat_id", chatUSER_CHAT_ID_1);
        chatUserObject1.put("name", chatUSER_NAME_1);
        chatUserObject1.put("phoneNumber", chatUSER_PHONE_1);
        chatUserObject1.put("email", chatUSER_EMAIL_1);

        chatUser1 = new ChatUser(chatUSER_NAME_1, chatUSER_PHONE_1, chatUSER_EMAIL_1);
        chatUser1.setId(chatUSER_ID_1);
        chatUser1.setChatId(chatUSER_CHAT_ID_1);

        chatUser2 = new ChatUser(chatUSER_NAME_2, chatUSER_PHONE_2, chatUSER_EMAIL_2);
        chatUser2.setId(chatUSER_ID_2);
        chatUser2.setChatId(chatUSER_CHAT_ID_2);
        chatUser2.setStatus(chatUSER_STATUS_2);
    }

    @Test
    public void testShouldCreateChatUser() throws Exception {
        when(chatUserRepository.save(any(ChatUser.class))).thenReturn(chatUser1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user?type=DOG")
                        .content(chatUserObject1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chatUSER_ID_1))
                .andExpect(jsonPath("$.chatId").value(chatUSER_CHAT_ID_1))
                .andExpect(jsonPath("$.name").value(chatUSER_NAME_1))
                .andExpect(jsonPath("$.phoneNumber").value(chatUSER_PHONE_1))
                .andExpect(jsonPath("$.email").value(chatUSER_EMAIL_1));
    }

    @Test
    public void testShouldEditChatUser() throws Exception {
        chatUserObject2.put("id", chatUSER_ID_1);
        chatUserObject2.put("chat_id", chatUSER_CHAT_ID_1);
        chatUserObject2.put("name", chatUSER_NAME_1);
        chatUserObject2.put("phoneNumber", chatUSER_PHONE_1);
        chatUserObject2.put("email", chatUSER_EMAIL_2);

        chatUser1.setEmail(chatUSER_EMAIL_2);
        when(chatUserRepository.save(any(ChatUser.class))).thenReturn(chatUser1);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user?chatId=123&type=DOG&status=GUEST")
                        .content(chatUserObject2.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chatUSER_ID_1))
                .andExpect(jsonPath("$.chatId").value(chatUSER_CHAT_ID_1))
                .andExpect(jsonPath("$.name").value(chatUSER_NAME_1))
                .andExpect(jsonPath("$.phoneNumber").value(chatUSER_PHONE_1))
                .andExpect(jsonPath("$.email").value(chatUSER_EMAIL_2));
    }

    @Test
    public void testShouldDeleteChatUser() throws Exception {
        doNothing().when(chatUserRepository).deleteById(any(Long.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/123")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testShouldGetChatUser() throws Exception {
        when(chatUserRepository.findById(any(Long.class))).thenReturn(Optional.of(chatUser1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/123")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chatUSER_ID_1))
                .andExpect(jsonPath("$.chatId").value(chatUSER_CHAT_ID_1))
                .andExpect(jsonPath("$.name").value(chatUSER_NAME_1))
                .andExpect(jsonPath("$.phoneNumber").value(chatUSER_PHONE_1))
                .andExpect(jsonPath("$.email").value(chatUSER_EMAIL_1));
    }

    @Test
    public void testShouldGetAllUsers() throws Exception {
        List<ChatUser> chatUserList = Arrays.asList(chatUser1, chatUser2);

        when(chatUserRepository.findAll()).thenReturn(chatUserList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(chatUSER_ID_1))
                .andExpect(jsonPath("$[1].id").value(chatUSER_ID_2))
                .andExpect(jsonPath("$[0].name").value(chatUSER_NAME_1))
                .andExpect(jsonPath("$[1].name").value(chatUSER_NAME_2));
    }
}
