package com.reason.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author impassive
 */
@Repository
public interface ImFriendRepository extends JpaRepository<ImFriend, Long> {


}
