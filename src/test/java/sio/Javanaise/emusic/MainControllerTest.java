package sio.Javanaise.emusic;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sio.javanaise.emusic.models.User;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void newActionTest() throws Exception {

        String login = "Test-Testnew";
        String civilite = "Mr";
        String nom = "Testnew";
        String prenom = "Test";
        String adresse = "12 rue Fermat";
        String ville = "Caen";
        Integer quotient_familial = 0;
        String code_postal = "14000";
        String email = "test@test.com";
        String password = "Azerty1234";
        String tel1 = "0674018434";
        String tel2 = "";
        String tel3 = "";
        this.mockMvc
                .perform(post("/e-music/new").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"login\":\"" + login + "\"," +
                            "\"civilite\":\"" + civilite + "\"," +
                            "\"nom\":\"" + nom + "\"," +
                            "\"prenom\":\"" + prenom + "\"," +
                            "\"adresse\":\"" + adresse + "\"," +
                            "\"ville\":\"" + ville + "\"," +
                            "\"quotient_familial\":\"" + quotient_familial + "\"," +
                            "\"code_postal\":\"" + code_postal + "\"," +
                            "\"email\":\"" + email + "\"," +
                            "\"password\":\"" + password + "\"," +
                            "\"tel1\":\"" + tel1 + "\"," +
                            "\"tel2\":\"" + tel2 + "\"," +
                            "\"tel3\":\"" + tel3 + "\"" +
                            "}"
                    ).with(csrf())
                );

    }

    @Test
    void LoginActionTest() throws Exception {

        String username = "Test-Testnew";
        String password = "Azerty1234";
        this.mockMvc
                .perform(post("/e-music/login").contentType(MediaType.APPLICATION_JSON)
                   .content("{\"username\":\"" + username + "\"," +
                           "\"password\":\"" + password + "\"" +
                           "}"
                   ).with(csrf())
                );

    }

    @Test void DecoActionTest() throws Exception {

        this.mockMvc
                .perform(get("/e-music/deco").contentType(MediaType.APPLICATION_JSON));

    }

}
