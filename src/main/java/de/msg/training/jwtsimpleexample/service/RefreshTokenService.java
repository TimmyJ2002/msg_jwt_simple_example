package de.msg.training.jwtsimpleexample.service;


import de.msg.training.jwtsimpleexample.config.JwtUtils;
import de.msg.training.jwtsimpleexample.model.RefreshToken;
import de.msg.training.jwtsimpleexample.model.User;
import de.msg.training.jwtsimpleexample.repository.RefreshTokenRepository;
import de.msg.training.jwtsimpleexample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public void deleteRefreshTokenForUser(Long userId) {
        refreshTokenRepository.deleteRefreshTokenFromUser(userId);
    }


    public void createRefreshToken(String uuid, Long userId) {
        RefreshToken rt = new RefreshToken();
        rt.setRefreshToken(uuid);
        rt.setExpiryDate(Instant.now().plusSeconds(84000));
        rt.setUser(userRepository.findById(userId).get());
        refreshTokenRepository.save(rt);
    }


    public String exchangeRefreshToken(String refreshToken) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(refreshToken);
        if(!refreshTokenOptional.isPresent()) {
            throw new RuntimeException("Refresh token is not valid");
        }
        RefreshToken rt = refreshTokenOptional.get();
        if(rt.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(rt);
            throw new RuntimeException("Refresh token is expired");
        }
        return jwtUtils.generateJwtToken(userDetailsService.loadUserByUsername(rt.getUser().getUsername()));
    }

}
