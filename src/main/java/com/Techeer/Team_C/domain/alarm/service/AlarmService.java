package com.Techeer.Team_C.domain.alarm.service;

import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.domain.user.entity.User;
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
    private final ProductRegisterMysqlRepository productRegisterMysqlRepository;

    @Async
    public String sendMail(Product product, User user) throws MessagingException { // id = productId
        Context context = new Context();
        context.setVariable("product_name", product.getName());
        context.setVariable("product_image", product.getImage());
        context.setVariable("price_link", product.getUrl());
        context.setVariable("desire_price", productRegisterMysqlRepository.findByUserAndProduct(user, product).get().getDesiredPrice());
        context.setVariable("price", product.getMinimumPrice());
        // thymeleaf 변수 설정

        String process = templateEngine.process("priceAlarm", context);
        //thymeleaf 변수 template에 적용

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setSubject("상품 가격 하락 알림");
        messageHelper.setText(process, true);
        messageHelper.setTo(user.getEmail());
        javaMailSender.send(message);
        return "Sent";
    }
}
