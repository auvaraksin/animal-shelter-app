package pro.sky.animalshelterapp.reportTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelterapp.model.Report;
import pro.sky.animalshelterapp.repository.ReportRepository;
import pro.sky.animalshelterapp.service.ChatUserService;
import pro.sky.animalshelterapp.service.serviceImpl.ReportServiceImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.animalshelterapp.DataTest.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    private Report report1;
    private Report report2;

    @Mock
    private ReportRepository reportRepositoryMock;

    @Mock
    private ChatUserService chatUserServiceMock;

    @InjectMocks
    private ReportServiceImpl out;

    @BeforeEach
    public void setUp() {
        report1 = new Report();
        report2 = new Report();
        report1.setId(REPORT_ID_1);
        report2.setId(REPORT_ID_2);
        report1.setClientId(chatUSER_ID_1);
        report2.setClientId(chatUSER_ID_1);
        out = new ReportServiceImpl(reportRepositoryMock, chatUserServiceMock);
    }

    @Test
    public void testShouldSaveReport() {
        when(reportRepositoryMock.save(report1)).thenReturn(report1);
        assertEquals(report1, out.saveReport(report1));
        verify(reportRepositoryMock, times(1)).save(report1);
    }

    @Test
    public void testShouldThrowNullPointerExceptionAfterHandlePhoto()  {
        assertThrows(NullPointerException.class,
                () -> out.handlePhoto(REPORT_MESSAGE,FILE_SIZE_1,FILE_PATH_1, REPORT_TEXT_1));
    }


    @Test
    public void testShouldThrowIOExceptionGeneratePhotoPreview() {
        assertThrows(IOException.class,
                () -> out.generatePhotoPreview(FILE_PATH_2));
    }

    @Test
    public void testShouldGetReportsByUserId() {
        List<Report> reports = List.of(report1,report2);
        when(reportRepositoryMock.findByUserId(any(Long.class))).
                thenReturn(Optional.of(Optional.of(reports).orElse(null)));
        assertEquals(reports, out.getReportsByUserId(chatUSER_ID_1));
    }

    @Test
    public void testShouldGetById() {

        when(reportRepositoryMock.findById(any(Long.class))).
                thenReturn(Optional.of(Optional.of(report1).orElse(null)));
        assertEquals(report1, out.getById(REPORT_ID_1));

    }

    @Test
    public void testShouldGetLastReportByChatUserId() {
        when(reportRepositoryMock.findLastReportByUserId(chatUSER_ID_1)).thenReturn(Optional.ofNullable(report1));
        assertEquals(report1, out.getLastReportByUserId(chatUSER_ID_1));
    }

    @Test
    public void testShouldGetDateOfLastReportByUserId() {
        when(reportRepositoryMock.findDateOfLastReportByUserId(chatUSER_ID_1)).thenReturn(Optional.of(SENT_DATE_1));
        assertEquals(SENT_DATE_1, out.getDateOfLastReportByUserId(chatUSER_ID_1));
    }

    @Test
    public void testShouldCheckIfReportWasSentToday() {
        when(reportRepositoryMock.findDateOfLastReportByUserId(chatUSER_ID_1)).thenReturn(Optional.of(SENT_DATE_1));
        assertTrue(out.reportWasSentToday(SENT_DATE_1, chatUSER_ID_1));

    }

    @Test
    public void testShouldCountUserReports() {
        when(reportRepositoryMock.countReportsByClientId(chatUSER_ID_1)).thenReturn(Optional.of(1));
        assertEquals(1, out.countUserReports(chatUSER_ID_1));
    }

    @Test
    public void testShouldEditReportStatus() {
        when(reportRepositoryMock.save(report2)).thenReturn(report2);
        report2.setStatus(Report.ReportStatus.ACCEPTED);
        assertEquals(report2, out.editReportByVolunteer(report2, REPORT_STATUS_2));
    }
}
