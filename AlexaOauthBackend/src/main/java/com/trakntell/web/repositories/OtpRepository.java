package com.trakntell.web.repositories;

import com.trakntell.web.models.OtpModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<OtpModel, Integer> {

    OtpModel findTop1ByMobileAndExpireGreaterThanEqualAndNumSentLessThanAndUsedOrderByCreatedDesc(String mobile, String now, int numSent, int used);
}
