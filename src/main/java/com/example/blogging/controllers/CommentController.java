package com.example.blogging.controllers;

import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import com.example.blogging.service.BlogPostService;
import com.example.blogging.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    BlogPostService blogPostService;

    @Autowired
    BlogPostController blogPostController;
    @Operation(
            description = "Posting comments",
            summary = "Posting of comments"

    )
    @PostMapping("/comment/{id}")
    public CommentResponse saveComment(@RequestBody CommentResponse comment, @PathVariable("id") Long id, HttpServletRequest request) {
        String username = blogPostController.getUsernameFromHeader(request);
        return commentService.createComment(comment, id, username);
    }
    @Operation(
            description = "Getting all comments",
            summary = "Getting of comments with pagination"

    )

    @GetMapping("/comment")
    public ResponseEntity<Page<CommentResponse>> getAllComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        Page<CommentResponse> commentPage = commentService.getAllComments(pageable);
        return ResponseEntity.ok(commentPage);
    }

    @Operation(
            description = "Getting a comment",
            summary = "Getting a comment by id"

    )
    @GetMapping("/comment/{id}")
    public ResponseEntity<Optional<CommentResponse>>getComment(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);
    }
    @Operation(
            description = "Editing a comment",
            summary = "Editing a comment"

    )
    @PutMapping("/comment/{id}")
    public CommentResponse updateComment(@PathVariable Long id, @RequestBody Comment updatedComment, HttpServletRequest request) {
        String username = blogPostController.getUsernameFromHeader(request);
        return  commentService.updateComment(id, updatedComment, username);
    }
    @Operation(
            description = "Deleting a comment",
            summary = "Deleting of comments"

    )
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        String username = blogPostController.getUsernameFromHeader(request);
        commentService.deleteCommentById(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(
            description = "Getting of all comments",
            summary = "Getting of comments"

    )
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments(){
        return ResponseEntity.ok(commentService.getComments());
    }
}