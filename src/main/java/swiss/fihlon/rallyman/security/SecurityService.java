package swiss.fihlon.rallyman.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swiss.fihlon.rallyman.data.entity.Role;
import swiss.fihlon.rallyman.service.DatabaseService;

import java.util.Set;

@Service
public final class SecurityService implements UserDetailsService {

    private final @NotNull DatabaseService databaseService;

    public SecurityService(@NotNull final DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public @NotNull UserDetails loadUserByUsername(@NotNull final String username) throws UsernameNotFoundException {
        return databaseService.getUserByEmail(username)
                .map(userData -> new User(userData.email(), userData.passwordHash(),
                        Set.of(new SimpleGrantedAuthority(Role.USER.name()))))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

}
