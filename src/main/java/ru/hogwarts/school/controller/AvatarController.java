package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;
import java.awt.print.Pageable;
@RestController
@RequestMapping("/avatars")
public class AvatarController {
    private final AvatarService avatarService;
    @Autowired
    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }
    @GetMapping
    public Page<Avatar> getAvatars(Pageable pageable) {
        return (Page<Avatar>) avatarService.getAvatars(pageable);
    }
}
