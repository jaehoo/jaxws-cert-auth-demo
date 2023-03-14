package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author José Alberto Sánchez González
 * Twitter: <a href="https://twitter.com/jaehoox">@jaehoox</a>
 * <p>
 * Created on 23/2/2023 20:05
 **/
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Getter
	@Setter
	private String text;

}
