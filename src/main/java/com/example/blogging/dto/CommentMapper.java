package com.example.blogging.dto;

import com.example.blogging.entity.Comment;
import org.springframework.stereotype.Component;

@Component // Mark it as a Spring component to be picked up by component scanning
public class CommentMapper {

    public static CommentResponse mapToResponseDto(Comment comment) {
        CommentResponse response = new CommentResponse();
        // Map properties from Comment entity to CommentResponse DTO
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        // Map other properties as needed
        return response;
    }

    public Comment mapToEntity(CommentResponse request) {
        Comment comment = new Comment();
        // Map properties from CommentRequest DTO to Comment entity
        comment.setContent(request.getContent());
        // Map other properties as needed
        return comment;
    }
}
