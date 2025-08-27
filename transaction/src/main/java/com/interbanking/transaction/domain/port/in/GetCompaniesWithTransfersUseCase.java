package com.interbanking.transaction.domain.port.in;

import com.interbanking.commons.models.entity.Empresa;
import java.util.List;

public interface GetCompaniesWithTransfersUseCase {
    List<CompanyWithTransfersResult> execute();

    class CompanyWithTransfersResult {
        private final Empresa company;
        private final int transferCount;

        public CompanyWithTransfersResult(Empresa company, int transferCount) {
            this.company = company;
            this.transferCount = transferCount;
        }

        public Empresa getCompany() {
            return company;
        }

        public int getTransferCount() {
            return transferCount;
        }
    }
}