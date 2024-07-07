package Auction_shop.auction.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customChat")
public class Chat {

    @Id
    private String id;
    private int 채팅방번호;
    private int 유저;
    private String 메세지;

    @CreatedDate
    private LocalDateTime 생성시간;
}
