package cmu.lee.twidder.test.controller;

import cmu.lee.twidder.entity.User;
import cmu.lee.twidder.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by lee on 4/14/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:testContext.xml"
})
@WebAppConfiguration
public class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserService testUserService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        Mockito.reset(testUserService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldGetAllUsersAsJSON() throws Exception {
        User user1 = new User(1, "user1");
        User user2 = new User(2, "user2");
        List<User> users = Arrays.asList(user1, user2);

        when(testUserService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/user/allUsers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType("application", "json")))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().string("[{\"name\":\"user1\",\"id\":1},{\"name\":\"user2\",\"id\":2}]"));

        verify(testUserService, times(1)).getAllUsers();
        verifyNoMoreInteractions(testUserService);
    }
}
