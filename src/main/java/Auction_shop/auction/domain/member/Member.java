package Auction_shop.auction.domain.member;

import Auction_shop.auction.domain.address.Address;
import Auction_shop.auction.domain.payments.Payments;
import Auction_shop.auction.util.BaseEntity;
import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(nullable = false)
    private Long point;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image profileImage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Payments> payments = new ArrayList<>();

    @Column
    @Builder.Default
    private boolean available = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @Version
    private Long version;

    public void update(String name, String nickname, String email, String phone, Address address) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        if (address != null) {
            this.addresses.add(address);
        }
        this.available = true;
    }

    public void setProfileImage(Image profileImage){
        this.profileImage = profileImage;
    }

    //연관 관계 편의 메서드
    public void addInquiry(Inquiry inquiry){
        this.inquiries.add(inquiry);
        inquiry.setMember(this);
    }

    public void removeInquiry(Inquiry inquiry){
        this.inquiries.remove(inquiry);
        inquiry.setMember(null);
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.setMember(this);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
        product.setMember(null);
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setMember(this);
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setMember(null);
    }

    public void addAddress(Address address){
        this.addresses.add(address);
    }

    public void removeAddress(Address address){
        this.addresses.remove(address);
    }
}
