package com.Techeer.Team_C.domain.alarm.service;

import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class AlarmService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    private final ProductMysqlRepository productMysqlRepository;
    private final ProductRegisterMysqlRepository productRegisterMysqlRepository;
    private final UserRepository userRepository;

    public String sendMail(Long id, Long userId) throws MessagingException {
        Optional<Product> product = productMysqlRepository.findById(id);
        Optional<User> user = userRepository.findById(userId);

        Context context = new Context();
        context.setVariable("product_name", product.get().getName());
        context.setVariable("product_image", product.get().getImage());
        context.setVariable("price_link", product.get().getLink());
        context.setVariable("desire_price", productRegisterMysqlRepository.findByUserId(userId).getDesiredPrice());
        context.setVariable("price", product.get().getOriginPrice());
        // thymeleaf 변수 설정

        String process = templateEngine.process("priceAlarm", context);
        //thymeleaf 변수 template에 적용

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setSubject("상품 가격 하락 알림");
        messageHelper.setText(process, true);
        messageHelper.setTo(user.get().getEmail());
        javaMailSender.send(message);
        return "Sent";
    }
}
