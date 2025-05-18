package backend.academy.scrapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.academy.scrapper.Data.DTO.Requests.AddLinkRequest;
import backend.academy.scrapper.Data.DTO.Responses.LinkResponse;
import backend.academy.scrapper.Services.LinkService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(LinkService.class)
public class LinkControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LinkService linkService;

    @Test
    public void addLink_ValidRequest_ReturnsCreated() throws Exception {
        // Given
        AddLinkRequest request = new AddLinkRequest("https://github.com/example", List.of("tag1"), List.of("filter1"));
        LinkResponse response = new LinkResponse(1L, request.link(), request.tags(), request.filters());

        when(linkService.addLink(anyLong(), any())).thenReturn(response);

        // When & Then
        mockMvc.perform(
                        post("/links")
                                .header("Tg-Chat-Id", "693227891")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                    {
                        "link": "https://github.com/example",
                        "tags": ["tag1"],
                        "filters": ["filter1"]
                    }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(request.link()))
                .andExpect(jsonPath("$.tags[0]").value("tag1"));
    }
}
