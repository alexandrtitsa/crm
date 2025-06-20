package ua.com.astone.acrm.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ua.com.astone.acrm.model.User;
import ua.com.astone.acrm.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return new UserDetailsImpl(user);
    }
}
