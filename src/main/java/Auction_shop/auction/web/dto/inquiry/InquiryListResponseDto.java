package Auction_shop.auction.web.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryListResponseDto {

    private Long id;
    private String title;
    private String content;
    private List<String> imageUrl;
    private String answer;
    private String nickname;
    private LocalDate createAt;
    private boolean status;
}
