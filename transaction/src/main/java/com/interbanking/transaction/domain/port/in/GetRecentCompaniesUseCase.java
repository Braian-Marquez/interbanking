package com.interbanking.transaction.domain.port.in;

import com.interbanking.commons.models.entity.Empresa;
import java.util.List;

public interface GetRecentCompaniesUseCase {
    List<Empresa> execute();
}