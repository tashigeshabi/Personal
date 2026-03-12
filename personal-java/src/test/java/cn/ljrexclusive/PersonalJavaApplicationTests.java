package cn.ljrexclusive;


import jakarta.annotation.Resource;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = PersonalJavaApplication.class)
public class PersonalJavaApplicationTests {


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void contextLoads() {
        String password = passwordEncoder.encode("123456");
        System.out.println(password);
    }

}
