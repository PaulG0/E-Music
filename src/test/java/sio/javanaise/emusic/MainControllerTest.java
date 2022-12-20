package sio.javanaise.emusic;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.jeemv.springboot.vuejs.VueJS;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ModelAttribute;
import sio.javanaise.emusic.EMusicApplication;
import sio.javanaise.emusic.configs.WebSecurityConfig;
import sio.javanaise.emusic.controllers.MainController;
import sio.javanaise.emusic.repositories.IEleveDAO;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.repositories.IResponsableDAO;
import sio.javanaise.emusic.repositories.IUserDAO;
import sio.javanaise.emusic.services.ResponsableService;
import sio.javanaise.emusic.services.TokenGenerator;

@WebMvcTest(MainController.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, EMusicApplication.class})
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    Environment environment;

    @MockBean
    private IResponsableDAO parentrepo;

    @MockBean
    private IUserDAO userrepo;

    @MockBean
    private IEleveDAO enfantrepo;

    @MockBean
    private UserDetailsService uService;

    @MockBean
    private ResponsableService rService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private VueJS vue;

    @MockBean
    private TokenGenerator tokgen;

    @MockBean
    private IProfRepository profRepository;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }

    @Test
    public void indexActionTest() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

    }

    @Test
    public void failureActionTest() throws Exception { // ne marche pas

        this.mockMvc
                .perform(get("/failure"))
                .andExpect(status().isOk());

    }

    @Test
    void newActionTest() throws Exception { // problème sur la vérification du nom

        String login = "Teste-Testnew";
        String civilite = "Mr";
        String nom = "Fauvel";
        String prenom = "Teste";
        String adresse = "12 rue Fermat";
        String ville = "Caen";
        String quotient_familial = null;
        String code_postal = "14000";
        String email = "test@test.com";
        String password = "Azerty1234";
        String tel1 = "0674018434";
        String tel2 = "";
        String tel3 = "";
        this.mockMvc
                .perform(post("/new").contentType(MediaType.APPLICATION_JSON)
                    .param("login", login)
                    .param("civilite", civilite)
                    .param("nom", nom)
                    .param("prenom", prenom)
                    .param("adresse", adresse)
                    .param("ville", ville)
                    .param("quotient_familial", quotient_familial)
                    .param("code_postal", code_postal)
                    .param("email", email)
                    .param("password", password)
                    .param("tel1", tel1)
                    .param("tel2", tel2)
                    .param("tel3", tel3)
                    .with(csrf())
                )
                .andExpect(status().isOk());

    }

    @Test
    public void personnelleActionTest() throws Exception {

        this.mockMvc
                .perform(get("/personnel"))
                .andExpect(status().isOk())
                .andExpect(view().name("/main/personnel"));

    }

    @Test
    public void findActionTest() throws Exception {

        this.mockMvc
                .perform(get("/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("/main/find"));

    }

}
