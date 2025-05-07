package backend.academy.bot.Configs;

import backend.academy.bot.Clients.ChatClient;
import backend.academy.bot.Clients.LinkClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@ConfigurationProperties(prefix = "scrapper.api")
public class ScrapperClientConfig {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Bean
    public ChatClient scrapperClient() {
        return createClient(baseUrl, ChatClient.class);
    }

    @Bean
    public LinkClient linkClient() {
        return createClient(baseUrl, LinkClient.class);
    }

    private <T> T createClient(String baseUrl, Class<T> clientType) {
        RestClient restClient = RestClient.builder().baseUrl(baseUrl).build();

        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build()
                .createClient(clientType);
    }
}
