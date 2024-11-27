package Auction_shop.auction.domain.alert;

public enum AlertType {
    chatting("채팅"),
    newBid("새로운 입찰"),
    auctionTimeLimit("경매 제한 시간"),
    successfulBid("낙찰");
    private final String content;

    AlertType(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }
}
