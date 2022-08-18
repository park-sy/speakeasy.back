package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.request.perfume.PerfumeSearch;

import java.util.List;

public interface PerfumeRepositoryCustom {

    List<Perfume> getList(PerfumeSearch perfumeSearch);

    void updateView(Long perfumeId);


}
