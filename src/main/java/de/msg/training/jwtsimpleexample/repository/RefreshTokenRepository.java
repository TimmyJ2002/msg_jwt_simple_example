package de.msg.training.jwtsimpleexample.repository;

import de.msg.training.jwtsimpleexample.model.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    @Query("delete from RefreshToken where user.id = :id")
    @Modifying
    public void deleteRefreshTokenFromUser(Long id);
}
