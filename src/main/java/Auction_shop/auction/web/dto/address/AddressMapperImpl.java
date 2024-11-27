package Auction_shop.auction.web.dto.address;

import Auction_shop.auction.domain.address.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapperImpl implements AddressMapper{

    @Override
    public AddressResponseDto toResponseDto(Address address) {
        AddressResponseDto responseDto = AddressResponseDto.builder()
                .id(address.getId())
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .name(address.getName())
                .phoneNumber(address.getPhoneNumber())
                .zipcode(address.getZipcode())
                .build();

        return responseDto;
    }
}
