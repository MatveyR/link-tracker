package backend.academy.scrapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import backend.academy.scrapper.Scrappers.Scrapper;
import backend.academy.scrapper.Scrappers.ScrapperScheduler;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ScrapperSchedulerTest {

    @Mock
    private Scrapper scrapper1;

    @Mock
    private Scrapper scrapper2;

    @InjectMocks
    private ScrapperScheduler scheduler;

    @BeforeEach
    void setUp() {
        scrapper1 = mock(Scrapper.class);
        scrapper2 = mock(Scrapper.class);
        scheduler = new ScrapperScheduler(List.of(scrapper1, scrapper2));
    }

    @Test
    void runScheduledScrapping_CallsAllScrappers() {
        doNothing().when(scrapper1).trackUpdates();
        doNothing().when(scrapper2).trackUpdates();

        scheduler.runScheduledScrapping();

        verify(scrapper1, times(1)).trackUpdates();
        verify(scrapper2, times(1)).trackUpdates();
    }

    @Test
    void runScheduledScrapping_HandlesExceptions() {
        doThrow(new RuntimeException("Test error")).when(scrapper1).trackUpdates();
        doNothing().when(scrapper2).trackUpdates();

        assertDoesNotThrow(() -> scheduler.runScheduledScrapping());

        verify(scrapper1, times(1)).trackUpdates();
        verify(scrapper2, times(1)).trackUpdates();
    }
}
