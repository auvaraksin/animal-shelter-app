package pro.sky.animalshelterapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.interfaces.ClientService;
import pro.sky.animalshelterapp.interfaces.ReportService;
import pro.sky.animalshelterapp.models.Report;
import pro.sky.animalshelterapp.repositories.ReportRepository;

import java.util.Collection;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    public ReportServiceImpl(ReportRepository reportRepository, ClientService clientService) {
        this.reportRepository = reportRepository;
        this.clientService = clientService;
    }

    public void createReport(Report report) {
        logger.info("Method to create a new record in the DB in table 'Report' was invoked");
        reportRepository.save(report);
    }

    public Report findReport(Long reportId) {
        logger.info("Method to find record by Id in the DB in table 'Report' was invoked");
        return reportRepository.findById(reportId).orElse(new Report());
    }

    public Collection<Report> findAllByClientName(String name) {
        logger.info("Method to show all records in the DB in table 'Report' interconnecting with Client's name was invoked");
        return reportRepository.findByChatId(clientService.findByName(name).getChatId());
    }

    public Report updateReportStatus(Long reportId, boolean status) {
        logger.info("Method to update the record in the DB in table 'Report' was invoked. Set report's status");
        Report report = reportRepository.findById(reportId).orElse(new Report());
        report.setStatus(status);
        reportRepository.save(report);
        return report;
    }
}