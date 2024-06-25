package com.cooksys.socialmedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.socialmedia.entities.Hashtag;

import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>{

}
