package com.example.blogging.controller.integrationtest;

import com.example.blogging.controllers.AuthenticationController;
import com.example.blogging.controllers.BlogPostController;
import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.BlogPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BlogPostController.class)
// Remove unnecessary ExtendWith annotation
public class BlogPostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogPostService blogPostService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testSaveBlog() throws Exception {
        BlogPost blogPost = new BlogPost();
        BlogPostResponse response = new BlogPostResponse();
        when(blogPostService.createBlogPost(any(BlogPost.class), anyString())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(blogPost)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.yourField").value("expectedValue"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
