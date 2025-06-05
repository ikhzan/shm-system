package com.ikhzan.shm;

import com.ikhzan.shm.web.WebController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(WebController.class)
public class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "isan", password = "bismillah", roles = "USER") // Simulate a logged-in user
    void testHomePage() throws Exception {
        mockMvc.perform(get("/")) // Simulates GET request to "/"
                .andExpect(status().isOk()) // Expect HTTP 200 response
                .andExpect(view().name("home")) // Assert the returned view name
                .andExpect(model().attribute("welcomeText", "Welcome to the system")) // Verify model attributes
                .andExpect(model().attribute("owner", "John Doe"))
                .andExpect(model().attribute("title", "Home Page"));
    }

}
