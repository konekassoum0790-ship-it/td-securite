package com.example.banque;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthentificationTest {

    @Autowired
    MockMvc mvc;

    @Test
    void sansIdentifiants_401() throws Exception {
        mvc.perform(get("/api/me"))
           .andExpect(status().isUnauthorized());
    }

    @Test
    void mauvaisMotDePasse_401() throws Exception {
        mvc.perform(get("/api/me").with(httpBasic("alice", "faux")))
           .andExpect(status().isUnauthorized());
    }

    @Test
    void bonsIdentifiants_renvoieLUtilisateurConnecte() throws Exception {
        mvc.perform(get("/api/me").with(httpBasic("bruno", "bruno123")))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.login").value("bruno"))
           .andExpect(jsonPath("$.roles[0]").value("CONSEILLER"));
    }
}