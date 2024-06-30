package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPfNoIgnoreCase(String pfNo);
    boolean existsByPfNoAndIdNot(String pfNo, long userId);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, long id);
    boolean existsByUsernameAndIdNot(String username, long id);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.role " +
            "LEFT JOIN FETCH u.branch " +
            "LEFT JOIN FETCH u.apps " +
            "WHERE LOWER(u.username) = LOWER(:username)")
    User findByUsernameIgnoreCase(String username);
    User findByEmail(String email);
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.id = :id")
    User findByIdWithRole(@Param("id") Long id);

    List<User> findByisDeletedFalse();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isDeleted = :status WHERE u.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isActive = :status WHERE u.id = :id")
    void markAsActive(@Param("id") long id, @Param("status") boolean status);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePasswordById(@Param("password") String password, @Param("id") long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isActive = :active, u.isDeleted = false, u.modifiedBy = :modifiedBy, u.modifiedOn = :modifiedOn WHERE u.id = :id")
    void updateIsActiveById(@Param("active") boolean active, @Param("modifiedBy") String modifiedBy, @Param("modifiedOn")String modifiedOn, @Param("id") long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isLoggedIn = :loggedIn, u.lastLoginOn = :lastLoginOn WHERE u.id = :id")
    void updateisLoggedInById(@Param("id") long id, @Param("loggedIn") boolean loggedIn, @Param("lastLoginOn")String lastLoginOn);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :#{#user.username}, u.isDeleted = :#{#user.isDeleted}," +
            "u.firstname = :#{#user.firstname}, u.lastname = :#{#user.lastname},u.gender = :#{#user.gender}," +
            "u.pfNo = :#{#user.pfNo},u.email = :#{#user.email}," +
            "u.isLoggedIn = :#{#user.isLoggedIn},u.isActive = :#{#user.isActive},u.isVerified = :#{#user.isVerified}, " +
            "u.verifiedBy = :#{#user.verifiedBy},u.createdBy = :#{#user.createdBy},u.createdOn = :#{#user.createdOn}, " +
            "u.modifiedOn = :#{#user.modifiedOn},u.modifiedBy = :#{#user.modifiedBy},u.role.id = :#{#roleId}," +
            "u.branch.id = :#{#branchId} WHERE u.id = :#{#user.id}")
    void update(@Param("user") User user, @Param("roleId") long roleId, @Param("branchId") long branchId);
}

