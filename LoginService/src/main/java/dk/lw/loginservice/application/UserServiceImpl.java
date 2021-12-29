package dk.lw.loginservice.application;


import dk.lw.loginservice.AppSettings;
import dk.lw.loginservice.infrastructure.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import stubs.user.*;
import dk.lw.loginservice.domain.User;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Logger producer;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {
            User user = userRepository.findOneByCpr(request.getCpr());

            if (user != null) {
                if (user.validPassword(request.getPassword())) {
                    String token = user.generateToken();
                    LoginResponse response = LoginResponse.newBuilder().setToken(token).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
                String error = "Wrong CPR or password";

                Status status = Status.INVALID_ARGUMENT.withDescription(error);
                producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

                responseObserver.onError(status.asRuntimeException());
            }
            String error = "User with CPR: " + request.getCpr() + " not found";

            Status status = Status.NOT_FOUND.withDescription(error);
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());

        } catch (Exception ex) {

            String error = ex.getMessage();

            Status status = Status.INTERNAL.withDescription(error);
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            User user = userRepository.findOneByCpr(request.getCpr());
            if (user != null) {
                UserResponse response = createResponse(user, responseObserver);
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            String error = "User with CPR: " + request.getCpr() + " not found";

            Status status = Status.NOT_FOUND.withDescription(error);
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());

        } catch (Exception ex) {
            String error = ex.getMessage();

            Status status = Status.INTERNAL.withDescription(error);
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void getUserById(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            Optional<User> optUser = userRepository.findById(UUID.fromString(request.getId()));

            if(optUser.isPresent()) {
                User user = optUser.get();
                UserResponse response = createResponse(user, responseObserver);
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            String error = "User with id: " + request.getId() + " not found";

            Status status = Status.NOT_FOUND.withDescription(error);
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());

        } catch (Exception ex) {
            String error = ex.getMessage();

            Status status = Status.INTERNAL.withDescription(error);
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            User user = userRepository.findOneByCpr(request.getCpr());

            if (user != null) {
                boolean valid = user.validPassword(request.getOldPassword());
                if(valid) {
                    user.setFullName(request.getFullName());
                    user.setEmail(request.getEmail());
                    user.setPhoneNumber(request.getPhoneNumber());
                    user.setPassword(request.getNewPassword());
                    user.setSalary(request.getSalary());
                    user.getAddress().setStreet(request.getAddress().getStreet());
                    user.getAddress().setNumber(request.getAddress().getNumber());
                    user.getAddress().setCity(request.getAddress().getCity());
                    user.getAddress().setZipcode(request.getAddress().getZipcode());
                    user = userRepository.save(user);

                    UserResponse response = createResponse(user, responseObserver);
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
                String error = "Invalid password or CPR";

                Status status = Status.INVALID_ARGUMENT.withDescription(error);
                producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

                responseObserver.onError(status.asRuntimeException());
            }
            String error = "User with CPR: " + request.getCpr() + " not found";

            Status status = Status.NOT_FOUND.withDescription(error);
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());

        } catch (Exception ex) {
            String error = ex.getMessage();

            Status status = Status.INTERNAL.withDescription(error);
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            User user = new User(request);
            user = userRepository.save(user);

            UserResponse response = createResponse(user,responseObserver);
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            String error = ex.getMessage();

            Status status = Status.INTERNAL.withDescription(ex.getMessage());
            producer.sendLogs(AppSettings.serviceName, error, status.getCode().value());

            responseObserver.onError(status.asRuntimeException());
        }
    }

    private UserResponse createResponse(User user, StreamObserver<UserResponse> responseObserver) throws ParseException {
        Address address = Address.newBuilder()
                .setNumber(user.getAddress().getNumber())
                .setStreet(user.getAddress().getStreet())
                .setCity(user.getAddress().getCity())
                .setZipcode(user.getAddress().getZipcode()).build();

        UserResponse response = UserResponse.newBuilder()
                    .setId(user.getId().toString())
                    .setType(stubs.user.Type.valueOf(user.getType().toString()))
                    .setCpr(user.getCpr())
                    .setEmail(user.getEmail())
                    .setFullName(user.getFullName())
                    .setPhoneNumber(user.getPhoneNumber())
                    .setSalary(user.getSalary())
                    .setAge(user.getAge())
                    .setAddress(address).build();

        return response;
    }
}
