package dk.lw.loanwolvesservice.DTO.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.lw.loanwolvesservice.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserDTO {
    private UUID id;
    @NotBlank(message = "CPR must be provided")
    @Pattern(message="CPR must be provided in pattern: xxxxxx-xxxx",
            regexp = "^\\d{6}-\\d{4}$", flags = Pattern.Flag.UNICODE_CASE)
    private String cpr;

    @NotBlank(message = "Old password must be provided")
    @Size(min = 4, message = "Old password must be at least 4 characters")
    private String oldPassword;

    @NotBlank(message = "New password must be provided")
    @Size(min = 4, message = "New password must be at least 4 characters")
    private String newPassword;

    @NotBlank(message = "Full name must be provided")
    @Pattern(message="CPR must be provided in pattern: xxxxxx-xxxx",
            regexp = "^[A-Z][a-z]{1,}(?: [A-Z][a-z]*){1,5}$", flags = Pattern.Flag.UNICODE_CASE)
    private String fullName;

    @NotBlank(message = "Email must be provided")
    @Pattern(message="Email is not valid",
            regexp = "^(?:[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String email;

    @NotBlank(message = "Phone number must be provided")
    @Size(min = 8, max = 8, message = "Phone number must be 8 digits long")
    @Pattern(message="Phone number is not valid", regexp = "^[0-9]{8}$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String phoneNumber;

    @DecimalMin(value = "0.00", message = "Salary should be minimum 0.00 kr.")
    private BigDecimal salary;

    @Valid
    private AddressDTO address;
}
