package pro.sky.animalshelterapp.reportTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.animalshelterapp.controller.ReportController;
import pro.sky.animalshelterapp.model.Animal;
import pro.sky.animalshelterapp.model.ChatUser;
import pro.sky.animalshelterapp.model.Report;
import pro.sky.animalshelterapp.repository.AnimalRepository;
import pro.sky.animalshelterapp.repository.ChatUserRepository;
import pro.sky.animalshelterapp.repository.ReportRepository;
import pro.sky.animalshelterapp.service.serviceImpl.AnimalServiceImpl;
import pro.sky.animalshelterapp.service.serviceImpl.ChatUserServiceImpl;
import pro.sky.animalshelterapp.service.serviceImpl.ReportServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pro.sky.animalshelterapp.DataTest.*;


@WebMvcTest(controllers = ReportController.class)
public class ReportControllerTest {

    private final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private Report report1;
    private Report report2;
    private ChatUser chatUser1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportRepository reportRepository;
    @MockBean
    private ChatUserRepository chatUserRepository;
    @MockBean
    private AnimalRepository animalRepository;

    @SpyBean
    private ReportServiceImpl reportService;
    @SpyBean
    private ChatUserServiceImpl chatUserService;
    @SpyBean
    private AnimalServiceImpl animalService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    public void setUp() throws Exception {

        Animal animal = new Animal(ANIMAL_ID, TYPE);

        chatUser1 = new ChatUser(chatUSER_NAME_1, chatUSER_PHONE_1, chatUSER_EMAIL_1);
        chatUser1.setId(chatUSER_ID_1);
        chatUser1.setChatId(chatUSER_CHAT_ID_1);
        chatUser1.setStatus(ChatUser.ChatUserStatus.ADOPTER_ON_TRIAL);
        chatUser1.setAnimal(animal);

        report1 = new Report(REPORT_ID_1, chatUSER_ID_1, REPORT_TEXT_1, FILE_PATH_1, FILE_SIZE_1, PREVIEW_1, SENT_DATE_1, REPORT_STATUS_1);
        report2 = new Report(REPORT_ID_1, chatUSER_ID_1, REPORT_TEXT_1, FILE_PATH_1, FILE_SIZE_1, PREVIEW_1, SENT_DATE_1, REPORT_STATUS_2);
    }

    @Test
    public void getReportPreviewTest() throws Exception {

        when(reportRepository.findById(any(Long.class))).thenReturn(Optional.of(report1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/1/preview")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE));
        verify(reportRepository, times(1)).findById(REPORT_ID_1);
    }

    @Test
    public void shouldThrowExceptionIfGetReportImageIsNotSuccess() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(reportRepository.findById(any(Long.class))).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class, () -> reportController.getReportImage(REPORT_ID_1, response));
    }

    @Test
    public void getReportTextTest() throws Exception {
        when(reportRepository.findById(any(Long.class))).thenReturn(Optional.of(report1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(REPORT_ID_1))
                .andExpect(jsonPath("$.clientId").value(chatUSER_ID_1))
                .andExpect(jsonPath("$.reportText").value(REPORT_TEXT_1))
                .andExpect(jsonPath("$.filePath").value(FILE_PATH_1))
                .andExpect(jsonPath("$.sentDate").value(SENT_DATE_1.toString()))
                .andExpect(jsonPath("$.status").value(REPORT_STATUS_1.toString()));
    }

    @Test
    public void getAllChatUserReports() throws Exception {

        when(reportRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(List.of(report1)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/getAll/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(REPORT_ID_1))
                .andExpect(jsonPath("$[0].clientId").value(chatUSER_ID_1))
                .andExpect(jsonPath("$[0].reportText").value(REPORT_TEXT_1))
                .andExpect(jsonPath("$[0].filePath").value(FILE_PATH_1))
                .andExpect(jsonPath("$[0].fileSize").value(FILE_SIZE_1))
                .andExpect(jsonPath("$[0].sentDate").value(SENT_DATE_1.toString()))
                .andExpect(jsonPath("$[0].status").value(REPORT_STATUS_1.toString()));
    }

    @Test
    public void editReportTest() throws Exception {
        when(reportRepository.findById(any(Long.class))).thenReturn(Optional.of(report2));
        logger.info("Report: " + reportRepository.findById(REPORT_ID_1).get());
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/report?id=1&status=ACCEPTED")
                )
                .andExpect(status().isOk());
    }
}
