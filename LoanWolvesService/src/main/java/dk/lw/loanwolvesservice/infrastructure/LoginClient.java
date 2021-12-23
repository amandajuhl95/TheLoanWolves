package dk.lw.loanwolvesservice.infrastructure;

import dk.lw.loanwolvesservice.DTO.LoginDTO;
import dk.lw.loanwolvesservice.DTO.UserDTO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import stubs.user.*;

import java.util.UUID;

@Service
public class LoginClient {

    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    private ManagedChannel channel;

    public LoginClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
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

    public UserDTO createUser(UserDTO userDTO) {
        Address address = Address.newBuilder()
                .setNumber(userDTO.getAddress().getNumber())
                .setStreet(userDTO.getAddress().getStreet())
                .setCity(userDTO.getAddress().getCity())
                .setZipcode(userDTO.getAddress().getZipcode()).build();

        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setCpr(userDTO.getCpr())
                .setPassword(userDTO.getPassword())
                .setFullName(userDTO.getFullName())
                .setEmail(userDTO.getEmail())
                .setPhoneNumber(userDTO.getPhoneNumber())
                .setSalary(userDTO.getSalary())
                .setType(Type.valueOf(userDTO.getType()))
                .setAddress(address).build();

        UserResponse response = userServiceBlockingStub.createUser(request);
        UserDTO user = new UserDTO(response);
        return user;
    }

    public UserDTO updateUser(UserDTO userDTO) {
        Address address = Address.newBuilder()
                .setNumber(userDTO.getAddress().getNumber())
                .setStreet(userDTO.getAddress().getStreet())
                .setCity(userDTO.getAddress().getCity())
                .setZipcode(userDTO.getAddress().getZipcode()).build();

        UpdateUserRequest request = UpdateUserRequest.newBuilder()
                .setCpr(userDTO.getCpr())
                .setOldPassword(userDTO.getPassword())
                .setNewPassword(userDTO.getNewPassword())
                .setFullName(userDTO.getFullName())
                .setEmail(userDTO.getEmail())
                .setPhoneNumber(userDTO.getPhoneNumber())
                .setSalary(userDTO.getSalary())
                .setAddress(address).build();

        UserResponse response = userServiceBlockingStub.updateUser(request);
        UserDTO user = new UserDTO(response);
        return user;
    }


}
