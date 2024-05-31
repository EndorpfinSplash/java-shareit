package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.comment.Dto.CommentOutputDto;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(" select new ru.practicum.shareit.comment.Dto.CommentOutputDto(c.id, c.text , a.name, c.created) " +
            "from Comment c join c.item i join c.author a " +
            " where i.id = ?1 " +
            " order by c.created desc "
    )
    List<CommentOutputDto> getCommentsByItemId(Integer itemId);
}
