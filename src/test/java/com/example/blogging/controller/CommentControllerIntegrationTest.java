package com.example.blogging.controller;

import com.example.blogging.controllers.BlogPostController;
import com.example.blogging.controllers.CommentController;
import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import com.example.blogging.service.CommentService;
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

@WebMvcTest(controllers = CommentController.class) // Focus on web layer
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BlogPostController blogPostController; // We need to mock BlogPostController

    @Test
    public void testSaveComment() throws Exception {
        Comment comment = new Comment();
        // Set up your comment object

        CommentResponse response = new CommentResponse();
        // Set up your response object

        when(blogPostController.getUsernameFromHeader(any())).thenReturn("username"); // Mock getUsernameFromHeader
        when(commentService.createComment(any(Comment.class), anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.yourField").value("expectedValue"));
    }

    // Add more test methods for other controller endpoints

    // Utility method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

