package com.amithangadi.docmind_AI.dto.response;

public class LoginResponse {

	private String accessToken;
	
	private String tokenType;
	
	public LoginResponse()
	{
		
	}
	
	public LoginResponse(String accessToken, String tokenType)
	{
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}
	
	public String getAccessToken()
	{
		return accessToken;
	}
	
	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}
	
	public void getTokenType(String tokenType)
	{
		this.tokenType = tokenType;
	}
	
	public void setTokenType(String tokenType)
	{
		this.tokenType = tokenType;
	}
}
