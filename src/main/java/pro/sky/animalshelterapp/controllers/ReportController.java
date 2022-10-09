package pro.sky.animalshelterapp.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelterapp.interfaces.ReportService;
import pro.sky.animalshelterapp.models.Report;

@RestController
@RequestMapping("reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/find-client-reports")
    public ResponseEntity getReportsListByClientName(@RequestParam String name) {
        return ResponseEntity.ok(reportService.findAllByClientName(name));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<byte[]> reportReviewAnnexPhoto(@PathVariable Long reportId) {
        Report report = reportService.findReport(reportId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(report.getMediaType()));
        headers.setContentLength(report.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(report.getData());
    }

    @PutMapping("/update-report-status")
    public ResponseEntity updateReportStatus(@RequestParam long id,
                                             @RequestParam Boolean status) {
        return ResponseEntity.ok(reportService.updateReportStatus(id, status));
    }
}
