package com.example.gradleAdminApi.model.network.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserApiRequest {

	private Long id;
	
	@NotBlank
	@Email
	private String userEmail;
	
	@NotBlank
	private String passwd;

	private String newPasswd;
	
	@NotBlank
	private String userName;
	
	@NotBlank
	private String userSurname;
	
	private String birthday;
	
	private String postCode;
	
	private String userAddr1;
	
	private String userAddr2;
	
	private String userAddr3;

	@NotBlank
	@Size(max=11, min=11)
	private String phoneNum;

	@NotNull
	private int access;
}
