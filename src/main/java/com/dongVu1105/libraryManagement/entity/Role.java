package com.dongVu1105.libraryManagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = "permissions") // Loại trừ permissions khỏi hashCode()

public class Role {
    @Id
    String name;
    String description;

    @ManyToMany
    Set<Permission> permissions;
}

/**
 Hibernate Lazy Loading: Khi bạn load một entity Role từ database, Hibernate không load ngay collection permissions. Thay vào đó, nó tạo một "proxy" và chỉ load khi bạn thực sự truy cập vào collection đó.

 Session đã đóng: Trong ApplicationRunner, sau khi method findById() thực thi xong, Hibernate session bị đóng.

 HashSet.add() gọi hashCode(): Khi bạn gọi roles.add(role), Java cần tính hashCode() của object Role để xác định vị trí lưu trữ trong HashSet.

 Lombok @Data sinh ra hashCode(): Annotation @Data tự động sinh ra method hashCode() bao gồm TẤT CẢ các field của class, kể cả permissions.

 Truy cập lazy collection: Khi hashCode() cố gắng truy cập vào permissions, Hibernate phát hiện session đã đóng → ném ra LazyInitializationException.
 * */