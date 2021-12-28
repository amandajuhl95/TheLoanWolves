package dk.lw.loanwolvesservice.infrastructure;

import dk.lw.loanwolvesservice.AppSettings;
import dk.lw.loanwolvesservice.DTO.account.AccountDTO;
import dk.lw.loanwolvesservice.DTO.account.TransactionDTO;
import dk.lw.loanwolvesservice.domain.AccountType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountClient extends RestClient {

    public AccountClient() {
        this.baseUrl = AppSettings.accountServiceURL;
    }

    public AccountDTO createAccount(AccountType type, UUID userId) throws IOException {
        String url = "new/"+ type + "/" + userId;
        String response = POST(url, "").body().string();
        AccountDTO accountDTO = gson.fromJson(response, AccountDTO.class);
        return accountDTO;
    }

    public AccountDTO getAccount(UUID userId, UUID accountId) throws IOException {
        String url = userId + "/" + accountId;
        String response = GET(url).body().string();
        AccountDTO accountDTO = gson.fromJson(response, AccountDTO.class);
        return accountDTO;
    }

    public List<AccountDTO> getAccounts(UUID userId) throws IOException {
        String response = GET(userId.toString()).body().string();
        List<AccountDTO> accountDTOs = gson.fromJson(response, ArrayList.class);
        return accountDTOs;
    }

    public AccountDTO transaction(UUID userId, UUID accountId, TransactionDTO transaction) throws IOException {
        String url = userId + "/" + accountId;
        String response = POST(url, gson.toJson(transaction)).body().string();
        AccountDTO accountDTO = gson.fromJson(response, AccountDTO.class);
        return accountDTO;
    }
}
