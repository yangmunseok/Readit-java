package org.spring.createa.demoproject.controller;

import org.spring.createa.demoproject.domain.Comment;
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

  CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping("/comments")
  String postComment(@ModelAttribute PostCommentRequestBody body,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    System.out.println("postComment invoked");
    commentService.addComment(body.isbn13(), body.comment(), body.score(), userPrincipal.getUser());
    return "redirect:/books/" + body.isbn13();
  }

  @PostMapping("/comments/{id}/likeOrUnlike")
  String likeOrUnlikeComment(@PathVariable int id,
      @AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
    commentService.likeOrUnlikeComment(id, userPrincipal.getUser());
    model.addAttribute("user", userPrincipal.getUser());
    model.addAttribute("comment", commentService.findCommentById(id));
    return "component/box :: commentBox";
  }

  @ResponseBody
  @DeleteMapping("/comments")
  void deleteComment(@RequestBody DeleteCommentRequestBody body,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    System.out.println("deleteComment() invoked");
    Comment exComment = commentService.findCommentById(body.id());
    commentService.deleteCommentById(exComment, userPrincipal.getUser());
  }

  @PatchMapping("/comments")
  String patchComment(PatchCommentRequestBody body,
      @AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
    System.out.println("patchComment invoked");
    Comment exComment = commentService.findCommentById(body.id());
    System.out.println(exComment);
    commentService.patchComment(exComment, body.content(), body.score(), userPrincipal.getUser());
    model.addAttribute("user", userPrincipal.getUser());
    model.addAttribute("comment", exComment);
    return "redirect:/books/" + exComment.getIsbn13();
  }
}
