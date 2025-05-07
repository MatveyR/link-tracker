package backend.academy.scrapper.Configs;

import backend.academy.scrapper.Clients.BotClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class BotClientConfig {

    @Bean
    public BotClient botClient(@Value("${bot.api.base-url}") String baseUrl) {
        RestClient restClient = RestClient.builder().baseUrl(baseUrl).build();

        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build()
                .createClient(BotClient.class);
    }
}
