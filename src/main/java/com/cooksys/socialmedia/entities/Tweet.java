package com.cooksys.socialmedia.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="tweet")
public class Tweet {
	
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime posted;
    
    private boolean deleted;
    private String content;
    
    
    @ManyToOne
    @JoinColumn (name = "tweet_id")
    private Tweet inReplyTo;
    
//  @OneToMany
//  @JoinColumn (mappedBy="tweet_id", cascade = CascadeType.ALL, orphanRemoval = true)
//  private List<Tweet> inReplyTo;
    
    @ManyToMany
    @JoinTable (
    		name = "tweet_hashtags",
    		joinColumns = @JoinColumn(name = "tweet_id"),
    		inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    	)
    private List<Hashtag> hashtags;
    
//    @OneToMany
//    @JoinColumn (mappedBy="tweet_id", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Tweet repostOf;
    
    @ManyToOne
    @JoinColumn (name = "tweet_id")
    private Tweet repostOf;

}
