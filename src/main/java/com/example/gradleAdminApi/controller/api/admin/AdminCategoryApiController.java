package com.example.gradleAdminApi.controller.api.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gradleAdminApi.controller.AdminICRUDController;
import com.example.gradleAdminApi.model.entity.Category;
import com.example.gradleAdminApi.model.network.request.CategoryApiRequest;
import com.example.gradleAdminApi.model.network.response.CategoryApiResponse;

@RestController
@RequestMapping(value="/api/admin/category")
public class AdminCategoryApiController extends AdminICRUDController<CategoryApiRequest, CategoryApiResponse, Category>{
}
