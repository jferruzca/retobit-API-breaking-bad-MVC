package dev.marshallBits.breakingBadApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marshallBits.breakingBadApi.dto.CreateCharacterDTO;
import dev.marshallBits.breakingBadApi.models.CharacterStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CharacterControllerTest {
    // MockMVC (Modelo Vista Controlador)
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("GET: Recibimos 10 characters en api/caracters")
    void getAllCharacters() throws Exception {
       mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    @DirtiesContext //Usamos nuevo contexto para no afectar otros test
    @DisplayName("POST: un nuevo character funciona correctamente en api/characters")
    void postNewCharacter() throws Exception{
        CreateCharacterDTO saul = CreateCharacterDTO
                .builder()
                .name("Saul Goodman")
                .occupation("Lawyer")
                .status(CharacterStatus.ALIVE)
                .build();

        mockMvc.perform(post("/api/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saul))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Saul Goodman"))
                .andExpect(jsonPath("$.occupation").value("Lawyer"));
    }

    @Test
    @DisplayName("GET: Obtener personaje por su ID")
    void getCharacterByID() throws Exception {
        mockMvc.perform(get("/api/characters/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Saul Goodman"))
                .andExpect(jsonPath("$.occupation").value("Abogado"))
                .andExpect(jsonPath("$.status").value("ALIVE"));
    }

    @Test
    @DisplayName("PATCH: Valida actualización estatus a DEAD para Skyler")
    void changeStatusToDead() throws Exception{
        mockMvc.perform(patch("/api/characters/3/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DEAD"));
    }

    @Test
    @DisplayName("PUT: Actualiza personaje por ID")
    void updateCharacterById() throws Exception {
        CreateCharacterDTO gus = CreateCharacterDTO
                .builder()
                .name("Gustavo")
                .occupation("Vendedor de Pollos")
                .status(CharacterStatus.DEAD)
                .build();

        mockMvc.perform(put("/api/characters/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gus))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gustavo"))
                .andExpect(jsonPath("$.occupation").value("Vendedor de Pollos"))
                .andExpect(jsonPath("$.status").value("DEAD"));
    }

}
