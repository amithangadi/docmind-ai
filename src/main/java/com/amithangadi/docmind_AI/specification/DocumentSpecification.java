package com.amithangadi.docmind_AI.specification;

import org.springframework.data.jpa.domain.Specification;

import com.amithangadi.docmind_AI.entity.Document;
import com.amithangadi.docmind_AI.entity.User;

public class DocumentSpecification {

	public static Specification<Document> belongsTo(User user) {

        return (root, query, cb) ->
                cb.equal(root.get("user"), user);
    }

    public static Specification<Document> containsKeyword(String keyword) {

        return (root, query, cb) -> {

            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("originalFileName")),
                            pattern
                    ),

                    cb.like(
                            cb.lower(root.get("description")),
                            pattern
                    )
            );
        };
    }

    public static Specification<Document> hasFileType(String fileType) {

        return (root, query, cb) -> {

            if (fileType == null || fileType.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("fileType"), fileType);
        };
    }
}
