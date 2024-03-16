package com.example.blogging.controllers;

import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import com.example.blogging.service.BlogPostService;
import com.example.blogging.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "201", description = "Created")
})
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    BlogPostService blogPostService;

    @Autowired
    BlogPostController blogPostController;
    @Operation(
            description = "Posting of comments",
            summary = "Posting of a comment on a blog post"

    )
    @PostMapping("/comment/{id}")
    public CommentResponse saveComment(@RequestBody Comment comment, @PathVariable("id") Long id, HttpServletRequest request) {
        String username = blogPostController.getUsernameFromHeader(request);
        return commentService.createComment(comment, id, username);
    }
    @Operation(
            description = "Getting of comments",
            summary = "Getting of all the comments"

    )
    @GetMapping("/comment")
    public ResponseEntity<List<Comment>> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        Page<Comment> commentPage = commentService.getAllComments(pageable);
        return ResponseEntity.ok(commentPage.getContent());
    }
    @Operation(
            description = "Getting of comments",
            summary = "Getting of comments by id"

    )
    @GetMapping("/comment/{id}")
    public Optional<Comment> getComment(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }
    @Operation(
            description = "Updating of comments",
            summary = "Updating of comments by id"

    )
    @PutMapping("/comment/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment updatedComment, HttpServletRequest request) {
        String username = blogPostController.getUsernameFromHeader(request);
        return  commentService.updateComment(updatedComment, username);
    }
    @Operation(
            description = "Deleting of comments",
            summary = "Deleting of comments by id"

    )
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        String username = blogPostController.getUsernameFromHeader(request);
        commentService.deleteCommentById(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
