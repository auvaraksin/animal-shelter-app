package pro.sky.animalshelterapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.interfaces.ReportService;
import pro.sky.animalshelterapp.models.Report;
import pro.sky.animalshelterapp.repositories.ReportRepository;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void createReport(Report report) {
        logger.info("Method to create a new record in the DB in table 'Report' was invoked ");
        reportRepository.save(report);
    }
}