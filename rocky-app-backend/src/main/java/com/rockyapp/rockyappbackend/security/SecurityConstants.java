package com.rockyapp.rockyappbackend.security;

public class SecurityConstants {
    public static final String SECRET = "rrEmm07zF5SpvvzrO7eiPOSgxvqa4kJNIFDRkPQ50Q4GJo-NtOUifzp1EHTfIRoaExW2A94RBAfMgJsaMQEFERRIIFmLQkw7lRFXlr8gtGCtebDJ1A_fPHA4UTC_KHKsg_S4jwf3SwcBHZaNYErumQkc4rMZD3ovs-szzRN6G0c";
    public static final long EXPIRATION_TIME_ACCESS =  3_600_000;
    public static final long EXPIRATION_TIME_REFRESH = 7_200_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
