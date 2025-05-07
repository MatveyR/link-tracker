package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Configs.ScrapperPropsConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapperScheduler {
    private final List<Scrapper> scrappers;

    @Scheduled(fixedRateString = "${scrapper.interval}")
    public void runScheduledScrapping() {
        scrappers.forEach(scrapper -> {
            try {
                scrapper.trackUpdates();
            } catch (Exception e) {
                log.error("Ошибка планировщика {}", e.getMessage());
            }
        });
    }
}
