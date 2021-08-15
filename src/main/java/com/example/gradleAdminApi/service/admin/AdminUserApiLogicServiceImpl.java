package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.entity.User;
import com.example.gradleAdminApi.model.enumclass.ErrorMessages;
import com.example.gradleAdminApi.model.enumclass.UserAccess;
import com.example.gradleAdminApi.model.enumclass.UserStatus;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.LoginApiRequest;
import com.example.gradleAdminApi.model.network.request.UserApiRequest;
import com.example.gradleAdminApi.model.network.response.UserApiResponse;
import com.example.gradleAdminApi.repository.UserRepository;
import com.example.gradleAdminApi.utils.DateFormat;
import com.example.gradleAdminApi.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AdminUserApiLogicServiceImpl implements AdminUserApiLogicService  {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DateFormat dateFormat;

    @Override
    public Header<List<UserApiResponse>> index(Authentication authentication, int searchKind, String keyword) throws Exception {
        log.info("get user list");

        jwtUtil.getAccessAllPermission(authentication);

        List<User> searchedUsers = new ArrayList<>();

        switch(searchKind) {
            case 0: searchedUsers = userRepository.findAll();
                break;
            case 1: searchedUsers = userRepository.findByUserEmailContaining(keyword);
                break;
            case 2: searchedUsers = userRepository.findByUserNameContaining(keyword);
                break;
            case 3: searchedUsers = userRepository.findByUserSurnameContaining(keyword);
                break;
            default:
                throw new NoSuchElementException();
        }

        List<UserApiResponse> userApiResponse = searchedUsers.stream()
                .map(this::response)
                .collect(Collectors.toList());

//        Page<User> allUsers = userRepository.findAll(pageable);
//        Pagination pagination = Pagination.builder()
//                .totalPages(allUsers.getTotalPages())
//                .totalElement(allUsers.getTotalElements())
//                .currentPage(allUsers.getNumber())
//                .currentElements(allUsers.getNumberOfElements())
//                .build();

        return Header.OK(userApiResponse);
    }

    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request, Authentication authentication) throws Exception{
        log.info("post user register");

        jwtUtil.getAccessAdminPermission(authentication);
        UserApiRequest userApiRequest = request.getData();

        String encodedPassword = passwordEncoder.encode(userApiRequest.getPasswd());

        User user = User.builder()
                .userEmail(userApiRequest.getUserEmail())
                .passwd(encodedPassword)
                .userName(userApiRequest.getUserName())
                .userSurname(userApiRequest.getUserSurname())
                .birthday(userApiRequest.getBirthday())
                .postCode(userApiRequest.getPostCode())
                .userAddr1(userApiRequest.getUserAddr1())
                .userAddr2(userApiRequest.getUserAddr2())
                .userAddr3(userApiRequest.getUserAddr3())
                .phoneNum(userApiRequest.getPhoneNum())
                .status(UserStatus.ACTIVATED)
                .access(getUserAccess(userApiRequest))
                .lastLoginAt(LocalDateTime.now())
                .upDate(LocalDateTime.now())
                .build();

        return Header.OK(response(userRepository.save(user)));
    }

    @Override
    public Header<UserApiResponse> read(Long id, Authentication authentication) throws Exception {
        log.info("get user detail");

        jwtUtil.getAccessAllPermission(authentication);
        return Header.OK(response(userRepository.findById(id).orElseThrow(NoSuchElementException::new)));
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request, Authentication authentication) throws Exception {
        log.info("put user modify");

        jwtUtil.getAccessAllPermission(authentication);
        UserApiRequest userApiRequest = request.getData();

        System.out.println(userApiRequest);

        User selectedUser = userRepository.findById(userApiRequest.getId()).orElseThrow(NoSuchElementException::new)
                .setUserName(userApiRequest.getUserName())
                .setUserSurname(userApiRequest.getUserSurname())
                .setBirthday(userApiRequest.getBirthday())
                .setPostCode(userApiRequest.getPostCode())
                .setUserAddr1(userApiRequest.getUserAddr1())
                .setUserAddr2(userApiRequest.getUserAddr2())
                .setUserAddr3(userApiRequest.getUserAddr3())
                .setPhoneNum(userApiRequest.getPhoneNum())
                .setAccess(getUserAccess(userApiRequest));

        if(userApiRequest.getPasswd() != null && passwordEncoder.matches(request.getData().getPasswd(), selectedUser.getPasswd())) {
            try {
                jwtUtil.getAccessAdminPermission(authentication);
            } catch (UnauthenticatedException e) {
                jwtUtil.getAuthPermission(request.getData().getId(), authentication);
            }
            String encodedPassword = passwordEncoder.encode(userApiRequest.getNewPasswd());

            selectedUser.setPasswd(encodedPassword);
        }

        return Header.OK(response(userRepository.save(selectedUser)));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Header delete(Long id, Authentication authentication) throws Exception {
        log.info("delete user");

        jwtUtil.getAccessAdminPermission(authentication);
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userRepository.save(user.setStatus(UserStatus.DELETED));
        return Header.OK();
    }

    @Override
    public Header passwdCheck(Header<LoginApiRequest> request, Authentication authentication) throws Exception {
        log.info("password check");

        Map<String, Object> claimsData = jwtUtil.getClaimsData(authentication);
        User user = userRepository.findById(request.getData().getId()).orElseThrow(NoSuchElementException::new);

        if(user.getUserEmail().equals(claimsData.get("userEmail").toString()) || Objects.equals(9, claimsData.get("access"))) {
            if (passwordEncoder.matches(request.getData().getPasswd(), user.getPasswd())) {
                return Header.OK();
            } else {
                return Header.ERROR(ErrorMessages.WRONG_PASSWORD.getTitle());
            }
        } else {
            throw new UnauthenticatedException();
        }
    }

    private UserAccess getUserAccess(UserApiRequest userApiRequest) {
        return userApiRequest.getAccess() ==
                UserAccess.ADMINISTRATOR.getId() ? UserAccess.ADMINISTRATOR : userApiRequest.getAccess() == UserAccess.MANAGER.getId() ? UserAccess.MANAGER :
                UserAccess.MEMBER;
    }

    public UserApiResponse response(User user) {

        return UserApiResponse.builder()
                .id(user.getId())
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .userSurname(user.getUserSurname())
                .birthday(user.getBirthday())
                .postCode(user.getPostCode())
                .userAddr1(user.getUserAddr1())
                .userAddr2(user.getUserAddr2())
                .userAddr3(user.getUserAddr3())
                .phoneNum(user.getPhoneNum())
                .status(user.getStatus().getTitle())
                .access(user.getAccess().getId())
                .strAccess(user.getAccess().getTitle())
                .lastLoginAt(dateFormat.dateFormat(user.getLastLoginAt()))
                .regDate(dateFormat.dateFormat(user.getRegDate()))
                .upDate(dateFormat.dateFormat(user.getUpDate()))
                .build();
    }
}
