package com.alfred.api.security.controller;

import com.alfred.api.app.model.Profile;
import com.alfred.api.security.model.Login;
import com.alfred.api.security.model.SpringSecurityUser;
import com.alfred.api.util.constants.App;
import com.alfred.api.util.token.TokenUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/login")
public class LoginController {


    private AuthenticationManager authenticationManager;
    private TokenUtils tokenUtils;
    private UserDetailsService userDetailsService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager,
                           TokenUtils tokenUtils,
                           UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

   @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody Login login)
            throws AuthenticationException {

        Authentication authentication = getAuthenticate(login);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(login.makeLogin(tokenUtils));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> authenticationRequest(HttpServletRequest request) {
        String token = request.getHeader(App.TOKEN_HEADER);
        String username = this.tokenUtils.getUsernameFromToken(token);
        SpringSecurityUser user = (SpringSecurityUser) getUserDetails(username);


        if (checkIfCanTokenBeRefreshed(token, user))
        {

            return ResponseEntity.ok(getDocumentRefreshToken(token));
        }
        else
        {
            return ResponseEntity.badRequest().body(null);
        }


    }


    @RequestMapping(value = "/is-first", method = RequestMethod.GET)
    public ResponseEntity<?> isFirst() {
        Boolean status = new Profile().findAll().size() == 0;
        return ResponseEntity.ok().body(status);

    }

    private Boolean checkIfCanTokenBeRefreshed(String token, SpringSecurityUser user) {
        return this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset());
    }

    private UserDetails getUserDetails(String userNameSpring) {
        return this.userDetailsService.loadUserByUsername(userNameSpring);
    }

    private Authentication getAuthenticate(Login login) {
        System.out.println(login.getUserNameSpring());
        return this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUserNameSpring(), login.getPassword()));
    }

    private Document getDocumentRefreshToken(String token) {
        Document refreshedToken = new Document();
        refreshedToken.put("token", this.tokenUtils.refreshToken(token));
        return refreshedToken;
    }

}
