package pro.sky.animalshelterapp.interfaces;

import pro.sky.animalshelterapp.models.Report;

import java.util.Collection;

public interface ReportService {
    void createReport (Report report);

    Report findReport(Long reportId);

    Collection<Report> findAllByClientName(String name);


    Report updateReportStatus(Long id, boolean status);
}
