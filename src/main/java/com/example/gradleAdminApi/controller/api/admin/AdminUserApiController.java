package com.example.gradleAdminApi.controller.api.admin;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.LoginApiRequest;
import com.example.gradleAdminApi.model.network.request.UserApiRequest;
import com.example.gradleAdminApi.model.network.response.UserApiResponse;
import com.example.gradleAdminApi.service.admin.AdminUserApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value="/api/admin/user")
public class AdminUserApiController {

    @Autowired
    private AdminUserApiLogicService adminUserApiLogicService;

    @GetMapping("")
    public Header<List<UserApiResponse>> index(
            @RequestParam(value = "searchKind", required = false) int searchKind, @RequestParam(value = "keyword", required = false) String keyword, Authentication authentication) throws Exception {
        return adminUserApiLogicService.index(authentication, searchKind, keyword);
    }

    @PostMapping("")
    public Header<UserApiResponse> create(@RequestBody @Valid Header<UserApiRequest> request, Authentication authentication) throws Exception {
        return adminUserApiLogicService.create(request, authentication);
    }

    @GetMapping("/{id}")
    public Header<UserApiResponse> read(@PathVariable Long id, Authentication authentication) throws Exception {
        return adminUserApiLogicService.read(id, authentication);
    }

    @PutMapping("")
    public Header<UserApiResponse> update(@RequestBody Header<UserApiRequest> request, Authentication authentication) throws Exception {
        return adminUserApiLogicService.update(request, authentication);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Header delete(@PathVariable Long id, Authentication authentication) throws Exception {
        return adminUserApiLogicService.delete(id, authentication);
    }

    @PostMapping("/passwd-check")
    public Header passwdCheck(@RequestBody Header<LoginApiRequest> request, Authentication authentication) throws Exception {
        return adminUserApiLogicService.passwdCheck(request, authentication);
    }
}
