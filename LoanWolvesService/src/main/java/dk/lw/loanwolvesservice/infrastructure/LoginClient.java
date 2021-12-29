package dk.lw.loanwolvesservice.infrastructure;

import dk.lw.loanwolvesservice.AppSettings;
import dk.lw.loanwolvesservice.DTO.login.CreateUserDTO;
import dk.lw.loanwolvesservice.DTO.login.LoginDTO;
import dk.lw.loanwolvesservice.DTO.login.UpdateUserDTO;
import dk.lw.loanwolvesservice.DTO.login.UserDTO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import stubs.user.*;

import java.util.UUID;

 public class LoginClient {

    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public LoginClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(AppSettings.loginHost, AppSettings.loginPort)
                .usePlaintext().build();
        userServiceBlockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public String login(LoginDTO login){

        LoginRequest request = LoginRequest.newBuilder()
                .setCpr(login.getCpr())
                .setPassword(login.getPassword()).build();

        LoginResponse response = userServiceBlockingStub.login(request);
        return response.getToken();
    }

    public UserDTO getUserById(UUID id) {
        GetUserRequest request = GetUserRequest.newBuilder()
                .setId(id.toString()).build();

        UserResponse response = userServiceBlockingStub.getUserById(request);
        UserDTO user = new UserDTO(response);
        return user;
    }

    public UserDTO getUser(String cpr) {
        UserRequest request = UserRequest.newBuilder()
                .setCpr(cpr).build();

        UserResponse response = userServiceBlockingStub.getUser(request);
        UserDTO user = new UserDTO(response);
        return user;
    }

    public UserDTO createUser(CreateUserDTO newUser) {
        Address address = Address.newBuilder()
                .setNumber(newUser.getAddress().getNumber())
                .setStreet(newUser.getAddress().getStreet())
                .setCity(newUser.getAddress().getCity())
                .setZipcode(newUser.getAddress().getZipcode()).build();

        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setCpr(newUser.getCpr())
                .setPassword(newUser.getPassword())
                .setFullName(newUser.getFullName())
                .setEmail(newUser.getEmail())
                .setPhoneNumber(newUser.getPhoneNumber())
                .setSalary(newUser.getSalary().doubleValue())
                .setType(Type.valueOf(newUser.getType().toString()))
                .setAddress(address).build();

        UserResponse response = userServiceBlockingStub.createUser(request);
        UserDTO user = new UserDTO(response);
        return user;
    }

    public UserDTO updateUser(UpdateUserDTO userDTO) {
        Address address = Address.newBuilder()
                .setNumber(userDTO.getAddress().getNumber())
                .setStreet(userDTO.getAddress().getStreet())
                .setCity(userDTO.getAddress().getCity())
                .setZipcode(userDTO.getAddress().getZipcode()).build();

        UpdateUserRequest request = UpdateUserRequest.newBuilder()
                .setCpr(userDTO.getCpr())
                .setOldPassword(userDTO.getOldPassword())
                .setNewPassword(userDTO.getNewPassword())
                .setFullName(userDTO.getFullName())
                .setEmail(userDTO.getEmail())
                .setPhoneNumber(userDTO.getPhoneNumber())
                .setSalary(userDTO.getSalary().doubleValue())
                .setAddress(address).build();

        UserResponse response = userServiceBlockingStub.updateUser(request);
        UserDTO user = new UserDTO(response);
        return user;
    }


}

