package com.alfred.api.security.service;



import com.alfred.api.app.dao.ProfileRepository;
import com.alfred.api.app.model.Profile;
import com.alfred.api.security.model.SpringSecurityUser;
import com.alfred.api.useful.mongo.MongoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
    private boolean isValidRequest;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        isValidRequest = email!=null && !email.isEmpty();
        if (isValidRequest)
        {
            SpringSecurityUser springSecurityUser = getSpringSecurityUser(email);

            if (springSecurityUser != null)
            {
                return springSecurityUser;
            }
            throw new UsernameNotFoundException(String.format("No appUser found with email '%s'.", email));
        }
        else
        {
            throw new UsernameNotFoundException(String.format("No appUser found with email '%s'.", email));
        }
    }


    private static ProfileRepository profileRepository = new ProfileRepository(Profile.COLLECTION, Profile.class);
    private SpringSecurityUser getSpringSecurityUser(String email) {

        Profile profile = profileRepository.findByEmail(email);
        SpringSecurityUser springSecurityUser = null;
        if (profile != null && profile.status)
            {

                springSecurityUser = new SpringSecurityUser(MongoHelper.treatsId(profile._id),
                                                            profile.email,
                                                            profile.BCryptEncoderPassword(),
                                                            profile.name,
                                                            null,
                                                            AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));

            }



        return springSecurityUser;
    }
}
