package Auction_shop.auction.web.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private String detailAddress;
    private String zipcode;
}
