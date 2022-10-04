package pro.sky.animalshelterapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelterapp.models.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
