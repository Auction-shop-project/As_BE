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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = false)
    private String deviceToken;

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

    @ElementCollection
    @Column(nullable = false)
    @Builder.Default
    private Set<String> categories = new HashSet<>();

    @Column
    @Builder.Default
    private boolean available = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @Version
    private Long version;

    public void update(String name, String nickname, String email, String phone, Address address, Set<String> categories) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        if (address != null) {
            this.addresses.add(address);
        }
        if (categories != null) {
            this.categories.clear();
            this.categories = categories;
        }
        this.available = true;
    }

    public void setProfileImage(Image profileImage){
        this.profileImage = profileImage;
    }

    public void updateDeviceToken(String deviceToken){
        this.deviceToken = deviceToken;
    }

    //연관 관계 편의 메서드
    public void addInquiry(Inquiry inquiry){
        this.inquiries.add(inquiry);
        inquiry.setMember(this);
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.setMember(this);
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

    public void updateRole(String role){
        this.role = role;
    }

    public void addPoint(Long point){
        this.point += point;
    }

}
