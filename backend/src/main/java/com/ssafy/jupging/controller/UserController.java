package com.ssafy.jupging.controller;

import com.ssafy.jupging.domain.entity.Mission;
import com.ssafy.jupging.domain.entity.User;
import com.ssafy.jupging.dto.UserLoginRequestDto;
import com.ssafy.jupging.dto.UserResponseDto;
import com.ssafy.jupging.dto.UserSaveRequestDto;
import com.ssafy.jupging.dto.UserUpdateRequestDto;
import com.ssafy.jupging.service.JwtService;
import com.ssafy.jupging.service.MissionService;
import com.ssafy.jupging.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final JwtService jwtService;

    private final UserService userService;

    private final MissionService missionService;

    @ApiOperation(value = "로그인", notes = "로그인 성공 시 (token, userId) 반환 / 회원정보가 없을 경우 false 반환", response = ControllerResponse.class)
    @PostMapping("/login")
    public ControllerResponse login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        ControllerResponse response = null;
        try {
            User loginUser = userService.login(userLoginRequestDto);
            if (loginUser == null) {
                response = new ControllerResponse("success", false);
            } else {
                String token = jwtService.create(loginUser);
                UserResponseDto userResponseDto = new UserResponseDto();
                userResponseDto = userResponseDto.saveTokenUserId(token, loginUser.getUserId());
                response = new ControllerResponse("success", userResponseDto);
            }
        } catch (Exception e) {
            response = new ControllerResponse("fail", e.getMessage());
        }

        return response;
    }

    @ApiOperation(value = "이메일 중복체크", notes = "이메일 중복이면 false / 중복아니면 true 반환", response = ControllerResponse.class)
    @PostMapping("/emailck")
    public ControllerResponse checkEmail(@RequestParam String email) {
        ControllerResponse response = null;
        try {
            boolean ispresent = userService.checkEmail(email);
            response = new ControllerResponse("success", ispresent);
        } catch (Exception e) {
            response = new ControllerResponse("fail", e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복이면 false / 중복아니면 true 반환", response = ControllerResponse.class)
    @PostMapping("/nicknameck")
    public ControllerResponse checkNickname(@RequestParam String nickname) {
        ControllerResponse response = null;
        try {
            boolean ispresent = userService.checkNickname(nickname);
            response = new ControllerResponse("success", ispresent);
        } catch (Exception e) {
            response = new ControllerResponse("fail", e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "회원가입", notes = "회원가입 성공 시 true 반환 / 이메일or닉네임 중복 시 false 반환", response = ControllerResponse.class)
    @PostMapping("/join")
    public ControllerResponse saveUser(@RequestBody UserSaveRequestDto requestDto) {
        ControllerResponse response = null;

        try {
            if (userService.checkEmail(requestDto.getEmail())           //이메일,닉네임 중복이 없을 때
                    && userService.checkNickname(requestDto.getNickname())) {

                User user = new User();
                user = user.saveUser(requestDto);
                userService.save(user);

                //회원가입과 동시에 유저 mission 초기화
                Mission mission = new Mission();
                mission = mission.saveInit(user.getUserId());
                missionService.saveInit(mission);

                response = new ControllerResponse("success", true);

            } else {
                response = new ControllerResponse("success", false);
            }

        } catch (Exception e) {
            response = new ControllerResponse("fail", e.getMessage());
        }

        return response;
    }

    @ApiOperation(value = "유저 정보 찾기", notes = "성공 시 유저 데이터 반환 / 실패 시 에러메세지", response = ControllerResponse.class)
    @GetMapping("/{id}")
    public ControllerResponse findUser(@PathVariable("id") Long userId) {
        ControllerResponse response = null;

        try {
            User result = userService.findUser(userId);
            response = new ControllerResponse("success", result);
        } catch (Exception e) {
            response = new ControllerResponse("fail", e.getMessage());
        }

        return response;
    }

    @ApiOperation(value = "유저 정보 수정", notes = "수정 성공 시 '회원 수정 성공' 반환 / 실패 시 에러메세지", response = ControllerResponse.class)
    @PutMapping("/{id}")
    public ControllerResponse updateUser(@PathVariable("id") Long userId, @RequestBody UserUpdateRequestDto requestDto) {
        ControllerResponse response = null;

        try {
            userService.updateUser(userId, requestDto);
            response = new ControllerResponse("success", "회원 수정 성공");
        } catch (Exception e) {
            response = new ControllerResponse("fail", e.getMessage());
        }

        return response;
    }

    @ApiOperation(value = "회원 탈퇴", notes = "탈퇴 성공 시 '회원 탈퇴 성공' 반환 / 실패 시 에러메세지", response = ControllerResponse.class)
    @DeleteMapping("/{id}")
    public ControllerResponse deleteUser(@PathVariable("id") Long userId) {
        ControllerResponse response = null;

        try {
            userService.deleteUser(userId);
            response = new ControllerResponse("success", "회원 탈퇴 성공");
        } catch (Exception e) {
            response = new ControllerResponse("fail", e.getMessage());
        }

        return response;
    }

}
