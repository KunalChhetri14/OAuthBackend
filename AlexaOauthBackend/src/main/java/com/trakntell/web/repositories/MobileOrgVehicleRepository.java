package com.trakntell.web.repositories;

import com.trakntell.web.models.MobileOrgVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MobileOrgVehicleRepository extends JpaRepository<MobileOrgVehicle, String> {
    Optional<MobileOrgVehicle> findByMobileNum(String mobile);


    /*@Modifying
    @Query("update MobileOrgVehicle set subscription_status = 0, modified= :modified where mobile = :user")
    int markSubscriptionInactive(@Param("user") String user, @Param("modified") String modified);

    @Modifying
    @Query("update MobileOrgVehicle set subscription_status = 1, modified= :modified where mobile = :user")
    int markSubscriptionActive(@Param("user") String user, @Param("modified") String modified);*/

    @Transactional
    @Modifying
    @Query("update MobileOrgVehicle set subscription_status = :sub_status, modified= :modified where mobile = :user")
    int updateSubscriptionStatus(@Param("user") String user, @Param("sub_status") int sub_status, @Param("modified") String modified);

    @Modifying
    @Query("update MobileOrgVehicle set subscription_status = :sub_status, subscription_end_date= :sub_end_date, modified= :modified where mobile = :user")
    int copySubStatusAndDateToDB(@Param("user") String user, @Param("sub_status") int sub_status, @Param("sub_end_date") LocalDateTime sub_end_date, @Param("modified") String modified);

    @Query("select m from MobileOrgVehicle m where m.subscription_end_date < ?1 OR subscription_end_date is NULL")
    List<MobileOrgVehicle> findBySubscription_end_dateLessThan(LocalDateTime subscription_end_date);


}
