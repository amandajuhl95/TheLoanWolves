package dk.lw.loanservice.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AmortizationDTO {

    private UUID loanId, transactionId;
    private double amount;

    public AmortizationDTO(TransactionDTO transactionDTO, UUID loanId) {
        this.loanId = loanId;
        this.transactionId = transactionDTO.getTransactionId();
        this.amount = transactionDTO.getAmount();
    }


}
