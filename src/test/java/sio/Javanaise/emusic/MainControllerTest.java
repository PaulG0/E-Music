package sio.Javanaise.emusic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import sio.javanaise.emusic.configs.WebSecurityConfig;
import sio.javanaise.emusic.models.User;

@WebMvcTest(MainControllerTest.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, EMusicApplicationTests.class})
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void newActionTest() throws Exception {

        User user = new User();

    }

}
