package backend.academy.scrapper.Scrappers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapperScheduler {
    private final List<Scrapper> scrappers;

    @Scheduled(fixedRateString = "${scrapper.interval:600}")
    public void runScheduledScrapping() {
        scrappers.forEach(scrapper -> {
            try {
                scrapper.trackUpdates();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
