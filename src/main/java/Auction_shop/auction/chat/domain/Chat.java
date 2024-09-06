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
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customChat")
public class Chat {

    @Id
    private String id;
    private Long roomId; // 채팅방번호
    private String userId;
    private String message;

    @CreatedDate
    private LocalDateTime createdAt;


}
