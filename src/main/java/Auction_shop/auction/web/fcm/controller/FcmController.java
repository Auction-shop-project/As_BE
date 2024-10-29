package Auction_shop.auction.web.fcm.controller;

import Auction_shop.auction.web.dto.fcm.FcmRequestDto;
import Auction_shop.auction.web.fcm.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/fcm")
    public ResponseEntity pushMessage(@RequestBody FcmRequestDto fcmRequestDto) throws IOException{
        System.out.println("fcmRequestDto.getTargetToken() = " + fcmRequestDto.getTargetToken());
        System.out.println("fcmRequestDto.getTitle() = " + fcmRequestDto.getTitle());
        System.out.println("fcmRequestDto.getBody() = " + fcmRequestDto.getBody());
        System.out.println("fcmRequestDto.getType() = " + fcmRequestDto.getType());

        firebaseCloudMessageService.sendMessageTo(
                fcmRequestDto.getTargetToken(),
                fcmRequestDto.getTitle(),
                fcmRequestDto.getBody(),
                fcmRequestDto.getType()
        );
        return ResponseEntity.ok().build();
    }
}
