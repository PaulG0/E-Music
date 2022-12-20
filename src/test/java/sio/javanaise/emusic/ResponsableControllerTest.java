package sio.javanaise.emusic;

import io.github.jeemv.springboot.vuejs.VueJS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ModelAttribute;
import sio.javanaise.emusic.EMusicApplication;
import sio.javanaise.emusic.configs.WebSecurityConfig;
import sio.javanaise.emusic.controllers.ResponsableController;
import sio.javanaise.emusic.repositories.IEleveRepository;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.repositories.IResponsableDAO;
import sio.javanaise.emusic.repositories.IUserDAO;
import sio.javanaise.emusic.services.ResponsableService;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UIResponsableService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ResponsableController.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, EMusicApplication.class})
public class ResponsableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VueJS vue;

    @Autowired
    Environment environment;
    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }

    @MockBean
    private IUserDAO userrepo;

    @MockBean
    private IResponsableDAO responsablerepo;

    @MockBean
    private IEleveRepository eleverepo;

    @MockBean
    private IProfRepository profRepository;

    @MockBean
    private UIResponsableService responsableService;

    @MockBean
    private ResponsableService rService;

    @MockBean
    private UserDetailsService uService;

    @MockBean
    private TokenGenerator tokgen;

    @Test
    public void indexActionTest() throws Exception { // ne marche pas

        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/responsables/index"));

    }

}
