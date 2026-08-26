package org.spring.createa.demoproject.controller;

import jakarta.validation.Valid;
import org.spring.createa.demoproject.domain.Comment;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.dto.UserPrincipal;
import org.spring.createa.demoproject.dto.request.DeleteCommentRequestBody;
import org.spring.createa.demoproject.dto.request.PatchCommentRequestBody;
import org.spring.createa.demoproject.dto.request.PostCommentRequestBody;
import org.spring.createa.demoproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController {

  public static final String COMPONENT_BOX_COMMENT_BOX = "component/box :: commentBox";
  CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @ModelAttribute
  User populateUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    return userPrincipal.getUser();
  }

  @PostMapping("/comments")
  String postComment(@ModelAttribute @Valid PostCommentRequestBody body,
      @ModelAttribute User user) {
    commentService.addComment(body.isbn13(), body.comment(), body.score(), user);
    return "redirect:/books/" + body.isbn13();
  }

  @PostMapping("/comments/{id}/likeOrUnlike")
  String likeOrUnlikeComment(@PathVariable int id, @ModelAttribute User user, Model model) {
    commentService.likeOrUnlikeComment(id, user);
    model.addAttribute("comment", commentService.findCommentById(id));
    return COMPONENT_BOX_COMMENT_BOX;
  }

  @ResponseBody
  @DeleteMapping("/comments")
  void deleteComment(@RequestBody DeleteCommentRequestBody body,
      @ModelAttribute User user) {
    commentService.deleteCommentById(body.id(), user);
  }

  @PatchMapping("/comments")
  String patchComment(PatchCommentRequestBody body,
      @ModelAttribute User user, Model model) {
    Comment comment = commentService.patchComment(body.id(), body.content(), body.score(), user);
    model.addAttribute("comment", comment);
    return "redirect:/books/" + comment.getIsbn13();
  }
}
