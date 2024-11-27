package Auction_shop.auction.chat.service;

import Auction_shop.auction.chat.domain.Chat;
import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Transactional
    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

}
