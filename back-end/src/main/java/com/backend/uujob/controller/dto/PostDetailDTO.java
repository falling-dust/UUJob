package com.backend.uujob.controller.dto;

import com.backend.uujob.entity.VO.CommentVO;
import lombok.Data;

import java.util.List;

/**
 * @author mapleleaf
 * @date 2023年04月16日15:44
 */
@Data
public class PostDetailDTO {
        private Integer postId;
        private String title;
        private String content;
        private String date;
        private Integer type;
        private Integer publisherId;
        private List<CommentVO> commentList;
}
