package com.example.gradleAdminApi.controller.api;

import com.example.gradleAdminApi.controller.CUDController;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.service.CommentApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.gradleAdminApi.model.entity.Comment;
import com.example.gradleAdminApi.model.network.request.CommentApiRequest;
import com.example.gradleAdminApi.model.network.response.CommentApiResponse;

import java.util.List;

@RestController
@RequestMapping(value="/api/comment")
public class CommentApiController extends CUDController<CommentApiRequest, CommentApiResponse, Comment> {

    @Autowired
    private CommentApiLogicService commentApiLogicService;

    @GetMapping("/goods/{id}")
    public Header<List<CommentApiResponse>> readWithGoodsId(@PathVariable Long id) throws Exception {
        return commentApiLogicService.readWithGoodsId(id);
    }

    @GetMapping("/user/{id}")
    public Header<List<CommentApiResponse>> readWithUserId(@PathVariable Long id, Authentication authentication) throws Exception {
        return commentApiLogicService.readWithUserId(id, authentication);
    }
}
