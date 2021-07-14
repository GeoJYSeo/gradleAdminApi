package com.example.gradleAdminApi.controller.api.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gradleAdminApi.controller.AdminICRUDController;
import com.example.gradleAdminApi.model.entity.Comment;
import com.example.gradleAdminApi.model.network.request.CommentApiRequest;
import com.example.gradleAdminApi.model.network.response.CommentApiResponse;

@RestController
@RequestMapping(value="/api/admin/comment")
public class AdminCommentApiController extends AdminICRUDController<CommentApiRequest, CommentApiResponse, Comment>{
}
