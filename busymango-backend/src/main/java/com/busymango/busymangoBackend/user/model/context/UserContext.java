package com.busymango.busymangoBackend.user.model.context;

import com.busymango.busymangoBackend.user.model.dto.UserDTO;

public class UserContext {
    private static final ThreadLocal<UserDTO> userDTOThreadLocal = new ThreadLocal<>();

    public static void setUserDTO(UserDTO userDTO) {
        userDTOThreadLocal.set(userDTO);
    }

    public static UserDTO getUserDTO() {
        return userDTOThreadLocal.get();
    }

    public static void clear() {
        userDTOThreadLocal.remove();
    }
}
