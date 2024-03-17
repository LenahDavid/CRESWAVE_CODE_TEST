package com.example.blogging.controller.integrationtest;

import com.example.blogging.controllers.BlogPostController;
import com.example.blogging.controllers.CommentController;
import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import com.example.blogging.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommentController.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BlogPostController blogPostController;

    @Test
    public void testSaveComment() throws Exception {
        Comment comment = new Comment();
        CommentResponse response = new CommentResponse();
        when(blogPostController.getUsernameFromHeader(any())).thenReturn("username"); // Mock getUsernameFromHeader
        when(commentService.createComment(any(Comment.class), any(Long.class), any(String.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
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
}

