package study.task3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.task3.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
