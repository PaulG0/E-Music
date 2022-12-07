package sio.Javanaise.emusic;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import sio.javanaise.emusic.configs.WebSecurityConfig;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UserService;

@WebMvcTest(MainControllerTest.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, EMusicApplicationTests.class})
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService uService;

    @MockBean
    private TokenGenerator tokgen;

    @Test
    void newActionTest() throws Exception {

        User user = ((UserService) uService).createUser("Test-Test", "Azerty1234");
        String token = tokgen.generateToken();
        user.setToken(token);
        user.setAuthorities("PARENT");
        this.mockMvc.perform(post("/e-music/new").contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"token\":\"" + token + "\",\"user\":\"" + user.getNickname() + "\"}"))
                );

    }

}
