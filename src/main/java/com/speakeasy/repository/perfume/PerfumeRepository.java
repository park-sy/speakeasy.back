package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

//JPARepository를 통해 DB에 접근, 엔티티와 PK를 값으로 가짐
public interface PerfumeRepository extends JpaRepository<Perfume, Long>,PerfumeRepositoryCustom {


}
