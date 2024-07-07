package Auction_shop.auction.chat.repository;

import Auction_shop.auction.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat,String> {

}
