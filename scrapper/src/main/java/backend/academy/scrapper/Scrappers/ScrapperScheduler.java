package backend.academy.scrapper.Scrappers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
