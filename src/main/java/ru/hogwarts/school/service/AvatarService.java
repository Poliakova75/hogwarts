package ru.hogwarts.school.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
@Service
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    @Autowired
    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }
    public Page<Avatar> getAvatars(Pageable pageable) {
        logger.info("Was invoked method getAvatars");
        Page<Avatar> avatars = avatarRepository.findAll(pageable);
        logger.debug("Retrieved {} avatars", avatars.getTotalElements());
        return avatars;
    }
    public Avatar createStudent(Avatar avatar) {
        logger.info("Was invoked method createStudent");
        try {
            Avatar savedAvatar = avatarRepository.save(avatar);
            logger.debug("Successfully created avatar with id {}", savedAvatar.getId());
            return savedAvatar;
        } catch (Exception e) {
            logger.error("Error occurred while creating student: {}", e.getMessage());
            throw e; //обработка исключения
        }
    }
    public Avatar getAvatarById(Long id) {
        logger.info("Was invoked method getAvatarById with id = {}", id);
        return avatarRepository.findById(id).orElseThrow(() -> {
            logger.error("There is no student with id = {}", id);
            return new RuntimeException("Student not found");
        });
    }
}