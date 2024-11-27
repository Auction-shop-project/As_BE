package Auction_shop.auction.domain.alert.service;

import Auction_shop.auction.domain.alert.Alert;
import Auction_shop.auction.domain.alert.AlertType;
import Auction_shop.auction.domain.alert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    public List<Alert> getList(Long memberId) {
        return alertRepository.findByMemberId(memberId);
    }

    @Transactional
    public Long add(Long memberId, String memberNickname, String content, AlertType alertType){
        Alert alert = Alert.builder()
                .memberId(memberId)
                .alertType(alertType)
                .memberNickname(memberNickname)
                .content(content)
                .build();

        alertRepository.save(alert);
        return alert.getId();
    }

    @Transactional
    public void delete(Long id) {
        alertRepository.deleteById(id);
    }
}
