package Auction_shop.auction.web.dto.address;

import Auction_shop.auction.domain.address.Address;

public interface AddressMapper {

    AddressResponseDto toResponseDto(Address address);
}
