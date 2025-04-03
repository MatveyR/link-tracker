package backend.academy.bot.Configs;

import backend.academy.bot.Clients.ChatClient;
import backend.academy.bot.Clients.LinkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ScrapperClientConfig {

    @Value("${scrapper.api.base-url}")
    private String scrapperBaseUrl;

    @Bean
    public ChatClient scrapperClient() {
        return createClient(scrapperBaseUrl, ChatClient.class);
    }

    @Bean
    public LinkClient linkClient() {
        return createClient(scrapperBaseUrl, LinkClient.class);
    }

    private <T> T createClient(String baseUrl, Class<T> clientType) {
        RestClient restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();

        return HttpServiceProxyFactory.builder()
            .exchangeAdapter(RestClientAdapter.create(restClient))
            .build()
            .createClient(clientType);
    }
}
