package com.example.pafbackend.controllers;

import com.example.pafbackend.models.Post;
import com.example.pafbackend.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/posts") // Base URL for all endpoints in this controller
public class PostController {

    private final PostRepository postRepository;

    @Autowired // Inject the PostRepository dependency automatically
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

      // ------------------ GET all Posts ------------------


    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postRepository.findAll();// Fetch all posts from DB
        return new ResponseEntity<>(posts, HttpStatus.OK);// Return with status 200 OK
    }

     // ------------------ GET Posts by a Specific User ID ------------------

    @GetMapping("/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable String userId) {
        List<Post> posts = postRepository.findByUserId(userId); // Fetch posts created by specific user
        return new ResponseEntity<>(posts, HttpStatus.OK);// Return with status 200 OK
    }


      // ------------------ CREATE a New Post ------------------

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post savedPost = postRepository.save(post);// Save the new post to the DB
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId) {
        postRepository.deleteById(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable String postId, @RequestBody Post updatedPost) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            // Update fields if present in the request body
            if (updatedPost.getMediaLink() != null) {
                existingPost.setMediaLink(updatedPost.getMediaLink());
            }
            if (updatedPost.getMediaType() != null) {
                existingPost.setMediaType(updatedPost.getMediaType());
            }
            if (updatedPost.getContentDescription() != null) {
                existingPost.setContentDescription(updatedPost.getContentDescription());
            }
            // Save the updated post
            Post savedPost = postRepository.save(existingPost);
            return new ResponseEntity<>(savedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
