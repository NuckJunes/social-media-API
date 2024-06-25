package com.cooksys.socialmedia.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.services.ValidateService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/validate")
@AllArgsConstructor
public class ValidateController {

	private ValidateService validateService;
}
