package com.example.gradleAdminApi.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserApiResponse {
	
	private Long id;
	
	private String userEmail;
	
	private String userName;
	
	private String userSurname;
	
	private String birthday;
	
	private String postCode;
	
	private String userAddr1;
	
	private String userAddr2;
	
	private String userAddr3;
	
	private String phoneNum;

	private int access;

	private String strAccess;

	private String lastLoginAt;
	
	private String regDate;
	
	private String upDate;
}
