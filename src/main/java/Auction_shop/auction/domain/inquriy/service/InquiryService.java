package Auction_shop.auction.domain.inquriy.service;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.inquriy.repository.InquiryRepository;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.web.dto.inquiry.InquiryCreateDto;
import Auction_shop.auction.web.dto.inquiry.InquiryMapper;
import Auction_shop.auction.web.dto.inquiry.InquiryUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ImageService imageService;
    private final MemberService memberService;
    private final InquiryMapper inquiryMapper;

    //문의 등록
    @Transactional
    public Inquiry createInquiry(InquiryCreateDto inquiryDto, Long memberId, List<MultipartFile> images){
        Member member = memberService.getById(memberId);
        Inquiry inquiry = inquiryMapper.toEntity(inquiryDto, member);

        member.addInquiry(inquiry);

        List<Image> imageList = imageService.saveImages(images);
        inquiry.setImageList(imageList);

        return inquiryRepository.save(inquiry);
    }

    //어드민 전용 답변 등록
    @Transactional
    public Inquiry addAnswer(Long inquiryId, String answer){
        Inquiry inquiry = getById(inquiryId);
        inquiry.addAnswer(answer);
        return inquiryRepository.save(inquiry);
    }

    //문의 전체 조회 (Admin 전용)
    public List<Inquiry> getAllInquiry() {
        return inquiryRepository.findAll();
    }

    //유저 문의 조회
    public List<Inquiry> getAllByMemberId(Long memberId){
        Member member = memberService.getById(memberId);
        return inquiryRepository.findByMemberId(member.getId());
    }

    //문의 단일 조회
    public Inquiry getById(Long inquiryId){
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException(inquiryId + "에 해당하는 문의가 없습니다."));
        return inquiry;
    }

    //문의 게시글 업데이트
    @Transactional
    public Inquiry updateInquiry(Long inquiryId, InquiryUpdateDto inquiryDto, List<MultipartFile> images){
        Inquiry inquiry = getById(inquiryId);
        deleteExistingImages(inquiry);

        List<Image> imageList = imageService.saveImages(images);
        inquiry.getImageList().addAll(imageList);

        inquiry.updateInquiry(inquiryDto.getTitle(), inquiryDto.getContent());
        return inquiry;
    }

    //문의 게시글 삭제
    @Transactional
    public void deleteInquiry(Long inquiryId){
        Inquiry inquiry = getById(inquiryId);
        deleteExistingImages(inquiry);
        inquiryRepository.deleteById(inquiryId);
    }

    //사진 삭제 메서드
    private void deleteExistingImages(Inquiry inquiry){
        if (inquiry.getImageUrls() != null){
            for (Image image : inquiry.getImageList()){
                imageService.deleteImage(image.getStoredName());
            }
            inquiry.getImageList().clear();
        }
    }

}
