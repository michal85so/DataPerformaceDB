package domain;

import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "person")
@Document(collection = "person")
public class Person {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "first_name", nullable = false, length = 45)
    @NonNull
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    @NonNull
    private String lastName;

    @Column(name = "nickname", nullable = false, length = 45)
    @NonNull
    private String nickname;

    @Column(name = "password", nullable = false, length = 45)
    @NonNull
    private String password;

    @Column(name = "phone_number", length = 9)
    private String phoneNumber;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "nip", length = 11)
    private String nip;

    @Column(name = "pesel", length = 11)
    private String pesel;

    @Column(name = "type", nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    @NonNull
    private Type type;

    public enum Type {
        Client,
        Worker;
    }

    @PersistenceConstructor
    public Person(String firstName,
                  String lastName,
                  String nickname,
                  String password,
                  String phoneNumber,
                  String email,
                  String nip,
                  String pesel,
                  Type type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nip = nip;
        this.pesel = pesel;
        this.type = type;
    }
}
