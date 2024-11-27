package Auction_shop.auction.domain.address.service;

import Auction_shop.auction.domain.address.Address;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.address.repository.AddressRepository;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.web.dto.address.AddressRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    //주소 추가
    @Transactional
    public Address addAddress(Long memberId, AddressRequestDto addressRequestDto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        Address address = Address.builder()
                .name(addressRequestDto.getName())
                .phoneNumber(addressRequestDto.getPhoneNumber())
                .address(addressRequestDto.getAddress())
                .detailAddress(addressRequestDto.getDetailAddress())
                .zipcode(addressRequestDto.getZipcode())
                .defaultAddress(false)
                .build();

        addressRepository.save(address);

        member.addAddress(address);

        return address;
    }

    //주소 삭제
    @Transactional
    public void deleteAddresses(Long memberId, List<Long> deleteList) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        List<Address> addressesToRemove = member.getAddresses().stream()
                .filter(address -> deleteList.contains(address.getId()))
                .collect(Collectors.toList());

        for (Address address : addressesToRemove) {
            if (address.isDefaultAddress()) {
                System.out.println("기본 주소지로 설정된 주소는 삭제가 불가능합니다.");
                throw new IllegalArgumentException("기본 주소지로 설정된 주소는 삭제가 불가능합니다.");
            }
        }

        member.getAddresses().removeAll(addressesToRemove);
    }

    //주소 수정
    @Transactional
    public Address updateAddress(Long memberId, Long addressId,AddressRequestDto addressRequestDto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        Optional<Address> addressToUpdate = member.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst();

        return addressToUpdate.map(address -> {
            address.updateAddress(
                    addressRequestDto.getPhoneNumber(),
                    addressRequestDto.getName(),
                    addressRequestDto.getAddress(),
                    addressRequestDto.getDetailAddress(),
                    addressRequestDto.getZipcode());
            return address;
        }).orElseThrow(() -> new IllegalArgumentException("해당 주소가 없습니다."));
    }

    //기본 주소지 설정
    @Transactional
    public void updateDefaultAddress(Long memberId, Long addressId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        member.getAddresses().forEach(address -> {
            if (address.isDefaultAddress()) {
                address.setDefaultAddress(false);
            }
        });

        Optional<Address> addressToUpdate = member.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst();

        addressToUpdate.ifPresent(address -> {
            address.setDefaultAddress(true);
        });
    }
}
