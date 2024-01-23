package com.dyma.tennis.web;

import com.dyma.tennis.data.PlayerList;
import com.dyma.tennis.service.PlayerNotFoundException;
import com.dyma.tennis.service.PlayerService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
    public void shouldListAllPlayers() throws Exception {
        // Given
        Mockito.when(playerService.getAllPlayers()).thenReturn(PlayerList.ALL);

        // When / Then
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Murray")))
                .andExpect(jsonPath("$[1].lastName", CoreMatchers.is("Djokovic")))
                .andExpect(jsonPath("$[2].lastName", CoreMatchers.is("Federer")))
                .andExpect(jsonPath("$[3].lastName", CoreMatchers.is("Nadal")));
    }

    @Test
    public void shouldRetrievePlayer() throws Exception {
        // Given
        Mockito.when(playerService.getByLastName("nadal")).thenReturn(PlayerList.RAFAEL_NADAL);

        // When / Then
        mockMvc.perform(get("/players/nadal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$.rank.position", CoreMatchers.is(1)));
    }

    @Test
    public void shouldReturn404NotFoundWhenPlayerDoesNotExist() throws Exception {
        // Given
        Mockito.when(playerService.getByLastName("xxx")).thenThrow(new PlayerNotFoundException("xxx does not exist"));

        // When / Then
        mockMvc.perform(get("/players/xxx"))
                .andExpect(status().isNotFound());
    }
}
