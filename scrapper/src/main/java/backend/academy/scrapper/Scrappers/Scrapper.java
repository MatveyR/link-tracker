package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Data.Models.Link;

public interface Scrapper {
    void trackUpdates();
    void notifySubscribers(Link link);
}
